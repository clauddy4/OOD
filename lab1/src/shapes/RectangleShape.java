package shapes;

import java.awt.*;

public class RectangleShape implements Shape {
    private Point topLeftVertex;
    private Point bottomRightVertex;

    public RectangleShape(Point topLeftVertex, Point bottomRightVertex) {
        this.topLeftVertex = topLeftVertex;
        this.bottomRightVertex = bottomRightVertex;
    }

    public Point getTopLeftVertex() {
        return topLeftVertex;
    }

    public Point getBottomRightVertex() {
        return bottomRightVertex;
    }
}