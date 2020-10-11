package decorators.math;

import main.Point;
import shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public class MathDecoratorTriangle extends MathDecorator{
    private Point vertex1;
    private Point vertex2;
    private Point vertex3;

    public MathDecoratorTriangle(Shape shape, Point vertex1, Point vertex2, Point vertex3) {
        super(shape);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    private List<Double> getEdges() {
        ArrayList<Double> set = new ArrayList<>();
        double edge1 = Math.sqrt(Math.pow(vertex2.x - vertex1.x, 2) + Math.pow(vertex2.y - vertex1.y, 2));
        double edge2 = Math.sqrt(Math.pow(vertex3.x - vertex2.x, 2) + Math.pow(vertex3.y - vertex2.y, 2));
        double edge3 = Math.sqrt(Math.pow(vertex1.x - vertex3.x, 2) + Math.pow(vertex1.y - vertex3.y, 2));
        set.add(edge1);
        set.add(edge2);
        set.add(edge3);
        return set;
    }

    @Override
    public double getPerimeter() {
        List<Double> edges = getEdges();
        return (double) (edges.get(0) + edges.get(1) + edges.get(2));
    }

    @Override
    public double getArea() {
        List<Double> edges = getEdges();
        return (double) Math.sqrt(getPerimeter()
                * (getPerimeter() - edges.get(0))
                * (getPerimeter() - edges.get(1))
                * (getPerimeter() - edges.get(2)));
    }
}