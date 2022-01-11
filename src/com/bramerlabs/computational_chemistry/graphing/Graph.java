package com.bramerlabs.computational_chemistry.graphing;

import com.bramerlabs.computational_chemistry.math.vector.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph implements Runnable {

    public GraphDisplay graphDisplay;
    public GraphRenderer graphRenderer;
    public GraphListener listener;

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Graph() {
        Dimension windowSize = new Dimension(screenSize.width / 4, screenSize.width / 4);
        graphDisplay = new GraphDisplay(windowSize);
        GraphAxis xAxis = new GraphAxis("orientation", "x", "number_format", "%.2g");
        GraphAxis yAxis = new GraphAxis("orientation", "y", "number_format", "%.2g");
        GraphTitle title = new GraphTitle();

        graphRenderer = new GraphRenderer(graphDisplay, xAxis, yAxis, title);
        graphRenderer.setPadding(windowSize.width / 10, windowSize.height / 10);
        graphDisplay.setRenderer(graphRenderer);

        listener = new GraphListener();
        graphDisplay.addGraphListener(listener);

        this.start();
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

    public void start() {
        Thread graph = new Thread(this, this.toString());
        graph.start();
    }

    @Override
    public void run() {
        while (!listener.isKeyDown(KeyEvent.VK_ESCAPE) && !listener.isWindowClosed()) {
            repaint();
            listener.update();
            graphDisplay.update();
        }
        graphDisplay.close();
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.xLabel("x");
        graph.yLabel("y");
        graph.title("sin(x) and tan(x)");

        ArrayList<Vector2f> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new Vector2f(i, i));
        }

        GraphSeries gs = new GraphSeries(data, "-.ob");
        graph.addSeries(gs);
        graph.repaint();
    }
}
