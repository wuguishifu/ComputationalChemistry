package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;
import java.util.ArrayList;

public class GraphRenderer {

    private final ArrayList<GraphSeries> series;
    private final GraphAxis xAxis, yAxis;
    private final GraphTitle title;

    private double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    public boolean hasBounds = false;
    private int padX = 0, padY = 0;
    private Dimension displaySize;

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
        if (!hasBounds) {
            this.x1 = x1;
            this.x2 = x2;
            this.hasBounds = true;
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
        if (!hasBounds) {
            this.y1 = y1;
            this.y2 = y2;
            this.hasBounds = true;
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

    public void paint(Graphics2D g) {
        for (GraphSeries series : series) {
            series.paint(g, x1, y1, x2, y2, displaySize, padX, padY);
        }

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
