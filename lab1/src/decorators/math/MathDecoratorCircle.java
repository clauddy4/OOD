package decorators.math;

import shapes.Shape;

public class MathDecoratorCircle extends MathDecorator {
    private double mRadius;

    public MathDecoratorCircle(Shape shape, double radius) {
        super(shape);
        mRadius = radius;
    }

    @Override
    public double getPerimeter() {
        return (double) (2 * Math.PI * mRadius);
    }

    @Override
    public double getArea() {
        return (double) (Math.PI * Math.pow(mRadius, 2));
    }
}