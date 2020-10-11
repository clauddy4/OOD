package canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public interface CanvasPanelDrawable {
    void draw(Graphics2D g2d);
    MouseAdapter getMouseAdapter(JPanel panel);
}