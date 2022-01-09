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

    public GraphSeries(ArrayList<Vector2f> data, String quickProps) {
        this.data = data;
        this.props = new GraphProperties(quickProps);
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
    private String drawType;

    private void updateProperties() {
        solid = Boolean.parseBoolean(props.getOrDefault("solid", "false"));
        size = Integer.parseInt(props.getOrDefault("size", "10"));
        strokeSize = Integer.parseInt(props.getOrDefault("stroke_size", "2"));
        color = GraphProperties.getColor(props.getOrDefault("color", "black"));
        drawType = props.getOrDefault("draw_style", "o");
    }

    public void paint(Graphics2D g, double x1, double y1, double x2, double y2, Dimension displaySize, int padX, int padY) {
        if (drawType.contains("o")) {
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

        if (drawType.contains(".")) {
            for (Vector2f v : data) {
                double px = v.x - x1;
                double py = v.y - y1;
                double dX = px / (x2 - x1);
                double dY = py / (y2 - y1);
                int x = (int) (dX * (displaySize.width - 2 * padX));
                int y = (int) -(dY * (displaySize.height - 2 * padY)) + displaySize.height - 2 * padY;
                g.setStroke(new BasicStroke(strokeSize));
                g.setColor(color);
                g.fillOval(x + padX - size / 2, y + padY - size / 2, size, size);
            }
        }

        if (drawType.contains("-")) {
            for (int i = 0; i < data.size() - 1; i++) {
                double px = data.get(i).x - x1;
                double py = data.get(i).y - y1;
                double dX = px / (x2 - x1);
                double dY = py / (y2 - y1);
                int px1 = (int) (dX * (displaySize.width - 2 * padX));
                int py1 = (int) -(dY * (displaySize.height - 2 * padY)) + displaySize.height - 2 * padY;

                px = data.get(i + 1).x - x1;
                py = data.get(i + 1).y - y1;
                dX = px / (x2 - x1);
                dY = py / (y2 - y1);
                int px2 = (int) (dX * (displaySize.width - 2 * padX));
                int py2 = (int) -(dY * (displaySize.height - 2 * padY)) + displaySize.height - 2 * padY;

                if (data.get(i).y * data.get(i+1).y < 0) {
                    continue;
                }
                g.setStroke(new BasicStroke(strokeSize));
                g.setColor(color);
                g.drawLine(px1 + padX, py1 + padY, px2 + padX, py2 + padY);
            }
        }
    }

}
