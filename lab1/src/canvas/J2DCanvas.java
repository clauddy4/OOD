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
import static java.awt.event.MouseEvent.BUTTON1;

public class J2DCanvas extends JComponent implements Canvas {
    private final transient DragShapeAdapter dragShapeAdapter = new DragShapeAdapter();
    private final transient KeyShapeAdapter keyShapeAdapter = new KeyShapeAdapter();

    private transient List<CanvasShape> shapes = new ArrayList<>();

    private boolean selecting = false;
    private boolean ctrl = false;
    private Point currPoint;

    private Color selectionStrokeColor = new Color(33, 150, 243);
    private Color selectionFillColor = new Color(33, 150, 243, 30);

    private int selectionStrokeWidth = 1;
    private transient BasicStroke selectionStroke = new BasicStroke(selectionStrokeWidth);

    public J2DCanvas() {
        addMouseListener(dragShapeAdapter);
        addMouseMotionListener(dragShapeAdapter);
        addKeyListener(keyShapeAdapter);
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
        shapes.add(new SingleCanvasShape(path, fillColor, transform));
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
        shapes.add(new SingleCanvasShape(ellipse, fillColor, transform));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        int selectionTopLeftX = Integer.MAX_VALUE;
        int selectionTopLeftY = Integer.MAX_VALUE;
        int selectionBottomRightX = Integer.MIN_VALUE;
        int selectionBottomRightY = Integer.MIN_VALUE;
        for (CanvasShape shape : shapes) {
            shape.paint(g2d);
            if (shape.isSelected()) {
                Rectangle bounds = shape.getBounds();
                Point2D itemTopLeft = shape.getTransformedPoint(new Point(bounds.x, bounds.y));
                Point2D itemBottomRight = shape.getTransformedPoint(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
                selectionTopLeftX = (int) Math.min(selectionTopLeftX, itemTopLeft.getX());
                selectionTopLeftY = (int) Math.min(selectionTopLeftY, itemTopLeft.getY());
                selectionBottomRightX = (int) Math.max(selectionBottomRightX, itemBottomRight.getX());
                selectionBottomRightY = (int) Math.max(selectionBottomRightY, itemBottomRight.getY());
            }
        }
        g2d.setColor(selectionStrokeColor);
        g2d.setStroke(selectionStroke);
        g2d.drawRect(
                selectionTopLeftX - selectionStrokeWidth,
                selectionTopLeftY - selectionStrokeWidth,
                selectionBottomRightX - selectionTopLeftX + selectionStrokeWidth,
                selectionBottomRightY - selectionTopLeftY + selectionStrokeWidth
        );
        g2d.setColor(selectionFillColor);
        g2d.fillRect(
                selectionTopLeftX - selectionStrokeWidth,
                selectionTopLeftY - selectionStrokeWidth,
                selectionBottomRightX - selectionTopLeftX + selectionStrokeWidth,
                selectionBottomRightY - selectionTopLeftY + selectionStrokeWidth
        );
    }

    public CanvasShape findCanvasShape(Point2D point) {
        ListIterator<CanvasShape> iter = shapes.listIterator(shapes.size());
        while (iter.hasPrevious()) {
            CanvasShape shape = iter.previous();
            if (shape.containsPoint(point)) {
                return shape;
            }
        }
        return null;
    }

    @Override
    public List<CanvasShape> getCanvasShapes() {
        return shapes;
    }

    private class KeyShapeAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                ctrl = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                selecting = true;
            }
            if (ctrl && e.getKeyCode() == KeyEvent.VK_G) {
                List<CanvasShape> group = shapes.stream().filter(CanvasShape::isSelected).collect(Collectors.toList());
                shapes = shapes.stream().filter(item -> !item.isSelected()).collect(Collectors.toList());
                CompositeCanvasShape composite = new CompositeCanvasShape(group);
                shapes.add(composite);
                dragShapeAdapter.setHoverCanvasShape(composite);
                repaint();
            }
            if (ctrl && e.getKeyCode() == KeyEvent.VK_U) {
                var selected = shapes.stream().filter(CanvasShape::isSelected).findFirst();
                selected.ifPresent(item -> {
                    item.unselect();
                    item.ungroup(shapes);
                });
                CanvasShape hoverCanvasShape = findCanvasShape(currPoint);
                if (hoverCanvasShape != null) {
                    hoverCanvasShape.select();
                    dragShapeAdapter.setHoverCanvasShape(hoverCanvasShape);
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
        private CanvasShape hoverCanvasShape;
        private boolean dragging;
        private Cursor savedCursor;

        void setHoverCanvasShape(CanvasShape hoverCanvasShape) {
            this.hoverCanvasShape = hoverCanvasShape;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!dragging) {
                return;
            }
            Point2D point = e.getPoint();
            double tx = point.getX() - currPoint.getX();
            double ty = point.getY() - currPoint.getY();
            AffineTransform transform = AffineTransform.getTranslateInstance(tx, ty);
            transform.concatenate(hoverCanvasShape.getTransform());
            hoverCanvasShape.setTransform(transform);
            repaint();

            currPoint = new Point(e.getPoint());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point2D point = e.getPoint();
            CanvasShape shape = findCanvasShape(point);
            if (hoverCanvasShape == null && shape != null) {
                savedCursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
            } else if (hoverCanvasShape != null && shape == null) {
                setCursor(savedCursor);
            }
            hoverCanvasShape = shape;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!selecting) {
                shapes.forEach(CanvasShape::unselect);
            }
            if (e.getButton() == BUTTON1 && !dragging && hoverCanvasShape != null) {
                dragging = true;
                currPoint = new Point(e.getPoint());
                var found = findCanvasShape(e.getPoint());
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
