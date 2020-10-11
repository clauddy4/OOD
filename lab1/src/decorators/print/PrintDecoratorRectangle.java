package decorators.print;

import canvas.Canvas;
import shapes.Shape;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PrintDecoratorRectangle extends PrintDecorator {
    private Point topLeftVertex;
    private Point bottomRightVertex;
    private double perimeter;
    private double area;

    public PrintDecoratorRectangle(Shape shape, Point topLeftVertex, Point bottomRightVertex, double perimeter, double area) {
        super(shape);
        this.topLeftVertex = topLeftVertex;
        this.bottomRightVertex = bottomRightVertex;
        this.perimeter = perimeter;
        this.area = area;
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) {
        Point topRightVertex = new Point(bottomRightVertex.x, topLeftVertex.y);
        Point bottomLeftVertex = new Point(topLeftVertex.x, bottomRightVertex.y);
        List<Point> points = Arrays.asList(topLeftVertex, topRightVertex, bottomRightVertex, bottomLeftVertex);
        canvas.drawPolygon(points, Color.BLACK);
    }

    @Override
    public void print(FileWriter out) throws IOException {
        String output = "Rectangle: P=" + perimeter + "; S=" + area + "\n";
        out.write(output);
    }
}