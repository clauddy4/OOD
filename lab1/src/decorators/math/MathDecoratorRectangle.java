package decorators.math;

import main.Point;
import shapes.Shape;

public class MathDecoratorRectangle extends MathDecorator {
    private Point mTopLeftVertex;
    private Point mBottomRightVertex;

    public MathDecoratorRectangle(Shape shape, Point topLeftVertex, Point bottomRightVertex) {
        super(shape);
        mTopLeftVertex = topLeftVertex;
        mBottomRightVertex = bottomRightVertex;
    }

    @Override
    public double getPerimeter() {
        return (double) (2 * (mBottomRightVertex.x - mTopLeftVertex.x + mBottomRightVertex.y - mTopLeftVertex.y));
    }

    @Override
    public double getArea() {
        return (double) ((mBottomRightVertex.x - mTopLeftVertex.x) * (mBottomRightVertex.y - mTopLeftVertex.y));
    }
}