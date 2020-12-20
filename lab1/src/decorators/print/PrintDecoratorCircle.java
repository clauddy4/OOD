package decorators.print;

import canvas.Canvas;
import shapes.Shape;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class PrintDecoratorCircle extends PrintDecorator {
    private double radius;
    private Point center;
    private double perimeter;
    private double area;

    public PrintDecoratorCircle(Shape shape, Point center, double radius, double perimeter, double area) {
        super(shape);
        this.radius = radius;
        this.center = center;
        this.perimeter = perimeter;
        this.area = area;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center, radius, shape.getColor());
    }

    @Override
    public void print(FileWriter out) throws IOException {
        String output = "Circle: P=" + perimeter + "; S=" + area + "\n";
        out.write(output);
    }
}