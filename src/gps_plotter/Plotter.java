package gps_plotter;

import javafx.scene.chart.LineChart;

public class Plotter {

    private LineChart<Double, Double> chart;

    /**
     * Makes plotter object and sets LineChart element from controller
     *
     * @param chart LineChart to modify
     */
    public Plotter(LineChart<Double, Double> chart){
        this.chart = chart;
    }

    public void plotElevationGain(){
        //TODO
    }

    private double calculateElevationGain(double current, double last){

        double result = current - last;

        return result > 0 ? current : last;

    }

    private void clearChart(){
        //TODO
    }

    private void scaleChart(double xMax, double xMin, double yMax, double yMin){
        //TODO
    }

    private void plotPoint(double x, double y){
        //TODO
    }
}
