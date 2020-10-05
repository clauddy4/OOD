package decorators.print;

import main.Point;
import canvas.Canvas;
import shapes.Shape;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class PrintDecoratorTriangle extends PrintDecorator{
    private Point mVertex1;
    private Point mVertex2;
    private Point mVertex3;
    private int perimeter;
    private int area;

    public PrintDecoratorTriangle(Shape shape, Point vertex1, Point vertex2, Point vertex3, int perimeter, int area) {
        super(shape);
        mVertex1 = vertex1;
        mVertex2 = vertex2;
        mVertex3 = vertex3;
        this.perimeter = perimeter;
        this.area = area;
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) throws IOException {
        int mOutlineColor = 0;
        int mFillColor = 0;
        canvas.drawLine(mVertex1, mVertex2, mOutlineColor);
        canvas.drawLine(mVertex2, mVertex3, mOutlineColor);
        canvas.drawLine(mVertex3, mVertex1, mOutlineColor);
        canvas.fillPolygon(Arrays.asList(mVertex1, mVertex2, mVertex3), mFillColor);
    }

    @Override
    public void print(FileWriter out) throws IOException {
        String output = "Triangle: P=" + perimeter + "; S=" + area + "\n";
        out.write(output);
    }
}