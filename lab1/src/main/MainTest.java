package main;

import shapes.CircleShape;
import shapes.RectangleShape;
import shapes.TriangleShape;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private CircleShape circleShape = new CircleShape(new Point(200, 200), 30);
    private TriangleShape triangleShape = new TriangleShape(new Point(200, 200), new Point(100, 200), new Point(200, 300));
    private RectangleShape rectangleShape = new RectangleShape(new Point(200, 200), new Point(400, 400));

    @Test
    void getCirclePerimeter() {
        int perimeter = circleShape.getPerimeter();
        assertEquals(188, perimeter);
    }

    @Test
    void getCircleArea() {
        int area = circleShape.getArea();
        assertEquals(2827, area);
    }

    @Test
    void getTrianglePerimeter() {
        int perimeter = triangleShape.getPerimeter();
        assertEquals(341, perimeter);
    }

    @Test
    void getTriangleArea() {
        int area = triangleShape.getArea();
        assertEquals(62871, area);
    }

    @Test
    void getRectanglePerimeter() {
        int perimeter = rectangleShape.getPerimeter();
        assertEquals(800, perimeter);
    }

    @Test
    void getRectangleArea() {
        int area = rectangleShape.getArea();
        assertEquals(40000, area);
    }
}