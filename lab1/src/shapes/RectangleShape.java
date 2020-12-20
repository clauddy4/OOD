package shapes;

import canvas.Canvas;

import java.awt.*;

public class RectangleShape implements Shape {
    private Color color = Color.BLACK;
    private Point topLeft;
    private Point bottomRight;

    public RectangleShape(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Draw is not defined");
    }

    private Point getTopLeft() {
        return topLeft;
    }

    private Point getBottomRight() {
        return bottomRight;
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
        int x = getTopLeft().x;
        int y = getTopLeft().y;
        int width = getBottomRight().x - getTopLeft().x;
        int height = getBottomRight().y - getTopLeft().y;
        return new Rectangle(x, y, width, height);
    }
}