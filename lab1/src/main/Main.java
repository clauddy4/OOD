package main;

import shapes.Shape;
import canvas.Canvas;
import canvas.CanvasPanel;
import canvas.J2DCanvas;
import shapes.CircleShape;
import shapes.RectangleShape;
import shapes.TriangleShape;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Main {
    private static final int FRAME_WIDTH = 1080;
    private static final int FRAME_HEIGHT = 720;
    private static final String FRAME_TITLE = "OOD";

    public static void main(String[] args) throws IOException {
        ArrayList<shapes.Shape> shapes = new ArrayList<>();
        final FileReader in = new FileReader(args[0]);
        final FileWriter out = new FileWriter(args[1]);
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNext()) {
                ArrayList<Point> points = new ArrayList<>();
                int circleRadius = 0;
                String shapeType = getShapeType(scanner);

                scanner.skip(":");

                String restOfLine = scanner.useDelimiter("\n").next();
                try (Scanner restOfLineScanner = new Scanner(restOfLine)) {

                    while (restOfLineScanner.hasNext()) {
                        String pointToken = restOfLineScanner.useDelimiter("=").next();
                        // TRIANGLE/RECTANGLE point
                        if (pointToken.contains("P") || pointToken.contains("C")) {
                            getPoint(points, restOfLineScanner);
                        // CIRCLE POINT
                        } else if (pointToken.contains("R")) {
                            circleRadius = getCircleRadius(circleRadius, restOfLineScanner);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                addingNewShape(shapes, shapeType, points, circleRadius);
                in.close();
            }
            startUI(shapes, out);
            printResult(shapes, out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getShapeType(Scanner scanner) {
        String shapeType = scanner.useDelimiter(":").next();
        shapeType = shapeType.replace("\n", "");
        return shapeType;
    }

    private static void getPoint(ArrayList<Point> points, Scanner restOfLineScanner) {
        restOfLineScanner.skip("=");

        String stringCoords = restOfLineScanner.useDelimiter(";").next();
        restOfLineScanner.skip(";");

        String[] splitterCoords = stringCoords.split(",");

        int x = Integer.parseInt(splitterCoords[0]);
        int y = Integer.parseInt(splitterCoords[1]);
        Point point = new Point(x, y);
        points.add(point);
    }

    private static int getCircleRadius(int circleRadius, Scanner restOfLineScanner) {
        restOfLineScanner.skip("=");

        String stringRadius = restOfLineScanner.useDelimiter(";").next();
        restOfLineScanner.skip(";");

        int radius = Integer.parseInt(stringRadius);
        circleRadius = radius;
        return circleRadius;
    }

    private static void addingNewShape(ArrayList<Shape> shapes, String shapeType, ArrayList<Point> points, int circleRadius) {
        switch (shapeType) {
            case "TRIANGLE":
                Shape triangle = new TriangleShape(points.get(0), points.get(1), points.get(2));
                triangle = new PrintParameter(triangle);
                shapes.add(triangle);
                break;
            case "RECTANGLE":
                Shape rectangle = new RectangleShape(points.get(0), points.get(1));
                rectangle = new PrintParameter(rectangle);
                shapes.add(rectangle);
                break;
            case "CIRCLE":
                Shape circle = new CircleShape(points.get(0), circleRadius);
                circle = new PrintParameter(circle);
                shapes.add(circle);
                break;
            default:
                break;
        }
    }

    private static void startUI(ArrayList<Shape> shapes, FileWriter out) {
        EventQueue.invokeLater(() -> {
            try {
                J2DCanvas canvas = new J2DCanvas();
                CanvasPanel panel = new CanvasPanel(canvas);
                initUI(panel);
                draw(shapes, canvas, out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void initUI(CanvasPanel panel) {
        JFrame frame = new JFrame();
        frame.setTitle(FRAME_TITLE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
    }

    private static void draw(List<shapes.Shape> shapes, Canvas canvas, FileWriter out) {
        shapes.forEach(shape -> {
            try {
                shape.draw(canvas, out);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private static void printResult(ArrayList<Shape> shapes, FileWriter out) {
        shapes.forEach(shape -> {
            try {
                shape.print(out);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}