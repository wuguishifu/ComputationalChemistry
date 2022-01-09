package com.bramerlabs.computational_chemistry.graphing;

import com.bramerlabs.computational_chemistry.math.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph {

    public GraphDisplay graphDisplay;
    public GraphRenderer graphRenderer;

    public Graph() {
        Dimension windowSize = new Dimension(1600, 1200);
        graphDisplay = new GraphDisplay(windowSize);
        GraphAxis xAxis = new GraphAxis("orientation", "x");
        GraphAxis yAxis = new GraphAxis("orientation", "y");
        GraphTitle title = new GraphTitle();
        graphRenderer = new GraphRenderer(graphDisplay, xAxis, yAxis, title);
        graphDisplay.setRenderer(graphRenderer);

        graphRenderer.setPadding(windowSize.width / 10, windowSize.height / 10);
    }

    public void repaint() {
        graphDisplay.repaint();
    }

    public void addSeries(GraphSeries series) {
        ArrayList<Vector2f> data = series.data;
        float[] xData = new float[data.size()];
        float[] yData = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            xData[i] = data.get(i).x;
            yData[i] = data.get(i).y;
        }
        Arrays.sort(xData);
        Arrays.sort(yData);
        float x1 = xData[0], x2 = xData[xData.length - 1];
        float y1 = yData[0], y2 = yData[yData.length - 1];
        graphRenderer.expandXBounds(x1, x2);
        graphRenderer.expandYBounds(y1, y2);
        graphRenderer.addSeries(series);
    }

    public void xLim(double x1, double x2) {
        graphRenderer.setXBounds(x1, x2);
    }

    public void yLim(double y1, double y2) {
        graphRenderer.setYBounds(y1, y2);
    }

    public void xLabel(String label) {
        graphRenderer.setXAxisProperty("label", label);
    }

    public void yLabel(String label) {
        graphRenderer.setYAxisProperty("label", label);
    }

    public void title(String text) {
        graphRenderer.setTitleProperty("text", text);
    }

    public ArrayList<GraphSeries> getSeries() {
        return graphRenderer.getSeries();
    }

    public GraphAxis getXAxis() {
        return graphRenderer.getXAxis();
    }

    public GraphAxis getYAxis() {
        return graphRenderer.getYAxis();
    }

    public GraphTitle getTitle() {
        return graphRenderer.getTitle();
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        ArrayList<Vector2f> data1 = new ArrayList<>();
        ArrayList<Vector2f> data2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data1.add(new Vector2f(i, i / 2.f + 25));
            data2.add(new Vector2f(i + 10, i));
        }
        graph.addSeries(new GraphSeries(data2, new String[][]{{"color", "blue"}, {"size", "5"}}));
        graph.addSeries(new GraphSeries(data1, "color", "red", "size", "10", "solid", "false"));
        graph.xLabel("test label x");
        graph.yLabel("test label y");
        graph.title("test title");

        graph.repaint();
    }

}
