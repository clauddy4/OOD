package command;

import canvas.CanvasShape;

import java.awt.*;

public class SetColorCommand implements Command{
    private boolean executed;
    private Color color;
    private Color backup;
    private CanvasShape shape;

    public SetColorCommand(CanvasShape shape, Color color) {
        this.shape = shape;
        this.backup = shape.getColor();
        this.color = color;
    }

    @Override
    public void undo() {
        if (executed) {
            shape.setColor(backup);
            executed = false;
        }
        if (executed) {
            shape.setColor(backup);
            executed = false;
        }
    }

    @Override
    public void redo() {
        if (!executed) {
            shape.setColor(color);
            executed = true;
        }
    }

    @Override
    public void execute() {
        shape.setColor(color);
        executed = true;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}
