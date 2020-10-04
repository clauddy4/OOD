package shapes;

import canvas.Canvas;
import main.Point;

import java.io.FileWriter;
import java.util.Arrays;
import java.io.IOException;

public class RectangleShape extends Shape {
    private Point mTopLeftTop;
    private Point mBottomRightTop;
    private int mFillColor = 100;
    private int mOutlineColor = 100;


    public RectangleShape(Point topLeftTop, Point bottomRightTop) {
        mTopLeftTop = topLeftTop;
        mBottomRightTop = bottomRightTop;
    }

    @Override
    public int getPerimeter() {
        return (int) (2 * (mBottomRightTop.x - mTopLeftTop.x + mBottomRightTop.y - mTopLeftTop.y));
    }

    @Override
    public int getArea() {
        return (int) ((mBottomRightTop.x - mTopLeftTop.x) * (mBottomRightTop.y - mTopLeftTop.y));
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) {
        Point rightTop = new Point(mBottomRightTop.x, mTopLeftTop.y);
        Point leftBottom = new Point(mTopLeftTop.x, mBottomRightTop.y);
        canvas.drawLine(mTopLeftTop, rightTop, mOutlineColor);
        canvas.drawLine(rightTop, mBottomRightTop, mOutlineColor);
        canvas.drawLine(mBottomRightTop, leftBottom, mOutlineColor);
        canvas.drawLine(leftBottom, mTopLeftTop, mOutlineColor);
        canvas.fillPolygon(Arrays.asList(mTopLeftTop, rightTop, mBottomRightTop, leftBottom), mFillColor);
    }

    @Override
    public void print(FileWriter out) throws IOException {
    }

    @Override
    public String getName() {
        return "Rectangle";
    }
}