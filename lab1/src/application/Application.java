package application;

import canvas.Canvas;
import canvas.CanvasShape;
import canvas.J2DCanvas;
import command.Command;
import command.CommandHistory;
import command.SetStrokeColorCommand;
import command.SetThicknessCommand;
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
import shapes.Shape;
import shapes.TriangleShape;
import states.AddFigureState;
import states.FillState;
import states.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Application {
    private static final int FRAME_WIDTH = 1280;
    private static final int FRAME_HEIGHT = 720;
    private static final String FRAME_TITLE = "DashaTheArtist";

    private static Application instance;

    private J2DCanvas canvas = new J2DCanvas();
    private List<Shape> shapes = new ArrayList<>();


    private AddFigureState addFigureState = new AddFigureState(canvas);
    private FillState fillState = new FillState(canvas);
    private State state;

    private CommandHistory history = new CommandHistory();

    private Application() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (state != null) {
                    state.onClick(event);
                    state.onClick(shapes, event);
                }
            }
        });
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public void init(String input, String output) throws IOException {
        parseInput(input, shapes);
        writeOutput(output, shapes);
    }

    private void writeOutput(String output, List<Shape> shapes) throws IOException {
        try (FileWriter out = new FileWriter(output)) {
            startUI(shapes, out);
            printResult(shapes, out);
        }
    }

    private void parseInput(String input, List<Shape> shapes) throws IOException {
        try (FileReader in = new FileReader(input)) {
            Scanner scanner = new Scanner(in);
            while (scanner.hasNext()) {
                int circleRadius = 0;
                List<Point> points = new ArrayList<>();
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

    private void addShape(List<Shape> shapes, int circleRadius, List<Point> points, String shapeType) {
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

    private void addCircleShape(List<Shape> shapes, int circleRadius, List<Point> points) {
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

    private void addRectangleShape(List<Shape> shapes, List<Point> points) {
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

    private void addTriangleShape(List<Shape> shapes, List<Point> points) {
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

    private void getPoint(List<Point> points, Scanner scanner) {
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

    private void startUI(List<Shape> shapes, FileWriter out) {
        SwingUtilities.invokeLater(() -> {
            try {
                initUI(canvas);
                draw(shapes, canvas);
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

        initMenu(menuBar);
    }

    private void initMenu(JMenuBar menuBar) throws IOException {
        initFiguresMenu(menuBar);
        initStrokeColorMenu(menuBar);
        initThicknessMenu(menuBar);
        initFillColorMenu(menuBar);
        initSelectionButton(menuBar);
        initUndoRedo(menuBar);
    }

    private void initUndoRedo(JMenuBar menuBar) {
        var undo = new JButton("Undo");
        var redo = new JButton("Redo");
        try {
            undo.setIcon(new ImageIcon(getImageResource("icons/undo.png")));
            redo.setIcon(new ImageIcon(getImageResource("icons/redo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        undo.addActionListener(e -> {
            if (history.canUndo()) {
                var top = history.undoTop();
                top.undo();
                canvas.requestFocus();
                canvas.repaint();
            }
        });
        redo.addActionListener(e -> {
            if (history.canRedo()) {
                var top = history.redoTop();
                top.redo();
                canvas.requestFocus();
                canvas.repaint();
            }
        });
        menuBar.add(undo);
        menuBar.add(redo);
    }

    private void initStrokeColorMenu(JMenuBar menuBar) {
        JMenu menu = new JMenu("Stroke Color");
        JMenuItem red = new JMenuItem("Red");
        JMenuItem blue = new JMenuItem("Blue");
        JMenuItem yellow = new JMenuItem("Yellow");
        JMenuItem green = new JMenuItem("Green");
        JMenuItem black = new JMenuItem("Black");
        menu.add(red);
        menu.add(blue);
        menu.add(yellow);
        menu.add(green);
        menu.add(black);
        menuBar.add(menu);
        red.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetStrokeColorCommand(shape, Color.RED);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
        blue.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetStrokeColorCommand(shape, Color.BLUE);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
        yellow.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetStrokeColorCommand(shape, Color.YELLOW);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
        green.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetStrokeColorCommand(shape, Color.GREEN);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
        black.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetStrokeColorCommand(shape, Color.BLACK);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
    }

    private void initSelectionButton(JMenuBar menuBar) {
        JButton button = new JButton("Selection");
        button.addActionListener(e -> {
            state = null;
            canvas.requestFocus();
        });
        button.setMargin(new Insets(2, 2, 2, 2));
        try {
            button.setIcon(new ImageIcon(getImageResource("icons/cursor.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        menuBar.add(Box.createGlue());
        menuBar.add(button);
    }

    private void initThicknessMenu(JMenuBar menuBar) {
        JMenu menu = new JMenu("Thickness");
        JMenuItem thin = new JMenuItem("Thin");
        JMenuItem average = new JMenuItem("Average");
        JMenuItem thick = new JMenuItem("Thick");
        menu.add(thin);
        menu.add(average);
        menu.add(thick);
        menuBar.add(menu);
        thin.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetThicknessCommand(shape, 1);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
        average.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetThicknessCommand(shape, 3);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
        thick.addActionListener(e -> {
            for (CanvasShape shape : canvas.getCanvasShapes()) {
                if (shape.isSelected()) {
                    var command = new SetThicknessCommand(shape, 5);
                    executeCommand(command);
                }
            }
            canvas.repaint();
        });
    }

    private void initFillColorMenu(JMenuBar menuBar) {
        JMenu menu = new JMenu("Fill Color");
        JMenuItem red = new JMenuItem("Red");
        JMenuItem blue = new JMenuItem("Blue");
        JMenuItem yellow = new JMenuItem("Yellow");
        JMenuItem green = new JMenuItem("Green");
        JMenuItem black = new JMenuItem("Black");
        menu.add(red);
        menu.add(blue);
        menu.add(yellow);
        menu.add(green);
        menu.add(black);
        menuBar.add(menu);
        red.addActionListener(e -> {
            fillState.setColor(Color.RED);
            state = fillState;
        });
        blue.addActionListener(e -> {
            fillState.setColor(Color.BLUE);
            state = fillState;
        });
        yellow.addActionListener(e -> {
            fillState.setColor(Color.YELLOW);
            state = fillState;
        });
        green.addActionListener(e -> {
            fillState.setColor(Color.GREEN);
            state = fillState;
        });
        black.addActionListener(e -> {
            fillState.setColor(Color.BLACK);
            state = fillState;
        });
    }

    private void initFiguresMenu(JMenuBar menuBar) throws IOException {
        JMenu menu = new JMenu("Add Figure");
        JMenuItem circle = new JMenuItem("Circle", new ImageIcon(getImageResource("icons/circle.png")));
        JMenuItem triangle = new JMenuItem("Triangle", new ImageIcon(getImageResource("icons/triangle.png")));
        JMenuItem rectangle = new JMenuItem("Rectangle", new ImageIcon(getImageResource("icons/rectangle.png")));
        menu.add(circle);
        menu.add(triangle);
        menu.add(rectangle);
        menuBar.add(menu);
        circle.addActionListener(e -> {
            addFigureState.setType(AddFigureState.Type.CIRCLE);
            state = addFigureState;
        });
        triangle.addActionListener(e -> {
            addFigureState.setType(AddFigureState.Type.TRIANGLE);
            state = addFigureState;
        });
        rectangle.addActionListener(e -> {
            addFigureState.setType(AddFigureState.Type.RECTANGLE);
            state = addFigureState;
        });
    }

    private BufferedImage getImageResource(String resource) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream(resource));
    }

    private void draw(List<Shape> shapes, Canvas canvas) {
        for (Shape shape : shapes) {
            try {
                shape.draw(canvas);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printResult(List<Shape> shapes, FileWriter out) {
        for (Shape shape : shapes) {
            try {
                if (shape instanceof PrintDecorator) {
                    ((PrintDecorator) shape).print(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }
}