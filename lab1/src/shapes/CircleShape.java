package shapes;

import canvas.Canvas;
import main.Point;

import java.io.FileWriter;
import java.io.IOException;

public class CircleShape implements Shape {
    private int mRadius;
    private Point mCenter;

    public CircleShape(Point center, int radius) {
        mRadius = radius;
        mCenter = center;
    }

    public int getRadius() {
        return mRadius;
    }

    public Point getCenter() {
        return mCenter;
    }
}