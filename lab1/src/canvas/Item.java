package canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

public interface Item {
    void paint(Graphics2D g2d);

    boolean isSelected();

    boolean containsPoint(Point2D point);

    AffineTransform getTransform();

    void setTransform(AffineTransform transform);

    Color getColor();

    Rectangle getBounds();

    Point getTransformedPoint(Point point);

    void select();

    void unselect();

    void ungroup(List<Item> items);
}