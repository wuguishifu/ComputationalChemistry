package com.bramerlabs.computational_chemistry.graphing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GraphDisplay {

    private final JPanel panel;
    private final JFrame frame;

    private GraphRenderer renderer;
    private Dimension displaySize;

    private GraphListener listener;

    public GraphDisplay(Dimension windowSize) {
        this.displaySize = windowSize;
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g.create();
                if (renderer != null) {
                    renderer.paint(g2d);
                }
            }
        };
        panel.setPreferredSize(windowSize);
        frame.add(panel);
        frame.pack();

        frame.setVisible(true);
    }

    public void repaint() {
        panel.repaint();
    }

    public void setRenderer(GraphRenderer gr) {
        this.renderer = gr;
    }

    public void setDisplaySize(Dimension displaySize) {
        this.displaySize = displaySize;
    }

    public Dimension getDisplaySize() {
        return this.displaySize;
    }

    public ArrayList<GraphSeries> getSeries() {
        return renderer.getSeries();
    }

    public void addGraphListener(GraphListener listener) {
        this.listener = listener;
        this.frame.addKeyListener(listener);
        this.frame.addWindowListener(listener);
        this.panel.addMouseListener(listener);
        this.panel.addMouseMotionListener(listener);
        this.panel.addMouseWheelListener(listener);
    }

    public void close() {
        this.frame.dispose();
    }

    private boolean moving = false;
    private int px, py;
    private int cx, cy;
    private int dx, dy;
    private int cmx = -1, cmy = -1;
    private int pmx, pmy;
    boolean cmd, pmd;
    private int offsetX, offsetY;

    public void update() {
        pmd = cmd;
        cmd = listener.isMouseButtonDown(MouseEvent.BUTTON1);

        if (!moving) {
            px = listener.getMouseX();
            py = listener.getMouseY();
        }

        if (cmd && !pmd) {
            int cx = listener.getMouseX();
            int cy = listener.getMouseY();
            // bounds check
            if (cx <= displaySize.width - renderer.getPadX() && cx >= renderer.getPadX() &&
                    cy <= displaySize.height - renderer.getPadY() && cy >= renderer.getPadY()) {
                moving = true;
            }
        }

        if (!cmd && pmd) {
            moving = false;
        }

        if (moving) {
            if (listener.isMouseButtonDown(MouseEvent.BUTTON1)) {
                cx = listener.getMouseX();
                cy = listener.getMouseY();
                dx = cx - px;
                dy = cy - py;
                px = cx;
                py = cy;
                offsetX += dx;
                offsetY += dy;
            }
        }


        if (listener.isKeyDown(KeyEvent.VK_H)) {
            offsetX = 0;
            offsetY = 0;
            listener.clearScrollMult();
            renderer.reset();
        }
        renderer.setOffset(offsetX, offsetY);
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

}
