package test;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ShapesDragging extends JComponent {
    private static class Item {
        public Shape shape;
        public Color color;
        public AffineTransform transform;
    }

    private java.util.List<Item> items;
    private double dx;
    private double dy;
    private double scale;

    public ShapesDragging() {
        items = new ArrayList<Item>();
        dx = 0.0;
        dy = 0.0;
        scale = 1.0;
        DragShape dragShape = new DragShape();
        PanZoom panZoom = new PanZoom();
        addMouseListener(dragShape);
        addMouseListener(panZoom);
        addMouseMotionListener(dragShape);
        addMouseMotionListener(panZoom);
        addMouseWheelListener(panZoom);
    }

    public void addItem(Shape shape, Color color) {
        AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
        addItem(shape, color, transform);
    }

    public void addItem(Shape shape, Color color, AffineTransform transform) {
        Item item = new Item();
        item.shape = shape;
        item.color = color;
        item.transform = transform;
        items.add(item);
    }

    public void translate(double tx, double ty) {
        dx += tx * scale;
        dy += ty * scale;
    }

    public void scale(double factor, double x, double y) {
        double coef = scale * (1.0 - factor);
        dx += coef * x;
        dy += coef * y;
        scale *= factor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(dx, dy);
        g2d.scale(scale, scale);
        for (Item item : items) {
            AffineTransform saved = g2d.getTransform();
            g2d.transform(item.transform);
            g2d.setColor(item.color);
            g2d.fill(item.shape);
            g2d.setTransform(saved);
        }
    }

    private Point2D mapToScene(Point point) {
        double x = (point.x - dx) / scale;
        double y = (point.y - dy) / scale;
        return new Point2D.Double(x, y);
    }

    private Item findItem(Point2D point) {
        // process items in the order reversed to drawing order
        ListIterator<Item> iter = items.listIterator(items.size());
        while (iter.hasPrevious()) {
            Item item = iter.previous();
            AffineTransform transform = item.transform;
            Shape shape = item.shape;
            Point2D transfPoint = null;
            try {
                if (shape.contains(transform.inverseTransform(point, transfPoint)))
                    return item;
            } catch (NoninvertibleTransformException e) {
            }
        }
        return null;
    }

    private class DragShape extends MouseAdapter {
        private Item hoverItem;
        private boolean isDragging;
        private Point previous;
        private Cursor savedCursor;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isDragging)
                return;
            // don't move item if both mouse buttons are pressed
            if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0) {
                Point2D point = mapToScene(e.getPoint());
                Point2D prevPoint = mapToScene(previous);
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
            Point2D point = mapToScene(e.getPoint());
            Item item = findItem(point);
            if (hoverItem == null && item != null) {
                savedCursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else if (hoverItem != null && item == null) {
                setCursor(savedCursor);
            }
            hoverItem = item;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && !isDragging && hoverItem != null) {
                isDragging = true;
                previous = new Point(e.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
                isDragging = false;
            }
        }
    }

    private class PanZoom extends MouseAdapter {
        private boolean isDragging;
        private Point previous;
        private Cursor savedCursor;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 && !isDragging) {
                isDragging = true;
                previous = new Point(e.getPoint());
                savedCursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 && isDragging) {
                isDragging = false;
                setCursor(savedCursor);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragging) {
                Point2D point = mapToScene(e.getPoint());
                Point2D prevPoint = mapToScene(previous);
                double tx = point.getX() - prevPoint.getX();
                double ty = point.getY() - prevPoint.getY();
                translate(tx, ty);
                previous = new Point(e.getPoint());
                repaint();
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double factor = Math.pow(1.2, e.getWheelRotation());
            Point2D point = mapToScene(e.getPoint());
            scale(factor, point.getX(), point.getY());
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Shapes Dragging");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ShapesDragging shapesDragging = new ShapesDragging();
                shapesDragging.addItem(new Rectangle2D.Double(30, 30, 150, 100), Color.RED);
                shapesDragging.addItem(new Ellipse2D.Double(200, 150, 200, 100), Color.BLUE,
                        AffineTransform.getRotateInstance(0.7, 300, 200));
                Path2D.Double path = new Path2D.Double();
                path.moveTo(400, 10);
                path.lineTo(450, 150);
                path.lineTo(350, 150);
                path.closePath();
                shapesDragging.addItem(path, Color.GREEN);
                path = new Path2D.Double();
                path.moveTo(30, 220);
                path.lineTo(150, 220);
                path.append(new Arc2D.Double(120, 220, 100, 100, 90, -180, Arc2D.OPEN), true);
                path.lineTo(30, 320);
                path.closePath();
                Stroke stroke = new BasicStroke(20.0f);
                shapesDragging.addItem(stroke.createStrokedShape(path), Color.MAGENTA);
                path = new Path2D.Double();
                path.moveTo(500, 200);
                path.curveTo(600, 300, 550, 400, 450, 250);
                path.closePath();
                shapesDragging.addItem(stroke.createStrokedShape(path), Color.YELLOW);
                frame.getContentPane().add(shapesDragging);
                frame.setSize(600, 400);
                frame.setVisible(true);
            }
        });
    }
}