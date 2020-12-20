package command;

import canvas.CanvasShape;

import java.awt.*;

public class SetStrokeColorCommand implements Command{
    private boolean executed;
    private Color strokeColor;
    private Color backup;
    private CanvasShape shape;

    public SetStrokeColorCommand(CanvasShape shape, Color strokeColor) {
        this.shape = shape;
        this.backup = shape.getStrokeColor();
        this.strokeColor = strokeColor;
    }

    @Override
    public void undo() {
        if (executed) {
            shape.setStrokeColor(backup);
            executed = false;
        }
        if (executed) {
            shape.setStrokeColor(backup);
            executed = false;
        }
    }

    @Override
    public void redo() {
        if (!executed) {
            shape.setStrokeColor(strokeColor);
            executed = true;
        }
    }

    @Override
    public void execute() {
        shape.setStrokeColor(strokeColor);
        executed = true;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}
