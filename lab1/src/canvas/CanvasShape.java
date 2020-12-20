package canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

public interface CanvasShape {
    void paint(Graphics2D g2d);

    boolean isSelected();

    boolean containsPoint(Point2D point);

    AffineTransform getTransform();

    void setTransform(AffineTransform transform);

    Rectangle getBounds();

    void setColor(Color color);

    Color getColor();

    void setStroke(int stroke);

    void setStrokeColor(Color color);

    int getStroke();

    Color getStrokeColor();

    Point getTransformedPoint(Point point);

    void select();

    void unselect();

    void ungroup(List<CanvasShape> canvasShapes);
}