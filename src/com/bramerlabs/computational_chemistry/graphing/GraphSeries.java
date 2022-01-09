package com.bramerlabs.computational_chemistry.graphing;

import com.bramerlabs.computational_chemistry.math.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphSeries {

    public static final String COLOR = "color";
    public static final String SIZE = "size";
    public static final String BORDER_WIDTH = "border_width";
    public static final String HOLLOW = "hollow";

    private Color color = Color.BLACK;
    private int size = 5;
    private int border_width = 1;
    private boolean hollow = true;

    public static final HashMap<String, Color> colors;
    static {
        colors = new HashMap<>();
        colors.put("red", Color.RED);
        colors.put("orange", Color.ORANGE);
        colors.put("yellow", Color.YELLOW);
        colors.put("green", Color.GREEN);
        colors.put("blue", Color.BLUE);
        colors.put("magenta", Color.MAGENTA);
        colors.put("cyan", Color.CYAN);
        colors.put("black", Color.BLACK);
        colors.put("r", Color.RED);
        colors.put("o", Color.ORANGE);
        colors.put("y", Color.YELLOW);
        colors.put("g", Color.GREEN);
        colors.put("b", Color.BLUE);
        colors.put("m", Color.MAGENTA);
        colors.put("c", Color.CYAN);
        colors.put("k", Color.BLACK);
    }

    public final ArrayList<Vector2f> series;
    public HashMap<String, String> props;

    public GraphSeries(ArrayList<Vector2f> series) {
        this.series = series;
        props = new HashMap<>();
    }

    public GraphSeries(ArrayList<Vector2f> series, String[] properties) {
        this.series = series;
        props = new HashMap<>();
        for (int i = 0; i < properties.length; i += 2) {
            if (i + 1 < properties.length) {
                props.put(properties[i], properties[i+1]);
            }
        }
        updateProperties();
    }

    public GraphSeries(ArrayList<Vector2f> series, HashMap<String, String> props) {
        this.series = series;
        this.props = props;
        updateProperties();
    }

    public void setProperty(String property, String value) {
        this.props.put(property, value);
        updateProperties();
    }

    private void updateProperties() {
        this.color = colors.getOrDefault(props.getOrDefault(COLOR, "k"), Color.BLACK);
        this.size = Integer.parseInt(props.getOrDefault(SIZE, "5"));
        this.border_width = Integer.parseInt(props.getOrDefault(BORDER_WIDTH, "1"));
        this.hollow = Boolean.parseBoolean(props.getOrDefault(HOLLOW, "false"));
    }

    public void paint(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(border_width));
        for (Vector2f value : series) {
            if (hollow) {
                g.drawOval((int) value.x - size, (int) value.y - size, 2 * size, 2 * size);
            } else {
                g.fillOval((int) value.x - size, (int) value.y - size, 2 * size, 2 * size);
            }
        }
    }

}
