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

    /**
     * Plots elevation gain at each TrackPoint's date along the graph
     *
     * @param track track from which points will be plotted
     */
    public void plotElevationGain(Track track){

        clearChart(); //TODO - Change this?

        if(track.getPointAmount() < 2){
            return; //TODO - Must throw an exception for controller
        }

        double lastElevation = track.getTrackPoint(0).getElevation();
        double elevationPoint = 0;
        Date firstDate = null;
        Date currentDate;
        double xMax = 0;
        double xMin = 0;
        double yMax = 0;
        double yMin = 0;

        for(int i = 0; i < track.getPointAmount(); i++){

            double currentElevation = track.getTrackPoint(i).getElevation();

            //Set first date to calculate time passed
            if(i == 0){
                firstDate = track.getTrackPoint(i).getTime();
            }

            currentDate = track.getTrackPoint(i).getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            elevationPoint += calculateElevationGain(currentElevation, lastElevation); //Add the change in elevation to total change

            plotPoint(timePoint, elevationPoint); //Plot point on LineChart

            lastElevation = track.getTrackPoint(i).getElevation();

            //Used to set min/max x and y to scale graph
            if(i == 0){
                xMin = timePoint;
                yMin = elevationPoint;
            } else if(i == track.getPointAmount()){
                xMax = timePoint;
                yMax = elevationPoint;
            }

        }

        scaleChart(xMax, xMin, yMax, yMin); //TODO - Change this?

    }

    /**
     * Returns the gain in elevation from the previous elevation
     *
     * @param current current elevation
     * @param last previous elevation
     * @return difference between them; 0 if difference is < 0
     */
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

    /**
     * Takes two Date objects and calculates the minutes passed between them
     *
     * @param current current Date object
     * @param first first Date object from which time has passed
     * @return time passed in minutes
     */
    private double timePassedInMin(Date current, Date first){
        long differenceMs = current.getTime() - first.getTime();
        double timeInMin = differenceMs/MS_IN_MIN;
        return timeInMin;

    }



}
