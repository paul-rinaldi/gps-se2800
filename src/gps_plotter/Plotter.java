package gps_plotter;


import gps.*;

import javafx.scene.chart.LineChart;

import java.util.Date;

public class Plotter {

    private static double MS_IN_MIN = 60000;
    private static double DBL_EPSILON = 1E+6;

    private LineChart<Double, Double> chart;

    /**
     * Makes plotter object and sets LineChart element from controller
     *
     * @param chart LineChart to modify
     */
    public Plotter(LineChart<Double, Double> chart){
        this.chart = chart;
    }

    public void plotElevationGain(Track track){

        double lastElevation = track.getTrackPoint(0).getElevation();
        double elevationPoint = 0;
        Date firstDate = null;
        Date currentDate;

        for(int i = 0; i < track.getPointAmount(); i++){

            double currentElevation = track.getTrackPoint(i).getElevation();

            if(i == 0){
                currentElevation = 0;
                firstDate = track.getTrackPoint(i).getTime();
            }

            currentDate = track.getTrackPoint(i).getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            elevationPoint += calculateElevationGain(currentElevation, lastElevation);

            plotPoint(timePoint, elevationPoint);

            lastElevation = track.getTrackPoint(i).getElevation();
        }
    }

    private double calculateElevationGain(double current, double last){

        double result = current - last;

        return (result-result) > DBL_EPSILON ? result : 0;

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

    private double timePassedInMin(Date current, Date first){
        long differenceMs = current.getTime() - first.getTime();
        double timeInMin = differenceMs/MS_IN_MIN;
        return timeInMin;

    }



}
