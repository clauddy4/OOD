package canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeCanvasShape implements CanvasShape {
    private AffineTransform at = new AffineTransform();
    private List<CanvasShape> shapes;
    private List<AffineTransform> transforms;

    CompositeCanvasShape(List<CanvasShape> shapes) {
        this.shapes = shapes;
        transforms = shapes
                .stream()
                .map(item -> new AffineTransform(item.getTransform()))
                .collect(Collectors.toList());
    }

    @Override
    public void paint(Graphics2D g2d) {
        shapes.forEach(item -> item.paint(g2d));
    }

    @Override
    public boolean isSelected() {
        return shapes.get(0).isSelected();
    }

    @Override
    public boolean containsPoint(Point2D point) {
        for (CanvasShape shape : shapes) {
            if (shape.containsPoint(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AffineTransform getTransform() {
        return at;
    }

    @Override
    public void setTransform(AffineTransform transform) {
        at = transform;
        int index = 0;
        for (CanvasShape shape : shapes) {
            var currentItemTransform = transforms.get(index);
            var currentTranslation = new Point((int) currentItemTransform.getTranslateX(), (int) currentItemTransform.getTranslateY());
            var delta = transform.deltaTransform(currentTranslation, null);
            var deltaTransform = AffineTransform.getTranslateInstance(delta.getX(), delta.getY());
            var tempTransform = new AffineTransform(transform);
            tempTransform.concatenate(deltaTransform);
            shape.setTransform(tempTransform);
            index++;
        }
    }

    @Override
    public Rectangle getBounds() {
        Rectangle result = new Rectangle();
        int selectionTopLeftX = Integer.MAX_VALUE;
        int selectionTopLeftY = Integer.MAX_VALUE;
        int selectionBottomRightX = Integer.MIN_VALUE;
        int selectionBottomRightY = Integer.MIN_VALUE;
        for (CanvasShape shape : shapes) {
            Rectangle bounds = shape.getBounds();
            Point2D itemTopLeft = shape.getTransformedPoint(new Point(bounds.x, bounds.y));
            Point2D itemBottomRight = shape.getTransformedPoint(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
            selectionTopLeftX = (int) Math.min(selectionTopLeftX, itemTopLeft.getX());
            selectionTopLeftY = (int) Math.min(selectionTopLeftY, itemTopLeft.getY());
            selectionBottomRightX = (int) Math.max(selectionBottomRightX, itemBottomRight.getX());
            selectionBottomRightY = (int) Math.max(selectionBottomRightY, itemBottomRight.getY());
        }
        result.x = selectionTopLeftX;
        result.y = selectionTopLeftY;
        result.height = selectionBottomRightY - selectionTopLeftY;
        result.width = selectionBottomRightX - selectionTopLeftX;
        return result;
    }

    @Override
    public void setColor(Color color) {
        for (CanvasShape shape : shapes) {
            shape.setColor(color);
        }
    }

    @Override
    public int getStroke() {
        return shapes.get(0).getStroke();
    }

    @Override
    public Color getStrokeColor() {
        return shapes.get(0).getStrokeColor();
    }

    @Override
    public Color getColor() {
        return shapes.get(0).getColor();
    }

    @Override
    public void setStroke(int stroke) {
        for (CanvasShape shape : shapes) {
            shape.setStroke(stroke);
        }
    }

    @Override
    public void setStrokeColor(Color color) {
        for (CanvasShape shape : shapes) {
            shape.setStrokeColor(color);
        }
    }

    @Override
    public Point getTransformedPoint(Point point) {
        return point;
    }

    @Override
    public void select() {
        shapes.forEach(CanvasShape::select);
    }

    @Override
    public void unselect() {
        shapes.forEach(CanvasShape::unselect);
    }

    @Override
    public void ungroup(List<CanvasShape> canvasShapes) {
        canvasShapes.removeIf(item -> item == this);
        canvasShapes.addAll(this.shapes);
    }
}