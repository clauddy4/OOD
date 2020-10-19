package decorators.print;

import canvas.Canvas;
import shapes.Shape;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PrintDecoratorTriangle extends PrintDecorator {
    private Point vertex1;
    private Point vertex2;
    private Point vertex3;
    private double perimeter;
    private double area;
    private Color color = new Color(64, 122, 97);

    public PrintDecoratorTriangle(Shape shape, Point vertex1, Point vertex2, Point vertex3, double perimeter, double area) {
        super(shape);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.perimeter = perimeter;
        this.area = area;
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) {
        List<Point> points = Arrays.asList(vertex1, vertex2, vertex3);
        canvas.drawPolygon(points, color);
    }

    @Override
    public void print(FileWriter out) throws IOException {
        String output = "Triangle: P=" + perimeter + "; S=" + area + "\n";
        out.write(output);
    }
}