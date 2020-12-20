package decorators.print;

import shapes.Shape;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public abstract class PrintDecorator implements Shape {
    protected Shape shape;

    PrintDecorator(Shape shape) {
        this.shape = shape;
    }

    public abstract void print(FileWriter out) throws IOException;

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
}