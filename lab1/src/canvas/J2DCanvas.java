package canvas;

import java.awt.*;
import main.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class J2DCanvas implements Canvas, CanvasPanelDrawable {
    private final List<ShapeObject> shapes = new ArrayList<>();
    private final List<PolygonShape> polygons = new ArrayList<>();
    private final List<CircleShape> circles = new ArrayList<>();

    @Override
    public void drawLine(Point from, Point to, int lineColor) {
        shapes.add(new ShapeObject(new Line2D.Double(from.x, from.y, to.x, to.y), lineColor));
    }

    @Override
    public void fillPolygon(List<Point> points, int fillColor) {
        polygons.add(new PolygonShape(points, fillColor));
    }

    @Override
    public void drawCircle(Point center, double radius, int lineColor) {
        shapes.add(new ShapeObject(new Ellipse2D.Double(center.x, center.y, radius, radius), lineColor));
    }

    @Override
    public void fillCircle(Point center, double radius, int fillColor) {
        circles.add(new CircleShape(center, radius, fillColor));
    }

    @Override
    public void draw(Graphics2D g2d) {
        shapes.forEach(shape -> {
            g2d.setPaint(getHexColor(shape.color));
            g2d.draw(shape.shape);
        });
        polygons.forEach(polygon -> {
            g2d.setColor(getHexColor(polygon.color));
            Path2D path = new Path2D.Double();
            List<Point> points = polygon.points;
            path.moveTo(points.get(0).x, points.get(0).y);
            points.stream().skip(1).forEach(point -> path.lineTo(point.x, point.y));
            path.closePath();
            g2d.fill(path);
        });
        circles.forEach(circle -> {
            g2d.setColor(getHexColor(circle.color));
            Point center = circle.center;
            double radius = circle.radius;
            g2d.fill(new Ellipse2D.Double(center.x, center.y, radius, radius));
        });
    }

    private static class CircleShape {
        final Point center;
        final double radius;
        final int color;

        private CircleShape(Point center, double radius, int color) {
            this.center = center;
            this.radius = radius;
            this.color = color;
        }
    }

    private static class PolygonShape {
        final List<Point> points;
        final int color;

        private PolygonShape(List<Point> points, int color) {
            this.points = points;
            this.color = color;
        }
    }

    private static class ShapeObject {
        final java.awt.Shape shape;
        final int color;

        private ShapeObject(java.awt.Shape shape, int color) {
            this.shape = shape;
            this.color = color;
        }
    }

    private Color getHexColor(int color) {
        return Color.decode('#' + Integer.toHexString(color));
    }
}