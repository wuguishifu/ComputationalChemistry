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
    private Dimension windowSize;

    private GraphListener listener;

    public GraphDisplay(Dimension windowSize) {
        this.windowSize = windowSize;
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g.create();
                if (renderer != null) {
                    renderer.paint(g2d, offsetX, offsetY, zoom);
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

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    public Dimension getWindowSize() {
        return this.windowSize;
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
    private int offsetX, offsetY;

    private double zoom = 1;


    public void update() {
        if (!listener.isMouseButtonDown(MouseEvent.BUTTON1)) {
            px = listener.getMouseX();
            py = listener.getMouseY();
        }

        if (listener.isMouseButtonDown(MouseEvent.BUTTON1)) {
            cx = listener.getMouseX();
            cy = listener.getMouseY();
            dx = cx - px;
            dy = cy - py;
            px = cx;
            py = cy;
//            dx *= zoom;
//            dy *= zoom;
            offsetX += dx * zoom;
            offsetY += dy * zoom;
        }

        if (listener.isKeyDown(KeyEvent.VK_H)) {
            offsetX = 0;
            offsetY = 0;
            listener.clearScrollMult();
        }

//        zoom = listener.getScrollMult();

    }

}
