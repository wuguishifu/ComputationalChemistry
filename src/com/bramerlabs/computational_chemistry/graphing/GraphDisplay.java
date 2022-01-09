package com.bramerlabs.computational_chemistry.graphing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphDisplay {

    public JFrame frame;
    public JPanel panel;

    public ArrayList<GraphRenderer> renderers;

    public GraphDisplay(Dimension windowSize) {
        renderers = new ArrayList<>();
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                for (GraphRenderer renderer : renderers) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

    public void addRenderer(GraphRenderer gr) {
        this.renderers.add(gr);
    }
}
