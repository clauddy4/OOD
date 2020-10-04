package shapes;

import canvas.Canvas;
import main.Point;

import java.io.FileWriter;
import java.io.IOException;

public class CircleShape extends Shape {
    private int mRadius;
    private Point mCenter;
    private int mFillColor = 100000;
    private int mOutlineColor = 100000;

    public CircleShape(Point center, int radius) {
        mRadius = radius;
        mCenter = center;
    }

    @Override
    public int getPerimeter() {
        return (int) (2 * Math.PI * mRadius);
    }

    @Override
    public int getArea() {
        return (int) (Math.PI * Math.pow(mRadius, 2));
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) {
        canvas.drawCircle(mCenter, mRadius, mOutlineColor);
        canvas.fillCircle(mCenter, mRadius, mFillColor);
    }

    @Override
    public void print(FileWriter out) throws IOException {
    }

    @Override
    public String getName() {
        return "Circle";
    }
}