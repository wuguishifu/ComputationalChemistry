package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class GraphAxis {

    public static final String COLOR = "color";
    public static final String TEXT_SIZE = "text_size";
    public static final String SCIENTIFIC_NOTATION = "scientific_notation";

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

    public static final int AXIS_X = 0, AXIS_Y = 1;

    private Color color;
    private int textSize = 15;

    private double v0, v1;
    private int intervals;
    private int orientation;
    private String label;

    private Rectangle rect;
    private Dimension displaySize;
    private double graphWidth, graphHeight;
    private int paddingX, paddingY, graphPaddingX, graphPaddingY;
    private double x1, x2, y1, y2;

    private Font font;
    private boolean SN;
    public HashMap<String, String> props;

    public GraphAxis(float v0, float v1, int intervals, String label, int orientation, String[] props, Rectangle rect, Dimension displaySize) {
        this.v0 = v0;
        this.v1 = v1;
        this.intervals = intervals;
        this.label = label;
        this.orientation = orientation;
        this.rect = rect;
        this.displaySize = displaySize;

        font = new Font("Helvetica", Font.PLAIN, 15);

        this.props = new HashMap();
        for (int i = 0; i < props.length; i++) {
            if (i + 1 < props.length) {
                this.props.put(props[i], props[i+1]);
            }
        }
        updateProperties();

        paddingX = displaySize.width / 10;
        paddingY = displaySize.height / 10;
        if (orientation == AXIS_X) {
            graphWidth = v1 - v0;
            x1 = v0;
            x2 = v1;
        }

    }

    public static GraphAxis xAxis(float v0, float v1, int intervals, String label, Rectangle rect, Dimension windowSize) {
        return new GraphAxis(v0, v1, intervals, label, AXIS_X, new String[]{}, rect, windowSize);
    }

    public static GraphAxis yAxis(float v0, float v1, int intervals, String label, Rectangle rect, Dimension windowSize) {
        return new GraphAxis(v0, v1, intervals, label, AXIS_Y, new String[]{}, rect, windowSize);
    }

    private void updateProperties() {
        this.color = colors.getOrDefault(props.getOrDefault(COLOR, "k"), Color.BLACK);
        this.textSize = Integer.parseInt(props.getOrDefault(TEXT_SIZE, "15"));
        this.SN = Boolean.parseBoolean(props.getOrDefault(SCIENTIFIC_NOTATION, "false"));
    }

    public void paint(Graphics2D g) {
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);

        if (orientation == AXIS_X) {
            graphWidth = v1 - v0;
            g.drawLine(paddingX, displaySize.height - paddingY, displaySize.width - paddingX, displaySize.height - paddingY);
            drawCenteredString(g, label, new Rectangle(0, displaySize.height - paddingY / 2, displaySize.width, paddingY / 2), font, 0);

            // draw intervals
            int dx = (displaySize.width - 2 *paddingX) / (intervals - 1);
            double intervalValue = (v1 - v0) / (intervals - 1);
            int y1 = displaySize.height - paddingY;
            int y2 = displaySize.height - 5 * paddingY / 6;
            int yPos = y1 = metrics.getHeight() / 2 + metrics.getAscent();
            for (int i = 0; i < intervals; i++) {
                int x = paddingX + i * dx;
                g.drawLine(x, y1, x, y2);
                double value = i * intervalValue + v0;
                String s = SN ? String.format("%4.2e", value) : String.valueOf(value);
                g.drawString(s, x - paddingX / 6, yPos);
            }
        } else {
            graphHeight = v1 - v0;
            y1 = v0;
            y2 = v1;
            g.drawLine(paddingX, paddingY, paddingX, displaySize.height - paddingY);
            drawCenteredString(g, label,
                    new Rectangle(0, 0, paddingX / 2, displaySize.height), font, 90);

            // draw intervals
            int dy = (displaySize.height - 2 * paddingY) / (intervals - 1);
            double intervalValue = (v0 - v1) / (intervals - 1);
            int x1 = 5 * paddingX / 6;
            int xPos = x1 - metrics.getHeight() + metrics.getAscent();
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(-90), 0, 0);
            Font rotatedFont = font.deriveFont(affineTransform);
            g.setFont(rotatedFont);
            for (int i = 0; i < intervals; i++) {
                int y = paddingY + i * dy;
                double value = i * intervalValue + v1;
                String s = SN ? String.format("%4.2e", value) : String.valueOf(value);
                g.drawString(s, xPos, y + paddingY / 5);
            }
        }
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font, int angle) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        Graphics2D g2d = (Graphics2D) g;
        if (angle == 0) {
            g2d.drawString(text, x, y);
        } else {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(-angle),
                    metrics.stringWidth(text) / 2., 0);
            Font rotatedFont = font.deriveFont(affineTransform);
            g2d.setFont(rotatedFont);
            g2d.drawString(text, x, y);
        }
    }

}
