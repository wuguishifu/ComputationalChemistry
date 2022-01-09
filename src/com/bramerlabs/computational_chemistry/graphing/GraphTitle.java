package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;

public class GraphTitle {

    private final GraphProperties props;
    private Font font;

    private String text;

    private Color textColor;

    public GraphTitle(String... varargs) {
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
        text = props.getOrDefault("text", "");

        String textFont = props.getOrDefault("font", "Helvetica");
        int textStyle = Integer.parseInt(props.getOrDefault("text_style", "0"));
        int textSize = Integer.parseInt(props.getOrDefault("text_size", "28"));
        font = new Font(textFont, textStyle, textSize);

        textColor = GraphProperties.getColor(props.getOrDefault("text_color", "k"));
    }

    public void paint(Graphics2D g, Dimension displaySize, int padY) {
        g.setFont(font);
        g.setColor(textColor);
        drawCenteredString(g, text, new Rectangle(0, 0, displaySize.width, padY), font);
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString(text, x, y);
    }

}
