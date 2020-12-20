package shapes;

import canvas.Canvas;

import java.awt.*;

public class CircleShape implements Shape {
    private Color color = Color.BLACK;
    private int radius;
    private Point center;

    public CircleShape(Point center, int radius) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Draw is not defined");
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Rectangle getBounds() {
        int x = center.x - radius;
        int y = center.y - radius;
        int side = 2 * radius;
        return new Rectangle(x, y, side, side);
    }
}