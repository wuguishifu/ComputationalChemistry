package com.bramerlabs.computational_chemistry.graphing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphDisplay {

    private final JPanel panel;

    private GraphRenderer renderer;
    private Dimension windowSize;

    public GraphDisplay(Dimension windowSize) {
        this.windowSize = windowSize;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                renderer.paint(g2d);
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

}
