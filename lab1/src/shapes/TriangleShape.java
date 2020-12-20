package shapes;

import canvas.Canvas;

import java.awt.*;

public class TriangleShape implements Shape {
    private Color color = Color.BLACK;
    private Point vertex1;
    private Point vertex2;
    private Point vertex3;

    public TriangleShape(Point vertex1, Point vertex2, Point vertex3) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    @Override
    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Draw is not defined");
    }

    private Point getVertex1() {
        return vertex1;
    }

    private Point getVertex2() {
        return vertex2;
    }

    private Point getVertex3() {
        return vertex3;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Rectangle getBounds() {
        int x = getVertex2().x;
        int y = getVertex1().y;
        int width = getVertex3().x - getVertex2().x;
        int height = getVertex3().y - getVertex1().y;
        return new Rectangle(x, y, width, height);
    }
}