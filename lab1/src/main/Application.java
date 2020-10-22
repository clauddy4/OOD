package main;

import canvas.Canvas;
import canvas.J2DCanvas;
import decorators.math.MathDecorator;
import decorators.math.MathDecoratorCircle;
import decorators.math.MathDecoratorRectangle;
import decorators.math.MathDecoratorTriangle;
import decorators.print.PrintDecorator;
import decorators.print.PrintDecoratorCircle;
import decorators.print.PrintDecoratorRectangle;
import decorators.print.PrintDecoratorTriangle;
import shapes.CircleShape;
import shapes.RectangleShape;
import shapes.TriangleShape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

class Application {
    private static final int FRAME_WIDTH = 1280;
    private static final int FRAME_HEIGHT = 720;
    private static final String FRAME_TITLE = "OOD";
    private static Application instance;

    private Application() {
    }

    static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    void init(String input, String output) throws IOException {
        ArrayList<PrintDecorator> shapes = new ArrayList<>();
        parseInput(input, shapes);
        writeOutput(output, shapes);
    }

    private void writeOutput(String output, ArrayList<PrintDecorator> shapes) throws IOException {
        try (FileWriter out = new FileWriter(output)) {
            startUI(shapes, out);
            printResult(shapes, out);
        }
    }

    private void parseInput(String input, ArrayList<PrintDecorator> shapes) throws IOException {
        try (FileReader in = new FileReader(input)) {
            Scanner scanner = new Scanner(in);
            while (scanner.hasNext()) {
                int circleRadius = 0;
                ArrayList<Point> points = new ArrayList<>();
                String shapeType = getShapeType(scanner);
                scanner.skip(":");
                try (Scanner rest = new Scanner(scanner.useDelimiter("\n").next())) {
                    while (rest.hasNext()) {
                        String pointToken = rest.useDelimiter("=").next();
                        if (pointToken.contains("P") || pointToken.contains("C")) {
                            getPoint(points, rest);
                        } else if (pointToken.contains("R")) {
                            circleRadius = getCircleRadius(rest);
                        }
                    }
                }
                addShape(shapes, circleRadius, points, shapeType);
            }
        }
    }

    private void addShape(ArrayList<PrintDecorator> shapes, int circleRadius, ArrayList<Point> points, String shapeType) {
        switch (shapeType) {
            case "TRIANGLE":
                addTriangleShape(shapes, points);
                break;
            case "RECTANGLE":
                addRectangleShape(shapes, points);
                break;
            case "CIRCLE":
                addCircleShape(shapes, circleRadius, points);
                break;
            default:
                break;
        }
    }

    private void addCircleShape(ArrayList<PrintDecorator> shapes, int circleRadius, ArrayList<Point> points) {
        CircleShape circleShape = new CircleShape(points.get(0), circleRadius);
        MathDecorator circle = new MathDecoratorCircle(circleShape, circleRadius);
        PrintDecorator circleForResult = new PrintDecoratorCircle(
                circleShape,
                points.get(0),
                circleRadius,
                circle.getPerimeter(),
                circle.getArea());
        shapes.add(circleForResult);
    }

    private void addRectangleShape(ArrayList<PrintDecorator> shapes, ArrayList<Point> points) {
        RectangleShape rectangleShape = new RectangleShape(points.get(0), points.get(1));
        MathDecorator rectangle = new MathDecoratorRectangle(rectangleShape, points.get(0), points.get(1));
        PrintDecorator rectangleForResult = new PrintDecoratorRectangle(
                rectangleShape,
                points.get(0),
                points.get(1),
                rectangle.getPerimeter(),
                rectangle.getArea());
        shapes.add(rectangleForResult);
    }

    private void addTriangleShape(ArrayList<PrintDecorator> shapes, ArrayList<Point> points) {
        TriangleShape triangleShape = new TriangleShape(points.get(0), points.get(1), points.get(2));
        MathDecorator triangle = new MathDecoratorTriangle(triangleShape, points.get(0), points.get(1), points.get(2));
        PrintDecorator triangleForResult = new PrintDecoratorTriangle(
                triangleShape,
                points.get(0),
                points.get(1),
                points.get(2),
                triangle.getPerimeter(),
                triangle.getArea());
        shapes.add(triangleForResult);
    }

