package decorators.math;

import shapes.Shape;

public abstract class MathDecorator implements Shape {
    private Shape shape;

    MathDecorator(Shape shape) {
        this.shape = shape;
    }

    public abstract double getPerimeter();

    public abstract double getArea();
}