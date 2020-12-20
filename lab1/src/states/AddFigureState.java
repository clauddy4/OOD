package states;

import canvas.Canvas;
import decorators.print.PrintDecorator;
import decorators.print.PrintDecoratorCircle;
import decorators.print.PrintDecoratorRectangle;
import decorators.print.PrintDecoratorTriangle;
import shapes.CircleShape;
import shapes.RectangleShape;
import shapes.Shape;
import shapes.TriangleShape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class AddFigureState implements State {
    public enum Type {
        CIRCLE,
        TRIANGLE,
        RECTANGLE
    }

    private Canvas canvas;

    private Type type;

    public AddFigureState(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void onClick(List<Shape> shapes, MouseEvent event) {
        switch (type) {
            case CIRCLE:
                addCircle(shapes);
                break;
            case TRIANGLE:
                addTriangle(shapes);
                break;
            case RECTANGLE:
                addRectangle(shapes);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private void addCircle(List<Shape> shapes) {
        Point center = new Point(0, 0);
        int radius = 50;
        CircleShape shape = new CircleShape(center, radius);
        PrintDecorator decorator = new PrintDecoratorCircle(shape, center, radius, 0, 0);
        shapes.add(decorator);
        try {
            decorator.draw(canvas);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        canvas.repaint();
    }

    private void addTriangle(List<Shape> shapes) {
        Point vertex1 = new Point(100, 0);
        Point vertex2 = new Point(0, 100);
        Point vertex3 = new Point(200, 100);
        TriangleShape shape = new TriangleShape(vertex1, vertex2, vertex3);
        PrintDecorator decorator = new PrintDecoratorTriangle(shape, vertex1, vertex2, vertex3, 0, 0);
        shapes.add(decorator);
        try {
            decorator.draw(canvas);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        canvas.repaint();
    }

    private void addRectangle(List<Shape> shapes) {
        Point topLeftVertex = new Point(0, 0);
        Point bottomRightVertex = new Point(100, 100);
        RectangleShape shape = new RectangleShape(topLeftVertex, bottomRightVertex);
        PrintDecorator decorator = new PrintDecoratorRectangle(shape, topLeftVertex, bottomRightVertex, 0, 0);
        shapes.add(decorator);
        try {
            decorator.draw(canvas);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        canvas.repaint();
    }
}
