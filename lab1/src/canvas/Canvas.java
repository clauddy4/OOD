package canvas;

import java.awt.*;
import java.util.List;

public interface Canvas {
    void drawPolygon(List<Point> points, Color fillColor);

    void fillPolygon(List<Point> points, int fillColor);

    void drawCircle(Point center, double radius, int lineColor);

    void drawCircle(Point center, double radius, Color fillColor);
}