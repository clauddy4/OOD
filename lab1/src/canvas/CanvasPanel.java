package canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class CanvasPanel extends JPanel {
    private static final int STROKE_WIDTH = 10; // Толщина линии
    private final transient CanvasPanelDrawable canvas;

    public CanvasPanel(CanvasPanelDrawable canvas) {
        this.canvas = canvas;
        MouseAdapter adapter = canvas.getMouseAdapter(this);
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        initGraphics2D(g2d);
        canvas.draw(g2d);
    }

    private void initGraphics2D(final Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setStroke(new BasicStroke(STROKE_WIDTH));
    }
}