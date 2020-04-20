package gps_plotter;


import gps.*;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class Plotter {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private static final double DEG_TO_RAD = 0.0174533;
    private static final double M_TO_KM = 0.001;
    private static final double M_TO_MI = 0.000621371;
    private static final double RADIUS_OF_EARTH_M = 6371000;

    private static double MS_IN_MIN = 60000;
    //private static double DBL_EPSILON = 1E+6;

    private LineChart<Double, Double> chart;
    private ArrayList<XYChart.Series> seriesArrayList;

    private PlotterController plotterController;

    /**
     * Makes plotter object and sets LineChart element from controller
     *
     * @param chart LineChart to modify
     */
    public Plotter(LineChart<Double, Double> chart, PlotterController plotterController){
        this.chart = chart;
        this.plotterController = plotterController;
        seriesArrayList = new ArrayList<>();
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

        double highestElevation = track.getTrackPoint(0).getElevation();
        double elevationPoint = 0;
        Date firstDate = null;
        Date currentDate;
        double xMax = 0;
        double xMin = 0;
        double yMax = 0;
        double yMin = 0;

        for(int i = 0; i < track.getPointAmount(); i++){

            TrackPoint currentTrackPoint = track.getTrackPoint(i);

            double currentElevation = currentTrackPoint.getElevation();

            //Set first date to calculate time passed
            if(i == 0){
                firstDate = currentTrackPoint.getTime();
            }

            currentDate = currentTrackPoint.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            elevationPoint += calculateElevationGain(currentElevation, highestElevation); //Add the change in elevation to total change

            plotPoint(timePoint, elevationPoint); //Plot point on LineChart

            if(elevationPoint > 0) { //Only set highest elevation if gain is above 0
                highestElevation = currentTrackPoint.getElevation();
            }

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
     * @param highest highest elevation read
     * @return difference between them; 0 if difference is < 0
     */
   public double calculateElevationGain(double current, double highest){

       if(current < 0 || highest < 0){ //Values below zero will not be expected (below sea level)
           return 0;
       }

        double result = current - highest;

        return result > 0 ? result : 0;

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
    public double timePassedInMin(Date current, Date first){
        long differenceMs = current.getTime() - first.getTime();
        double timeInMin = differenceMs/MS_IN_MIN;
        return timeInMin;

    }

    public void convertToCartesian(){
        TracksHandler tracksHandler = plotterController.getTracksHandler();
        TrackPoint trackZero = tracksHandler.getTrack(0).getTrackPoint(0);
        for (int i = 0; i < tracksHandler.getTrackAmount(); i++){
            Track track = tracksHandler.getTrack(i);
            XYChart.Series series = new XYChart.Series();
            series.setName(track.getName());
            for(int z = 0; z < track.getPointAmount(); z++) {
                TrackPoint currentTrackPoint = track.getTrackPoint(z);
                double x = (RADIUS_OF_EARTH_M + ((trackZero.getElevation() + currentTrackPoint.getElevation())/2)) *
                        (trackZero.getLongitude()*DEG_TO_RAD - currentTrackPoint.getLongitude()*DEG_TO_RAD) *
                        Math.cos((trackZero.getLatitude()*DEG_TO_RAD + currentTrackPoint.getLatitude()*DEG_TO_RAD)/2);
                double y = (RADIUS_OF_EARTH_M + (trackZero.getElevation() + currentTrackPoint.getElevation())/2) *
                        (trackZero.getLatitude()*DEG_TO_RAD - currentTrackPoint.getLatitude()*DEG_TO_RAD);
                series.getData().add(new XYChart.Data(decimalFormat.format(x), y));
            }
            seriesArrayList.add(series);
            plotterController.graphTwoDPlot(series);
            plotterController.addTable(track.getTrackStats());
        }
    }
}
