package decorators.math;

import shapes.Shape;

public class MathDecoratorCircle extends MathDecorator {
    private double radius;

    public MathDecoratorCircle(Shape shape, double radius) {
        super(shape);
        this.radius = radius;
    }

    @Override
    public double getPerimeter() {
        return (double) (2 * Math.PI * radius);
    }

    @Override
    public double getArea() {
        return (double) (Math.PI * Math.pow(radius, 2));
    }
}