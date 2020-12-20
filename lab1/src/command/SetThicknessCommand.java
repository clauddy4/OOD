package command;

import canvas.CanvasShape;

public class SetThicknessCommand implements Command{
    private boolean executed;
    private int stroke;
    private int backup;
    private CanvasShape shape;

    public SetThicknessCommand(CanvasShape shape, int stroke) {
        this.backup = shape.getStroke();
        this.shape = shape;
        this.stroke = stroke;
    }

    @Override
    public void undo() {
        if (executed) {
            shape.setStroke(backup);
            executed = false;
        }
    }

    @Override
    public void redo() {
        if (!executed) {
            shape.setStroke(stroke);
            executed = true;
        }
    }

    @Override
    public void execute() {
        shape.setStroke(stroke);
        executed = true;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}
