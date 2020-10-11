package decorators.print;

import canvas.Canvas;
import shapes.Shape;

import java.io.FileWriter;
import java.io.IOException;

public abstract class PrintDecorator implements Shape {
    protected Shape shape;

    PrintDecorator(Shape shape) {
        this.shape = shape;
    }

    public abstract void draw(Canvas canvas, FileWriter out) throws IOException;

    public abstract void print(FileWriter out) throws IOException;
}