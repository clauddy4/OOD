package decorators.math;

import shapes.Shape;

public abstract class MathDecorator implements Shape {
    protected Shape shape;

    MathDecorator(Shape shape) {
        this.shape = shape;
    }

    public abstract double getPerimeter();

    public abstract double getArea();
}