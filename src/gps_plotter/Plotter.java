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
    private static final double RADIUS_OF_EARTH_M = 6371000;

    private static double MS_IN_MIN = 60000;

    private LineChart<Double, Double> chart;

    private PlotterController plotterController;

    /**
     * Makes plotter object and sets LineChart element from controller
     *
     * @param chart LineChart to modify
     */
    public Plotter(LineChart<Double, Double> chart, PlotterController plotterController){
        this.chart = chart;
        this.plotterController = plotterController;
    }

    /**
     * Plots elevation gain at each TrackPoint's date along the graph
     *
     * @param track track from which points will be plotted
     */
    public void plotElevationGain(Track track){

        if (track.getPointAmount() < 2) {
            throw new IllegalArgumentException("There must be at least 2 points in the track to plot Elevation Gain vs Time.");
        }

        XYChart.Series series = new XYChart.Series();
        series.setName(track.getName());
        setChartAxisLabels("Time Passed (min)", "Elevation Gain (m)");

        double highestElevation = track.getTrackPoint(0).getElevation();
        double elevationPoint = 0;
        Date firstDate = null;
        Date currentDate;
        double xMax = 0;
        double xMin = 0;
        double yMax = 0;
        double yMin = 0;

        for (int i = 0; i < track.getPointAmount(); i++) {

            TrackPoint currentTrackPoint = track.getTrackPoint(i);

            double currentElevation = currentTrackPoint.getElevation();

            //Set first date to calculate time passed
            if (i == 0) {
                firstDate = currentTrackPoint.getTime();
            }

            currentDate = currentTrackPoint.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            elevationPoint += calculateElevationGain(currentElevation, highestElevation); //Add the change in elevation to total change

            plotPoint(series, timePoint, elevationPoint); //Plot point on LineChart

            if (elevationPoint > 0) { //Only set highest elevation if gain is above 0
                highestElevation = currentTrackPoint.getElevation();
            }

        }

        this.chart.getData().add(series);


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

    /**
     * Clears current data series for chart
     */
    public void clearChart(){
        this.chart.getData().clear();
    }

    /**
     * Sets axis labels for chart
     *
     * @param x axis label
     * @param y axis label
     */
    private void setChartAxisLabels(String x, String y){
        this.plotterController.getXAxis().setLabel(x);
        this.plotterController.getYAxis().setLabel(y);
    }

    /**
     * Adds points to data series for LineChart
     *
     * @param series XYSeries to add data to
     * @param x x point
     * @param y y point
     */
    private void plotPoint(XYChart.Series series, double x, double y){
        series.getData().add(new XYChart.Data(x, y));
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

        if(this.chart.getData() != null && this.chart.getData().size() != 0){ //Clears graph when window is opened only if series exists
            clearChart();
            plotterController.clearTable();
        }

        TracksHandler tracksHandler = plotterController.getTracksHandler();
        if(tracksHandler != null) {
            setChartAxisLabels("Meters(east and west)", "Meters(north and south)");
            TrackPoint trackZero = tracksHandler.getTrack(0).getTrackPoint(0);
            for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
                Track track = tracksHandler.getTrack(i);
                XYChart.Series series = new XYChart.Series();
                series.setName(track.getName());
                for (int z = 0; z < track.getPointAmount(); z++) {
                    TrackPoint currentTrackPoint = track.getTrackPoint(z);
                    double x = (RADIUS_OF_EARTH_M + ((trackZero.getElevation() + currentTrackPoint.getElevation()) / 2)) *
                            (trackZero.getLongitude() * DEG_TO_RAD - currentTrackPoint.getLongitude() * DEG_TO_RAD) *
                            Math.cos((trackZero.getLatitude() * DEG_TO_RAD + currentTrackPoint.getLatitude() * DEG_TO_RAD) / 2);
                    double y = (RADIUS_OF_EARTH_M + (trackZero.getElevation() + currentTrackPoint.getElevation()) / 2) *
                            (trackZero.getLatitude() * DEG_TO_RAD - currentTrackPoint.getLatitude() * DEG_TO_RAD);
                    plotPoint(series, x, y);
                }
                this.chart.getData().add(series);
                plotterController.addTable(track.getTrackStats());
            }
        } else{
            throw new NullPointerException("No Tracks are Loaded!");
        }
    }
}
