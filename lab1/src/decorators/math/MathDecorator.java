package decorators.math;

import shapes.Shape;
import canvas.Canvas;

import java.awt.*;
import java.io.IOException;

public abstract class MathDecorator implements Shape {
    protected Shape shape;

        MathDecorator(Shape shape) {
            this.shape = shape;
    }

    @Override
    public void draw(Canvas canvas) throws IOException {
        shape.draw(canvas);
    }

    @Override
    public void setColor(Color color) {
        shape.setColor(color);
    }

    @Override
    public Color getColor() {
        return shape.getColor();
    }

    @Override
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    public abstract double getPerimeter();

    public abstract double getArea();
}