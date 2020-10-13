package canvas;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.event.InputEvent.BUTTON3_DOWN_MASK;
import static java.awt.event.MouseEvent.BUTTON1;

public class J2DCanvas extends JComponent implements Canvas {
    private final transient DragShapeAdapter drag = new DragShapeAdapter();
    private transient List<Item> items = new ArrayList<>();
    private boolean selecting = false;
    private Point currPoint;
    private Color selectionStrokeColor = new Color(33, 150, 243);
    private Color selectionFillColor = new Color(33, 150, 243, 30);
    private int selectionWidth = 1;
    private transient BasicStroke selectionStroke = new BasicStroke(selectionWidth);

    public J2DCanvas() {
        addMouseListener(drag);
        addMouseMotionListener(drag);
        addKeyListener(new KeyShapeAdapter());
    }

    @Override
    public void drawPolygon(List<Point> points, Color fillColor) {
        Path2D.Double path = new Path2D.Double();
        ListIterator<Point> it = points.listIterator();
        Point first = it.next();
        path.moveTo(first.x, first.y);
        while (it.hasNext()) {
            Point current = it.next();
            path.lineTo(current.x, current.y);
        }
        path.closePath();
        AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
        items.add(new SingleItem(path, fillColor, transform));
    }

    @Override
    public void fillPolygon(List<Point> points, int fillColor) {

    }

    @Override
    public void drawCircle(Point center, double radius, int lineColor) {

    }

    @Override
    public void drawCircle(Point center, double radius, Color fillColor) {
        Ellipse2D.Double ellipse = new Ellipse2D.Double(center.x, center.y, radius, radius);
        AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
        items.add(new SingleItem(ellipse, fillColor, transform));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        int selectionTopLeftX = Integer.MAX_VALUE;
        int selectionTopLeftY = Integer.MAX_VALUE;
        int selectionBottomRightX = Integer.MIN_VALUE;
        int selectionBottomRightY = Integer.MIN_VALUE;
        for (Item item : items) {
            item.paint(g2d);
            if (item.isSelected()) {
                Rectangle bounds = item.getBounds();
                Point2D itemTopLeft = item.getTransformedPoint(new Point(bounds.x, bounds.y));
                Point2D itemBottomRight = item.getTransformedPoint(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
                selectionTopLeftX = (int) Math.min(selectionTopLeftX, itemTopLeft.getX());
                selectionTopLeftY = (int) Math.min(selectionTopLeftY, itemTopLeft.getY());
                selectionBottomRightX = (int) Math.max(selectionBottomRightX, itemBottomRight.getX());
                selectionBottomRightY = (int) Math.max(selectionBottomRightY, itemBottomRight.getY());
            }
        }
        g2d.setColor(selectionStrokeColor);
        g2d.setStroke(selectionStroke);
        g2d.drawRect(
                selectionTopLeftX - selectionWidth,
                selectionTopLeftY - selectionWidth,
                selectionBottomRightX - selectionTopLeftX + selectionWidth,
                selectionBottomRightY - selectionTopLeftY + selectionWidth
        );
        g2d.setColor(selectionFillColor);
        g2d.fillRect(
                selectionTopLeftX - selectionWidth,
                selectionTopLeftY - selectionWidth,
                selectionBottomRightX - selectionTopLeftX + selectionWidth,
                selectionBottomRightY - selectionTopLeftY + selectionWidth
        );
    }

    private Item findItem(Point2D point) {
        ListIterator<Item> iter = items.listIterator(items.size());
        while (iter.hasPrevious()) {
            Item item = iter.previous();
            if (item.containsPoint(point)) {
                return item;
            }
        }
        return null;
    }

    private class KeyShapeAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                selecting = true;
            }
            if (selecting && e.getKeyCode() == KeyEvent.VK_G) {
                List<Item> group = items.stream().filter(Item::isSelected).collect(Collectors.toList());
                items = items.stream().filter(item -> !item.isSelected()).collect(Collectors.toList());
                CompositeItem composite = new CompositeItem(group);
                items.add(composite);
                drag.setHoverItem(composite);
                repaint();
            }
            if (selecting && e.getKeyCode() == KeyEvent.VK_U) {
                var selected = items.stream().filter(Item::isSelected).findFirst();
                selected.ifPresent(item -> {
                    item.unselect();
                    item.ungroup(items);
                });
                Item hoverItem = findItem(currPoint);
                if (hoverItem != null) {
                    hoverItem.select();
                    drag.setHoverItem(hoverItem);
                }
                repaint();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                selecting = false;
            }
        }
    }

    private class DragShapeAdapter extends MouseAdapter {
        private Item hoverItem;
        private boolean dragging;
        private Cursor savedCursor;

        void setHoverItem(Item hoverItem) {
            this.hoverItem = hoverItem;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!dragging) {
                return;
            }

            if ((e.getModifiersEx() & BUTTON3_DOWN_MASK) == 0) {
                Point2D point = e.getPoint();
                double tx = point.getX() - currPoint.getX();
                double ty = point.getY() - currPoint.getY();
                AffineTransform transform = AffineTransform.getTranslateInstance(tx, ty);
                transform.concatenate(hoverItem.getTransform());
                hoverItem.setTransform(transform);
                repaint();
            }
            currPoint = new Point(e.getPoint());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point2D point = e.getPoint();
            Item item = findItem(point);
            if (hoverItem == null && item != null) {
                savedCursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
            } else if (hoverItem != null && item == null) {
                setCursor(savedCursor);
            }
            hoverItem = item;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!selecting) {
                items.forEach(Item::unselect);
            }
            if (e.getButton() == BUTTON1 && !dragging && hoverItem != null) {
                dragging = true;
                currPoint = new Point(e.getPoint());
                var found = findItem(e.getPoint());
                if (found != null) {
                    found.select();
                }
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == BUTTON1 && dragging) {
                dragging = false;
            }
        }
    }
}
