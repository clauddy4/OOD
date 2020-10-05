package decorators.math;

import main.java.Point;
import main.java.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public class MathDecoratorTriangle extends MathDecorator{
    private Point mVertex1;
    private Point mVertex2;
    private Point mVertex3;

    public MathDecoratorTriangle(Shape shape, Point vertex1, Point vertex2, Point vertex3) {
        super(shape);
        mVertex1 = vertex1;
        mVertex2 = vertex2;
        mVertex3 = vertex3;
    }

    private List<Double> getEdges() {
        ArrayList<Double> set = new ArrayList<>();
        double edge1 = Math.sqrt(Math.pow(mVertex2.x - mVertex1.x, 2) + Math.pow(mVertex2.y - mVertex1.y, 2));
        double edge2 = Math.sqrt(Math.pow(mVertex3.x - mVertex2.x, 2) + Math.pow(mVertex3.y - mVertex2.y, 2));
        double edge3 = Math.sqrt(Math.pow(mVertex1.x - mVertex3.x, 2) + Math.pow(mVertex1.y - mVertex3.y, 2));
        set.add(edge1);
        set.add(edge2);
        set.add(edge3);
        return set;
    }

    @Override
    public int getPerimeter() {
        List<Double> edges = getEdges();
        return (int) (edges.get(0) + edges.get(1) + edges.get(2));
    }

    @Override
    public int getArea() {
        List<Double> edges = getEdges();
        return (int) Math.sqrt(getPerimeter()
                * (getPerimeter() - edges.get(0))
                * (getPerimeter() - edges.get(1))
                * (getPerimeter() - edges.get(2)));
    }
}