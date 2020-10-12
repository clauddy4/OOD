package canvas;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.event.InputEvent.BUTTON3_DOWN_MASK;
import static java.awt.event.MouseEvent.BUTTON1;

public class J2DCanvas extends JComponent implements Canvas {
    private transient List<Item> items;
    private boolean selecting = false;

    public J2DCanvas() {
        items = new ArrayList<>();
        MouseAdapter mouseAdapter = new DragShapeAdapter();
        KeyAdapter keyAdapter = new KeyShapeAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addKeyListener(keyAdapter);
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
        items.add(new Item(path, fillColor, transform));
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
        items.add(new Item(ellipse, fillColor, transform));
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
            AffineTransform saved = g2d.getTransform();
            g2d.transform(item.transform);
            g2d.setColor(item.color);
            g2d.fill(item.shape);
            g2d.setTransform(saved);
            if (item.selected) {
                Rectangle bounds = item.shape.getBounds();
                Point2D itemTopLeft = item.transform.transform(new Point(bounds.x, bounds.y), null);
                Point2D itemBottomRight = item.transform.transform(new Point(bounds.x + bounds.width, bounds.y + bounds.height), null);
                selectionTopLeftX = (int) Math.min(selectionTopLeftX, itemTopLeft.getX());
                selectionTopLeftY = (int) Math.min(selectionTopLeftY, itemTopLeft.getY());
                selectionBottomRightX = (int) Math.max(selectionBottomRightX, itemBottomRight.getX());
                selectionBottomRightY = (int) Math.max(selectionBottomRightY, itemBottomRight.getY());
            }
        }
        if (selectionTopLeftX != 0 &&
            selectionTopLeftY != 0 &&
            selectionBottomRightX != 0 &&
            selectionBottomRightY != 0) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(
                selectionTopLeftX - 2,
                selectionTopLeftY - 2,
                selectionBottomRightX - selectionTopLeftX + 2,
                selectionBottomRightY - selectionTopLeftY + 2
            );
        }
    }

    private class Item {
        Shape shape;
        Color color;
        AffineTransform transform;
        boolean selected = false;

        Item(Shape shape, Color color, AffineTransform transform) {
            this.shape = shape;
            this.color = color;
            this.transform = transform;
        }
    }

    private class KeyShapeAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                selecting = true;
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
        private Point prevPoint;
        private Cursor savedCursor;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!dragging) return;

            if ((e.getModifiersEx() & BUTTON3_DOWN_MASK) == 0) {
                Point2D point = e.getPoint();
                double tx = point.getX() - prevPoint.getX();
                double ty = point.getY() - prevPoint.getY();
                AffineTransform transform = AffineTransform.getTranslateInstance(tx, ty);
                transform.concatenate(hoverItem.transform);
                hoverItem.transform = transform;
                repaint();
            }
            prevPoint = new Point(e.getPoint());
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

        @SuppressWarnings("ConstantConditions")
        private Item findItem(Point2D point) {
            // process items in the order reversed to drawing order
            ListIterator<Item> iter = items.listIterator(items.size());
            while (iter.hasPrevious()) {
                Item item = iter.previous();
                AffineTransform transform = item.transform;
                Shape shape = item.shape;
                Point2D transformed = null;
                try {
                    if (shape.contains(transform.inverseTransform(point, transformed)))
                        return item;
                } catch (NoninvertibleTransformException ignored) {
                    // noop
                }
            }
            return null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == BUTTON1 && !dragging && hoverItem != null) {
                dragging = true;
                prevPoint = new Point(e.getPoint());
                var found = findItem(e.getPoint());
                if (found != null && selecting) {
                    found.selected = !found.selected;
                } else {
                    selecting = false;
                    for (Item item : items) {
                        item.selected = false;
                    }
                }
            } else {
                selecting = false;
                for (Item item : items) {
                    item.selected = false;
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
