package decorators.print;

import main.Point;
import canvas.Canvas;
import shapes.Shape;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class PrintDecoratorRectangle extends PrintDecorator {
    private Point mTopLeftVertex;
    private Point mBottomRightVertex;
    private int perimeter;
    private int area;

    public PrintDecoratorRectangle(Shape shape, Point topLeftVertex, Point bottomRightVertex, int perimeter, int area) {
        super(shape);
        mTopLeftVertex = topLeftVertex;
        mBottomRightVertex = bottomRightVertex;
        this.perimeter = perimeter;
        this.area = area;
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) throws IOException {
        Point rightTop = new Point(mBottomRightVertex.x, mTopLeftVertex.y);
        Point leftBottom = new Point(mTopLeftVertex.x, mBottomRightVertex.y);
        int mOutlineColor = 0;
        int mFillColor = 0;
        canvas.drawLine(mTopLeftVertex, rightTop, mOutlineColor);
        canvas.drawLine(rightTop, mBottomRightVertex, mOutlineColor);
        canvas.drawLine(mBottomRightVertex, leftBottom, mOutlineColor);
        canvas.drawLine(leftBottom, mTopLeftVertex, mOutlineColor);
        canvas.fillPolygon(Arrays.asList(mTopLeftVertex, rightTop, mBottomRightVertex, leftBottom), mFillColor);
    }

    @Override
    public void print(FileWriter out) throws IOException {
        String output = "Rectangle: P=" + perimeter + "; S=" + area + "\n";
        out.write(output);
    }
}