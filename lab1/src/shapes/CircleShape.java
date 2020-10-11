package shapes;

import canvas.Canvas;
import main.Point;

import java.io.FileWriter;
import java.io.IOException;

public class CircleShape implements Shape {
    private double radius;
    private Point center;

    public CircleShape(Point center, double radius) {
        this.radius = radius;
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public Point getCenter() {
        return center;
    }
}