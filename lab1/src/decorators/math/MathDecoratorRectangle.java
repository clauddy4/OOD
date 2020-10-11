package decorators.math;

import main.Point;
import shapes.Shape;

public class MathDecoratorRectangle extends MathDecorator {
    private Point topLeftVertex;
    private Point bottomRightVertex;

    public MathDecoratorRectangle(Shape shape, Point topLeftVertex, Point bottomRightVertex) {
        super(shape);
        this.topLeftVertex = topLeftVertex;
        this.bottomRightVertex = bottomRightVertex;
    }

    @Override
    public double getPerimeter() {
        return (double) (2 * (bottomRightVertex.x - topLeftVertex.x + bottomRightVertex.y - topLeftVertex.y));
    }

    @Override
    public double getArea() {
        return (double) ((bottomRightVertex.x - topLeftVertex.x) * (bottomRightVertex.y - topLeftVertex.y));
    }
}