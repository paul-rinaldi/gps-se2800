package gps_plotter;


import gps.*;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Date;

/**
 * Handles plotting tasks for the PlotterController
 */
public class Plotter {

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
    public Plotter(LineChart<Double, Double> chart, PlotterController plotterController) {
        this.chart = chart;
        this.plotterController = plotterController;
    }

    /**
     * Plots elevation gain at each TrackPoint's date along the graph
     *
     * @param track track from which points will be plotted
     */
    public void plotElevationGain(Track track) {
        XYChart.Series series = new XYChart.Series();
        series.setName(track.getName());
        setChartAxisLabels("Time Passed (min)", "Elevation Gain (m)");

        double highestElevation = track.getTrackPoint(0).getElevation();
        double elevationPoint = 0;
        Date firstDate = null;
        Date currentDate;

        for (int i = 0; i < track.getPointAmount(); i++) {

            TrackPoint currentTrackPoint = track.getTrackPoint(i);

            double currentElevation = currentTrackPoint.getElevation();

            //Set first date to calculate time passed
            if (i == 0) {
                firstDate = currentTrackPoint.getTime();
            }

            currentDate = currentTrackPoint.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            double elevationGain = calculateElevationGain(currentElevation, highestElevation);
            elevationPoint += elevationGain; //Add the change in elevation to total change

            plotPoint(series, timePoint, elevationPoint); //Plot point on LineChart

            if (elevationGain > 0.0) { //Only set highest elevation if gain is above 0
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
    public double calculateElevationGain(double current, double highest) {

        if (current < 0.0 || highest < 0.0) { //Values below zero will not be expected (below sea level)
            return 0.0;
        }

        double result = current - highest;

        return result > 0 ? result : 0;

    }


    /**
     * Clears current data series for chart
     */
    public void clearChart() {
        this.chart.getData().clear();
    }

    /**
     * Sets axis labels for chart
     *
     * @param x axis label
     * @param y axis label
     */
    private void setChartAxisLabels(String x, String y) {
        this.plotterController.getXAxis().setLabel(x);
        this.plotterController.getYAxis().setLabel(y);
    }

    /**
     * Adds points to data series for LineChart
     *
     * @param series XYSeries to add data to
     * @param x      x point
     * @param y      y point
     */
    private void plotPoint(XYChart.Series series, double x, double y) {
        series.getData().add(new XYChart.Data(x, y));
    }

    /**
     * Takes two Date objects and calculates the minutes passed between them
     *
     * @param current current Date object
     * @param first   first Date object from which time has passed
     * @return time passed in minutes
     */
    public double timePassedInMin(Date current, Date first) {
        long differenceMs = current.getTime() - first.getTime();
        double timeInMin = differenceMs / MS_IN_MIN;
        return timeInMin;

    }

    /**
     * converts all selected tracks to different colored lines representing
     * instantaneous speeds at that point when the graph 2D plot button is pressed
     *
     * @throws throws a null pointer exception when this is called and no tracks have been loaded
     */
    public void plotSpeedOverPath() throws NullPointerException {
        //Clears the graph when window opens and a series exists.
        checkGraph();
        //Gets track handler, which holds all the tracks to be found.
        TracksHandler tracksHandler = plotterController.getTracksHandler();
        //Configures axises if there are tracks.
        if (tracksHandler != null) {
            setChartAxisLabels("Meters(east and west)", "Meters(north and south)");
            int index = getFirstLoadedIndex();
            boolean[] showOnGraph = plotterController.getShowOnGraph();

            //If there is a track selected...
            if (index != -1) {
                //Loads the first track's first point.
                TrackPoint trackZero = tracksHandler.getTrack(index).getTrackPoint(0);
                //Color variable for future use.
                Color color;
                for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
                    if (showOnGraph[i]) {
                        Track track = tracksHandler.getTrack(i);
                        //Gets the instantaneous speeds for the track. 
                        ArrayList<Double> speeds = track.getTrackStats().getSpeeds();
                        for (int z = 0; z < track.getPointAmount() - 1; z++) {
                            XYChart.Series series = new XYChart.Series();
                            series.setName(track.getName() + " " + z);
                            //First point
                            TrackPoint currentTrackPoint = track.getTrackPoint(z);
                            double x = calculateXCoord(currentTrackPoint, trackZero);
                            double y = calculateYCoord(currentTrackPoint, trackZero);
                            plotPoint(series, x, y);
                            //Second point
                            TrackPoint nextTrackPoint = track.getTrackPoint(z + 1);
                            x = calculateXCoord(nextTrackPoint, trackZero);
                            y = calculateYCoord(nextTrackPoint, trackZero);
                            plotPoint(series, x, y);
                            //Adds the series to the chart
                            this.chart.getData().add(series);
                            //Gets the node property for the line of the newly created series.
                            Node line = series.getNode().lookup(".chart-series-line");

                            //This section is for deciding which color the line should be based on instantaneous speed.
                            //Dark blue- Anything less than 3 MPH
                            if (speeds.get(z) < 3) {
                                color = Color.BLUE;
                            }
                            //Light blue- any speed that's between 3 MPH and 7MPH, or equals 3 MPH.
                            else if (speeds.get(z) >= 3 && speeds.get(z) < 7) {
                                color = Color.AQUA;
                            }
                            //Green- any speed that's between 3 MPH and 7MPH, or equals 3 MPH.
                            else if (speeds.get(z) >= 7 && speeds.get(z) < 10) {
                                color = Color.GREEN;
                            }
                            //Yellow- any speed that's between 3 MPH and 7MPH, or equals 3 MPH.
                            else if (speeds.get(z) >= 10 && speeds.get(z) < 15) {
                                color = Color.YELLOW;
                            }
                            //Orange- any speed that's between 3 MPH and 7MPH, or equals 3 MPH.
                            else if (speeds.get(z) >= 15 && speeds.get(z) <= 20) {
                                color = Color.ORANGE;
                            }
                            //Red- Any speed over 20 MPH.
                            else {
                                color = Color.RED;
                            }
                            //Sets the line color for the series.
                            line.setStyle("-fx-stroke: rgba(" + rgbFormat(color) + ", 1.0);");
                        }
                        plotterController.addTable(track.getTrackStats());
                    }
                }
            }
        } else {
            throw new NullPointerException("No Tracks are Loaded!");
        }
    }


    /**
     * converts all selected tracks to cartesian coordinates to be displayed on the graph and on the chart
     * when the graph 2D plot button is pressed
     *
     * @throws throws a null pointer exception when this is called and no tracks have been loaded
     */
    public void convertToCartesian() throws NullPointerException {
        //Clears the graph when window opens and a series exists.
        checkGraph();
        TracksHandler tracksHandler = plotterController.getTracksHandler();
        if (tracksHandler != null) {
            setChartAxisLabels("Meters(east and west)", "Meters(north and south)");
            int index = getFirstLoadedIndex();
            boolean[] showOnGraph = plotterController.getShowOnGraph();
            if (index != -1) {
                TrackPoint trackZero = tracksHandler.getTrack(index).getTrackPoint(0);
                for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
                    if (showOnGraph[i]) {
                        Track track = tracksHandler.getTrack(i);
                        XYChart.Series series = new XYChart.Series();
                        series.setName(track.getName());
                        for (int z = 0; z < track.getPointAmount(); z++) {
                            TrackPoint currentTrackPoint = track.getTrackPoint(z);
                            double x = calculateXCoord(currentTrackPoint, trackZero);
                            double y = calculateYCoord(currentTrackPoint, trackZero);
                            plotPoint(series, x, y);
                        }
                        this.chart.getData().add(series);
                        plotterController.addTable(track.getTrackStats());
                    }
                }
            }
        } else {
            throw new NullPointerException("No Tracks are Loaded!");
        }
    }

    private int getFirstLoadedIndex() {
        int returnValue = -1;
        boolean[] temp = plotterController.getShowOnGraph();
        TracksHandler tracksHandler = plotterController.getTracksHandler();
        for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
            if (temp[i]) {
                returnValue = i;
                i = 10; //breaks the loop
            }
        }
        return returnValue;
    }

    private void checkGraph() {
        if (this.chart.getData() != null && this.chart.getData().size() != 0) { //Clears graph when window is opened only if series exists
            clearChart();
            plotterController.clearTable();
        }
    }

    //
    private double calculateXCoord(TrackPoint currentTrackPoint, TrackPoint trackZero) {
        return (RADIUS_OF_EARTH_M + ((trackZero.getElevation() + currentTrackPoint.getElevation()) / 2)) *
                (trackZero.getLongitude() * DEG_TO_RAD - currentTrackPoint.getLongitude() * DEG_TO_RAD) *
                Math.cos((trackZero.getLatitude() * DEG_TO_RAD + currentTrackPoint.getLatitude() * DEG_TO_RAD) / 2);
    }

    private double calculateYCoord(TrackPoint currentTrackPoint, TrackPoint trackZero) {
        return (RADIUS_OF_EARTH_M + (trackZero.getElevation() + currentTrackPoint.getElevation()) / 2) *
                (trackZero.getLatitude() * DEG_TO_RAD - currentTrackPoint.getLatitude() * DEG_TO_RAD);
    }

    private String rgbFormat(Color color) {
        return String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void setLegend(){

    }
}
