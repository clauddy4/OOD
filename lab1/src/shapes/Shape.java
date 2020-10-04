package shapes;

import canvas.Canvas;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Shape {
    public abstract int getPerimeter();

    public abstract int getArea();

    public abstract void draw(Canvas canvas, FileWriter out) throws IOException;

    public abstract void print(FileWriter out) throws IOException;

    public abstract String getName();
}
