package shapes;

import main.Point;

public class TriangleShape implements Shape {
    private Point mVertex1;
    private Point mVertex2;
    private Point mVertex3;

    public TriangleShape(Point vertex1, Point vertex2, Point vertex3) {
        mVertex1 = vertex1;
        mVertex2 = vertex2;
        mVertex3 = vertex3;
    }

    public Point getVertex1() {
        return mVertex1;
    }

    public Point getVertex2() {
        return mVertex2;
    }

    public Point getVertex3() {
        return mVertex3;
    }
}