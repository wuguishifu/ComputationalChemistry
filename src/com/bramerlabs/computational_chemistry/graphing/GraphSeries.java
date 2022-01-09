package com.bramerlabs.computational_chemistry.graphing;

import com.bramerlabs.computational_chemistry.math.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public class GraphSeries {

    protected ArrayList<Vector2f> data;
    private final GraphProperties props;

    public GraphSeries(ArrayList<Vector2f> data, String[][] props) {
        this.data = data;
        this.props = new GraphProperties(props);
        updateProperties();
    }

    public GraphSeries(ArrayList<Vector2f> data, String... varargs) {
        this.data = data;
        String[][] props = new String[varargs.length / 2][2];
        for (int i = 0; i < varargs.length; i += 2) {
            props[i / 2][0] = varargs[i];
            props[i / 2][1] = varargs[i + 1];
        }
        this.props = new GraphProperties(props);
        updateProperties();
    }

    public void setProperty(String prop, String value) {
        this.props.addProperty(prop, value);
    }

    public void setProperties(String... varargs) {
        for (int i = 0; i < varargs.length; i += 2) {
            if (i + 1 < varargs.length) {
                props.addProperty(varargs[i], varargs[i + 1]);
            }
        }
        updateProperties();
    }

    private boolean solid;
    private int size;
    private int strokeSize;
    private Color color;

    private void updateProperties() {
        solid = Boolean.parseBoolean(props.getOrDefault("solid", "true"));
        size = Integer.parseInt(props.getOrDefault("size", "3"));
        strokeSize = Integer.parseInt(props.getOrDefault("stroke_size", "2"));
        color = GraphProperties.getColor(props.getOrDefault("color", "black"));
    }

    public void paint(Graphics2D g, double x1, double y1, double x2, double y2, Dimension displaySize, int padX, int padY) {
        for (Vector2f v : data) {
            double px = v.x - x1;
            double py = v.y - y1;
            double dX = px / (x2 - x1);
            double dY = py / (y2 - y1);
            int x = (int) (dX * (displaySize.width - 2 * padX));
            int y = (int) -(dY * (displaySize.height - 2 * padY)) + displaySize.height - 2 * padY;
            g.setStroke(new BasicStroke(strokeSize));
            g.setColor(color);
            if (solid) {
                g.fillOval(x + padX - size, y + padY - size, 2 * size, 2 * size);
            } else {
                g.drawOval(x + padX - size, y + padY - size, 2 * size, 2 * size);
            }
        }
    }

}
