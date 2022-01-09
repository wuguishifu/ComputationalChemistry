package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;
import java.util.ArrayList;

public class GraphRenderer {

    private final ArrayList<GraphSeries> series;
    private final GraphAxis xAxis, yAxis;
    private final GraphTitle title;

    private double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    public boolean hasXBounds = false, hasYBounds = false;
    private int padX = 0, padY = 0;
    private int seriesPadX = 15, seriesPadY = 15;
    private Dimension displaySize;

    private static final Color backgroundColor = new Color(238, 238, 238);

    public GraphRenderer(GraphDisplay gd, GraphAxis xAxis, GraphAxis yAxis, GraphTitle title) {
        this.displaySize = gd.getWindowSize();
        series = new ArrayList<>();
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.title = title;
    }

    public void setDisplaySize(Dimension displaySize) {
        this.displaySize = displaySize;
    }

    public void addSeries(GraphSeries series) {
        this.series.add(series);
    }

    public void removeSeries(GraphSeries series) {
        this.series.remove(series);
    }

    public void expandXBounds(double x1, double x2) {
        if (!hasXBounds) {
            this.x1 = x1;
            this.x2 = x2;
            this.hasXBounds = true;
        } else {
            if (x1 < this.x1) {
                this.x1 = x1;
            }
            if (x2 > this.x2) {
                this.x2 = x2;
            }
        }
    }

    public void setXBounds(double x1, double x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    public void expandYBounds(double y1, double y2) {
        if (!hasYBounds) {
            this.y1 = y1;
            this.y2 = y2;
            this.hasYBounds = true;
        } else {
            if (y1 < this.y1) {
                this.y1 = y1;
            }
            if (y2 > this.y2) {
                this.y2 = y2;
            }
        }
    }

    public void setYBounds(double y1, double y2) {
        this.y1 = y1;
        this.y2 = y2;
    }

    public void setPadding(int padX, int padY) {
        this.padX = padX;
        this.padY = padY;
    }

    public void setSeriesPadding(int seriesPadX, int seriesPadY) {
        this.seriesPadX = seriesPadX;
        this.seriesPadY = seriesPadY;
    }

    public void paint(Graphics2D g) {
        g.fillRect(0, 0, displaySize.width, displaySize.height);

        g.setColor(Color.WHITE);
        g.fillRect(padX, padY, displaySize.width - 2 * padX, displaySize.height - 2 * padY);

        for (GraphSeries series : series) {
            series.paint(g, x1, y1, x2, y2, displaySize, padX + seriesPadX, padY + seriesPadY);
        }

        g.setColor(backgroundColor);
        g.fillRect(0, 0, padX, displaySize.height);
        g.fillRect(padX, 0, displaySize.width - padX, padY);
        g.fillRect(displaySize.width - padX, padY, padX, displaySize.height - padY);
        g.fillRect(padX, displaySize.height - padY, displaySize.width - 2 * padX, padY);

        xAxis.paint(g, x1, x2, displaySize, padX, padY);
        yAxis.paint(g, y1, y2, displaySize, padX, padY);

        title.paint(g, displaySize, padY);
    }

    public void setXAxisProperty(String prop, String value) {
        xAxis.setProperty(prop, value);
    }

    public void setYAxisProperty(String prop, String value) {
        yAxis.setProperty(prop, value);
    }

    public void setTitleProperty(String prop, String value) {
        title.setProperty(prop, value);
    }

    public ArrayList<GraphSeries> getSeries() {
        return series;
    }

    public GraphAxis getXAxis() {
        return xAxis;
    }

    public GraphAxis getYAxis() {
        return yAxis;
    }

    public GraphTitle getTitle() {
        return title;
    }
}
