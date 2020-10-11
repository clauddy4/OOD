package shapes;

import canvas.Canvas;
import main.Point;

import java.io.FileWriter;
import java.io.IOException;

public class CircleShape implements Shape {
    private double mRadius;
    private Point mCenter;

    public CircleShape(Point center, double radius) {
        mRadius = radius;
        mCenter = center;
    }

    public double getRadius() {
        return mRadius;
    }

    public Point getCenter() {
        return mCenter;
    }
}