package com.bramerlabs.computational_chemistry.graphing;

import java.awt.*;
import java.util.ArrayList;

public class GraphRenderer {

    ArrayList<GraphSeries> series;
    ArrayList<GraphAxis> axes;

    public void paint(Graphics2D g) {

        for (GraphSeries s : series) {
            s.paint(g);
        }

        for (GraphAxis a : axes) {
            a.paint(g);
        }

    }

}
