package canvas;

import java.awt.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.event.InputEvent.BUTTON3_DOWN_MASK;
import static java.awt.event.MouseEvent.BUTTON1;

public class J2DCanvas extends JComponent implements Canvas {
    private transient List<Item> items;

    public J2DCanvas() {
        items = new ArrayList<>();
        DragShape dragShape = new DragShape();
        addMouseListener(dragShape);
        addMouseMotionListener(dragShape);
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Item item : items) {
            AffineTransform saved = g2d.getTransform();
            g2d.transform(item.transform);
            g2d.setColor(item.color);
            g2d.fill(item.shape);
            g2d.setTransform(saved);
        }
    }

    private class Item {
        Shape shape;
        Color color;
        AffineTransform transform;

        Item(Shape shape, Color color, AffineTransform transform) {
            this.shape = shape;
            this.color = color;
            this.transform = transform;
        }
    }

    private class DragShape extends MouseAdapter {
        private Item hoverItem;
        private boolean isDragging;
        private Point previous;
        private Cursor savedCursor;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isDragging) return;

            if ((e.getModifiersEx() & BUTTON3_DOWN_MASK) == 0) {
                Point2D point = e.getPoint();
                Point2D prevPoint = previous;
                double tx = point.getX() - prevPoint.getX();
                double ty = point.getY() - prevPoint.getY();
                AffineTransform transform = AffineTransform.getTranslateInstance(tx, ty);
                transform.concatenate(hoverItem.transform);
                hoverItem.transform = transform;
                repaint();
            }
            previous = new Point(e.getPoint());
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
            if (e.getButton() == BUTTON1 && !isDragging && hoverItem != null) {
                isDragging = true;
                previous = new Point(e.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == BUTTON1 && isDragging) {
                isDragging = false;
            }
        }
    }
}
