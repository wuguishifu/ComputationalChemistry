package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;

public class GraphProperties {

    public static final HashMap<String, Color> colors;
    public final HashMap<String, String> properties;

    public GraphProperties(String[][] props) {
        properties = new HashMap<>();
        for (String[] prop : props) {
            if (prop.length == 2) {
                properties.put(prop[0], prop[1]);
            }
        }
    }

    public void addProperties(String[][] props) {
        for (String[] prop : props) {
            if (prop.length == 2) {
                properties.put(prop[0], prop[1]);
            }
        }
    }

    public void addProperty(String prop, String value) {
        properties.put(prop, value);
    }

    public void addProperty(String prop, int value) {
        properties.put(prop, String.valueOf(value));
    }

    public void addProperty(String prop, double value) {
        properties.put(prop, String.valueOf(value));
    }

    public void addProperty(String prop, boolean value) {
        properties.put(prop, String.valueOf(value));
    }

    public String getOrDefault(String prop, String defaultValue) {
        return properties.getOrDefault(prop, defaultValue);
    }

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

    public static Color getColor(String color) {
        String c = color.toLowerCase(Locale.ROOT);
        return colors.getOrDefault(c, Color.BLACK);
    }

}