    private String getShapeType(Scanner scanner) {
        String shapeType = scanner.useDelimiter(":").next();
        shapeType = shapeType.replace("\n", "");
        return shapeType;
    }

    private void getPoint(ArrayList<Point> points, Scanner scanner) {
        scanner.skip("=");
        String stringCoords = scanner.useDelimiter(";").next();
        scanner.skip(";");
        String[] splitterCoords = stringCoords.split(",");
        int x = Integer.parseInt(splitterCoords[0]);
        int y = Integer.parseInt(splitterCoords[1]);
        Point point = new Point(x, y);
        points.add(point);
    }

    private int getCircleRadius(Scanner scanner) {
        scanner.skip("=");
        String stringRadius = scanner.useDelimiter(";").next();
        scanner.skip(";");
        return Integer.parseInt(stringRadius);
    }

    private void startUI(ArrayList<PrintDecorator> shapes, FileWriter out) {
        SwingUtilities.invokeLater(() -> {
            try {
                J2DCanvas canvas = new J2DCanvas();
                initUI(canvas);
                draw(shapes, canvas, out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initUI(J2DCanvas panel) throws IOException {
        JFrame frame = new JFrame();
        frame.setTitle(FRAME_TITLE);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
        panel.requestFocus();

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        menuBar.setBounds(0, 0, 350, 40);

        initMenu(menuBar);
    }

    private void initMenu(JMenuBar menuBar) throws IOException {
        initFiguresMenu(menuBar);
        initColorMenu(menuBar, "Color");
        initThicknessMenu(menuBar);
        initColorMenu(menuBar, "Fill");
        initModeMenu(menuBar);
    }

    private void initModeMenu(JMenuBar menuBar) {
        JMenu menu = new JMenu("Mode");
        JMenuItem dragAndDrop = new JMenuItem("Drag And Drop");
        JMenuItem fillFigure = new JMenuItem("Fill");
        menu.add(dragAndDrop);
        menu.add(fillFigure);
        menuBar.add(menu);
    }

    private void initThicknessMenu(JMenuBar menuBar) {
        JMenu menu = new JMenu("Thickness");
        JMenuItem thin = new JMenuItem("thin");
        JMenuItem average = new JMenuItem("average");
        JMenuItem thick = new JMenuItem("thick");
        menu.add(thin);
        menu.add(average);
        menu.add(thick);
        menuBar.add(menu);
    }

    private void initColorMenu(JMenuBar menuBar, String color) {
        JMenu menu = new JMenu(color);
        JMenuItem red = new JMenuItem("red");
        JMenuItem blue = new JMenuItem("blue");
        JMenuItem yellow = new JMenuItem("yellow");
        JMenuItem green = new JMenuItem("green");
        JMenuItem black = new JMenuItem("black");
        menu.add(red);
        menu.add(blue);
        menu.add(yellow);
        menu.add(green);
        menu.add(black);
        menuBar.add(menu);
    }

    private void initFiguresMenu(JMenuBar menuBar) throws IOException {
        JMenu menu = new JMenu("Add figure");
        JMenuItem circle = new JMenuItem("Circle", new ImageIcon(getImageResource("./icons/circle.png")));
        JMenuItem triangle = new JMenuItem("Triangle", new ImageIcon(getImageResource("./icons/triangle.png")));
        JMenuItem rectangle = new JMenuItem("Rectangle", new ImageIcon(getImageResource("./icons/rectangle.png")));
        menu.add(circle);
        menu.add(triangle);
        menu.add(rectangle);
        menuBar.add(menu);
    }

    private BufferedImage getImageResource(String resource) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream(resource));
    }

    private void draw(List<PrintDecorator> shapes, Canvas canvas, FileWriter out) {
        for (PrintDecorator shape : shapes) {
            try {
                shape.draw(canvas, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printResult(ArrayList<PrintDecorator> shapes, FileWriter out) {
        for (PrintDecorator shape : shapes) {
            try {
                shape.print(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}