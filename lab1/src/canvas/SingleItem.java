package canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;

public class SingleItem implements Item {
    private boolean selected = false;
    private Shape shape;
    private Color color;
    private AffineTransform transform;

    SingleItem(Shape shape, Color color, AffineTransform transform) {
        this.shape = shape;
        this.color = color;
        this.transform = transform;
    }

    @Override
    public void paint(Graphics2D g2d) {
        AffineTransform saved = g2d.getTransform();
        g2d.transform(transform);
        g2d.setColor(color);
        g2d.fill(shape);
        g2d.setTransform(saved);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean containsPoint(Point2D point) {
        Point2D transformed = null;
        try {
            if (shape.contains(transform.inverseTransform(point, transformed)))
                return true;
        } catch (NoninvertibleTransformException ignored) {
            // noop
        }
        return false;
    }

    @Override
    public AffineTransform getTransform() {
        return transform;
    }

    @Override
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    @Override
    public Point getTransformedPoint(Point point) {
        Point2D pt = this.transform.transform(point, null);
        return new Point((int) pt.getX(), (int) pt.getY());
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unselect() {
        selected = false;
    }

    @Override
    public void ungroup(List<Item> items) {

    }
}