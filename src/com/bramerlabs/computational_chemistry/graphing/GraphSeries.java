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
        updateProperties();
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

    int cXLast, cYLast;

    public void paint(Graphics2D g, double xL, double yL, double xH, double yH, Dimension displaySize, int padX,
                      int padY, int ox, int oy) {
        if (drawType.contains(".") || drawType.contains("o")) {
            for (int i = 0; i < data.size(); i++) {
                Vector2f v = data.get(i);
                double px = v.x - xL;
                double py = v.y - yL;
                double dX = px / (xH - xL);
                double dY = py / (yH - yL);
                int x = (int) (dX * (displaySize.width - 2 * padX));
                int y = (int) -(dY * (displaySize.height - 2 * padY)) + displaySize.height - 2 * padY;
                g.setStroke(new BasicStroke(strokeSize));
                g.setColor(color);

                /* for zoom functionality
                int paintX = x - size / 2;
                int paintY = y - size / 2;
                int cx = displaySize.width / 2;
                int cy = displaySize.height / 2;
                paintX = (int) ((paintX - cx + padX + size / 2 + ox) * zoom) + cx;
                paintY = (int) ((paintY - cy + padY + size / 2 + oy) * zoom) + cy;
                */

                int paintX = x + padX + ox;
                int paintY = y + padY + oy;

                if (drawType.contains(".")) {
                    g.fillOval(paintX - size / 2, paintY - size / 2, size, size);
                }

                if (drawType.contains("o")) {
                    if (solid) {
                        g.fillOval(paintX - size, paintY - size, 2 * size, 2 * size);
                    } else {
                        g.drawOval(paintX - size, paintY - size, 2 * size, 2 * size);
                    }
                }

                if (drawType.contains("-") || i > 0) {
                    g.drawLine(cXLast, cYLast, paintX, paintY);
                    cXLast = paintX;
                    cYLast = paintY;
                }
            }
        }
    }

}
