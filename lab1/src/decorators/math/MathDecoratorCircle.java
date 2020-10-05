package decorators.math;

import shapes.Shape;

public class MathDecoratorCircle extends MathDecorator {
    private int mRadius;

    public MathDecoratorCircle(Shape shape, int radius) {
        super(shape);
        mRadius = radius;
    }

    @Override
    public int getPerimeter() {
        return (int) (2 * Math.PI * mRadius);
    }

    @Override
    public int getArea() {
        return (int) (Math.PI * Math.pow(mRadius, 2));
    }
}