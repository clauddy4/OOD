package canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeItem implements Item {
    private AffineTransform at = new AffineTransform();
    private List<Item> items;
    private List<AffineTransform> transforms;

    CompositeItem(List<Item> items) {
        this.items = items;
        transforms = items
                .stream()
                .map(item -> new AffineTransform(item.getTransform()))
                .collect(Collectors.toList());
    }

    @Override
    public void paint(Graphics2D g2d) {
        items.forEach(item -> item.paint(g2d));
    }

    @Override
    public boolean isSelected() {
        return items.get(0).isSelected();
    }

    @Override
    public boolean containsPoint(Point2D point) {
        for (Item item : items) {
            if (item.containsPoint(point)) {
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
        for (Item item : items) {
            var currentItemTransform = transforms.get(index);
            var currentTranslation = new Point((int) currentItemTransform.getTranslateX(), (int) currentItemTransform.getTranslateY());
            var delta = transform.deltaTransform(currentTranslation, null);
            var deltaTransform = AffineTransform.getTranslateInstance(delta.getX(), delta.getY());
            var tempTransform = new AffineTransform(transform);
            tempTransform.concatenate(deltaTransform);
            item.setTransform(tempTransform);
            index++;
        }
    }

    @Override
    public Color getColor() {
        return items.get(0).getColor();
    }

    @Override
    public Rectangle getBounds() {
        Rectangle result = new Rectangle();
        int selectionTopLeftX = Integer.MAX_VALUE;
        int selectionTopLeftY = Integer.MAX_VALUE;
        int selectionBottomRightX = Integer.MIN_VALUE;
        int selectionBottomRightY = Integer.MIN_VALUE;
        for (Item item : items) {
            Rectangle bounds = item.getBounds();
            Point2D itemTopLeft = item.getTransformedPoint(new Point(bounds.x, bounds.y));
            Point2D itemBottomRight = item.getTransformedPoint(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
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
    public Point getTransformedPoint(Point point) {
        return point;
    }

    @Override
    public void select() {
        items.forEach(Item::select);
    }

    @Override
    public void unselect() {
        items.forEach(Item::unselect);
    }

    @Override
    public void ungroup(List<Item> items) {
        items.removeIf(item -> item == this);
        items.addAll(this.items);
    }
}