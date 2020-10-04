package main;

import canvas.Canvas;
import shapes.Shape;

import java.io.FileWriter;
import java.io.IOException;

class PrintParameter extends Shape {
    private Shape mShape;

    PrintParameter(Shape shape) {
        mShape = shape;
    }

    @Override
    public int getPerimeter() {
        return mShape.getPerimeter();
    }

    @Override
    public int getArea() {
        return mShape.getArea();
    }

    @Override
    public void draw(Canvas canvas, FileWriter out) throws IOException {
        mShape.draw(canvas, out);
    }
    @Override
    public void print(FileWriter out) throws IOException {
        String output = getName() + ": P=" + getPerimeter() + "; S=" + getArea() + "\n";
        out.write(output);
    }

    @Override
    public String getName() {
        return mShape.getName();
    }
}