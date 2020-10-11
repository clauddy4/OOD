package canvas;

import java.awt.*;
import main.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.event.InputEvent.BUTTON3_DOWN_MASK;
import static java.awt.event.MouseEvent.BUTTON1;

public class J2DCanvas implements Canvas, CanvasPanelDrawable {
    private final List<ShapeObject> shapes = new ArrayList<>();

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
        shapes.add(new ShapeObject(path, fillColor, transform));
    }

    @Override
    public void drawCircle(Point center, double radius, Color fillColor) {
        Ellipse2D.Double ellipse = new Ellipse2D.Double(center.x, center.y, radius, radius);
        AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
        shapes.add(new ShapeObject(ellipse, fillColor, transform));
    }

    @Override
    public void draw(Graphics2D g2d) {
        shapes.forEach(shape -> {
            AffineTransform saved = g2d.getTransform();
            g2d.setTransform(shape.transform);
            g2d.setColor(shape.color);
            g2d.fill(shape.shape);
            g2d.setTransform(saved);
        });
    }

    @Override
    public MouseAdapter getMouseAdapter(JPanel panel) {
        return new DragShape(panel);
    }

    private static class ShapeObject {
        final java.awt.Shape shape;
        final Color color;
        AffineTransform transform;

        private ShapeObject(Shape shape, Color color, AffineTransform transform) {
            this.shape = shape;
            this.color = color;
            this.transform = transform;
        }
    }

    private class DragShape extends MouseAdapter {
        private final JPanel panel;
        private ShapeObject hoverShapeObject;
        private boolean isDragging;
        private java.awt.Point prevPoint;
        private Cursor savedCursor;

        DragShape(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isDragging) {
                return;
            }
            if ((e.getModifiersEx() & BUTTON3_DOWN_MASK) == 0) {
                java.awt.Point point = e.getPoint();
                double tx = point.getX() - prevPoint.getX();
                double ty = point.getY() - prevPoint.getY();
                AffineTransform transform = AffineTransform.getTranslateInstance(tx, ty);
                transform.concatenate(hoverShapeObject.transform);
                hoverShapeObject.transform = transform;
                panel.repaint();
            }
            prevPoint = new java.awt.Point(e.getPoint());
        }

        @SuppressWarnings("ConstantConditions")
        private ShapeObject findItem(Point2D point) {
            ListIterator<ShapeObject> it = shapes.listIterator(shapes.size());
            while (it.hasPrevious()) {
                ShapeObject object = it.previous();
                AffineTransform transform = object.transform;
                java.awt.Shape shape = object.shape;
                Point2D transformedPoint = null;
                try {
                    if (shape.contains(transform.inverseTransform(point, transformedPoint))) {
                        return object;
                    }
                } catch (NoninvertibleTransformException ignored) {
                    // noop
                }
            }
            return null;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            java.awt.Point point = e.getPoint();
            ShapeObject object = findItem(point);
            if (hoverShapeObject == null && object != null) {
                savedCursor = panel.getCursor();
                panel.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
            } else if (hoverShapeObject != null && object == null) {
                panel.setCursor(savedCursor);
            }
            hoverShapeObject = object;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == BUTTON1 && !isDragging && hoverShapeObject != null) {
                isDragging = true;
                prevPoint = new java.awt.Point(e.getPoint());
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