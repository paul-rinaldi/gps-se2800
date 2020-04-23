package gps_plotter;

import gps_plotter.PlotterController;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class ColorLinePlotter {
    ArrayList<XYChart.Series> Lines = new ArrayList<>();
    ArrayList<Integer> Speeds = new ArrayList<>();
    Node chartLegend;

    private LineChart<Double, Double> chart;

    private PlotterController plotterController;

    /**
     * Makes color line plotter object and sets LineChart element from controller
     *
     * @param chart LineChart to modify
     */
    public ColorLinePlotter(LineChart<Double, Double> chart, PlotterController plotterController){
        this.chart = chart;
        this.plotterController = plotterController;
    }





    private void speedLegend() {

    }


}
