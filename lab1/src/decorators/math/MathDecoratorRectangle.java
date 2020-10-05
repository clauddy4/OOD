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
    public int getPerimeter() {
        return (int) (2 * (mBottomRightVertex.x - mTopLeftVertex.x + mBottomRightVertex.y - mTopLeftVertex.y));
    }

    @Override
    public int getArea() {
        return (int) ((mBottomRightVertex.x - mTopLeftVertex.x) * (mBottomRightVertex.y - mTopLeftVertex.y));
    }
}