package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GraphAxis {

    private final GraphProperties props;

    private String label;
    private int numIntervals;
    private String orientation;

    private String numberFormat;

    private Font labelFont;
    private Font markFont;

    private Color labelColor;
    private Color markColor;

    private Color lineColor;

    public GraphAxis(String[][] props) {
        this.props = new GraphProperties(props);
        this.updateProperties();
    }

    public GraphAxis(String... varargs) {
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

    private void updateProperties() {
        label = props.getOrDefault("label", "");
        numIntervals = Integer.parseInt(props.getOrDefault("num_intervals", "5"));
        orientation = props.getOrDefault("orientation", "X");

        String textFont = props.getOrDefault("font", "Helvetica");
        int textStyle = Integer.parseInt(props.getOrDefault("text_style", "0"));
        int labelSize = Integer.parseInt(props.getOrDefault("label_size", "28"));
        int markSize = Integer.parseInt(props.getOrDefault("mark_size", "20"));
        labelFont = new Font(textFont, textStyle, labelSize);
        markFont = new Font(textFont, textStyle, markSize);

        labelColor = GraphProperties.getColor(props.getOrDefault("label_color", "k"));
        markColor = GraphProperties.getColor(props.getOrDefault("mark_color", "k"));
        lineColor = GraphProperties.getColor(props.getOrDefault("line_color", "k"));

        numberFormat = props.getOrDefault("number_format", "%4.2e");
        String formatType = props.getOrDefault("format", "none");
        switch (formatType) {
            case "scientific":
                numberFormat = "%.2e";
                break;
            case "float":
                numberFormat = "%.2g";
                break;
        }
    }

    public void paint(Graphics2D g, double v1, double v2, Dimension displaySize, int padX, int padY, int ox, int oy) {
        g.setFont(labelFont);
        g.setColor(labelColor);
        FontMetrics markMetrics = g.getFontMetrics(markFont);
        if (orientation.equals("x")) {
            g.setColor(lineColor);
            g.drawLine(padX, displaySize.height - padY, displaySize.width - padX, displaySize.height - padY);
            g.drawLine(padX, padY, displaySize.width - padX, padY);
            Rectangle labelRect = new Rectangle(0, displaySize.height - padY / 2, displaySize.width, padY / 2);
            g.setColor(labelColor);
            drawCenteredString(g, label, labelRect, labelFont, 0);

            // draw intervals
            g.setFont(markFont);

            int dx = (displaySize.width - 2 * padX) / (numIntervals - 1);
            int y1 = displaySize.height - padY;
            int y2 = displaySize.height - 5 * padY / 6;

            double intervalValue = (v2 - v1) / (numIntervals - 1);

            for (int i = 0; i < numIntervals; i++) {
                int x = padX + i * dx;
                g.setColor(lineColor);
                g.drawLine(x, y1, x, y2);
                double value = (i * intervalValue + v1) - (ox * (intervalValue / dx));
                g.setColor(markColor);
                Rectangle markRect = new Rectangle(x - padX / 6, y2, padX / 3, markMetrics.getHeight());
                drawCenteredString(g, String.format(numberFormat, value), markRect, markFont, 0);
            }
        } else if (orientation.equals("y")) {
            g.setColor(lineColor);
            g.drawLine(padX, padY, padX, displaySize.height - padY);
            g.drawLine(displaySize.width - padX, padY, displaySize.width - padX, displaySize.height - padY);
            Rectangle labelRect = new Rectangle(0, 0, padX / 2, displaySize.height);
            g.setColor(labelColor);
            drawCenteredString(g, label, labelRect, labelFont, 90);

            // draw intervals
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(-90), 0, 0);
            Font rotatedFont = markFont.deriveFont(transform);
            g.setFont(rotatedFont);
            g.setColor(markColor);

            int dy = (displaySize.height - 2 * padY) / (numIntervals - 1);
            int x1 = 5 * padX / 6;

            double intervalValue = (v1 - v2) / (numIntervals - 1);

            for (int i = 0; i < numIntervals; i++) {
                int y = padY + i * dy;
                g.setColor(lineColor);
                g.drawLine(x1, y, padX, y);
                double value = (i * intervalValue + v2) - (oy * (intervalValue / dy));
                g.setColor(markColor);
                int width = markMetrics.getHeight();
                Rectangle markRect = new Rectangle(x1 - width, y - padY / 6, width, padY / 3);
                drawCenteredString(g, String.format(numberFormat, value), markRect, markFont, 90);
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
