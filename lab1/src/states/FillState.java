package states;

import canvas.Canvas;

import java.awt.*;
import java.awt.event.MouseEvent;

import static java.awt.Color.*;

public class FillState implements State {
    private Canvas canvas;
    private Color color;

    public FillState(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void onClick(MouseEvent event) {
        if (RED.equals(color)) {
            setColor(event, RED);
        } else if (BLUE.equals(color)) {
            setColor(event, BLUE);
        } else if (YELLOW.equals(color)) {
            setColor(event, YELLOW);
        } else if (GREEN.equals(color)) {
            setColor(event, GREEN);
        } else if (BLACK.equals(color)) {
            setColor(event, BLACK);
        }
    }

    private void setColor(MouseEvent event, Color color) {
        var item = canvas.findCanvasShape(event.getPoint());
        if (item != null) {
            item.setColor(color);
            canvas.repaint();
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
}