package shapes;

import canvas.Canvas;
import main.Point;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class TriangleShape extends Shape {
//    public Point mTop1;
//    public Point mTop2;
//    public Point mTop3;
//    private int mFillColor = 444444;
//    private int mOutlineColor = 444444;
//
//    public TriangleShape(Point top1, Point top2, Point top3) {
//        mTop1 = top1;
//        mTop2 = top2;
//        mTop3 = top3;
//    }
//
//    private List<Double> getEdges() {
//        ArrayList<Double> set = new ArrayList<>();
//        double edge1 = Math.sqrt(Math.pow(mTop2.x - mTop1.x, 2) + Math.pow(mTop2.y - mTop1.y, 2));
//        double edge2 = Math.sqrt(Math.pow(mTop3.x - mTop2.x, 2) + Math.pow(mTop3.y - mTop2.y, 2));
//        double edge3 = Math.sqrt(Math.pow(mTop1.x - mTop3.x, 2) + Math.pow(mTop1.y - mTop3.y, 2));
//        set.add(edge1);
//        set.add(edge2);
//        set.add(edge3);
//        return set;
//    }
//
//    @Override
//    public int getPerimeter() {
//        List<Double> edges = getEdges();
//        return (int) (edges.get(0) + edges.get(1) + edges.get(2));
//    }
//
//    @Override
//    public int getArea() {
//        List<Double> edges = getEdges();
//        return (int) Math.sqrt(getPerimeter()
//                * (getPerimeter() - edges.get(0))
//                * (getPerimeter() - edges.get(1))
//                * (getPerimeter() - edges.get(2)));
//    }
//
//    @Override
//    public void draw(Canvas canvas, FileWriter out) {
//        canvas.drawLine(mTop1, mTop2, mOutlineColor);
//        canvas.drawLine(mTop2, mTop3, mOutlineColor);
//        canvas.drawLine(mTop3, mTop1, mOutlineColor);
//        canvas.fillPolygon(Arrays.asList(mTop1, mTop2, mTop3), mFillColor);
//    }
//
//    @Override
//    public void print(FileWriter out) throws IOException {
//    }
}