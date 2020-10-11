package decorators.print;

import main.Point;
import canvas.Canvas;
import shapes.Shape;

import java.io.FileWriter;
import java.io.IOException;

public class PrintDecoratorCircle extends PrintDecorator {
    private double mRadius;
    private Point mCenter;
    private double perimeter;
    private double area;

    public PrintDecoratorCircle(Shape shape, Point center, double radius, double perimeter, double area) {
        super(shape);
        mRadius = radius;
        mCenter = center;
        this.perimeter = perimeter;
        this.area = area;
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) throws IOException {
        int mOutlineColor = 0;
        int mFillColor = 0;
        canvas.drawCircle(mCenter, mRadius, mOutlineColor);
        canvas.fillCircle(mCenter, mRadius, mFillColor);
    }

    @Override
    public void print(FileWriter out) throws IOException {
        String output = "Circle: P=" + perimeter + "; S=" + area + "\n";
        out.write(output);
    }
}