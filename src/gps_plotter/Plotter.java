package gps_plotter;


import gps.*;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Date;

/**
 * Handles plotting tasks for the PlotterController
 */
public class Plotter {

    private static final double DEG_TO_RAD = 0.0174533;
    private static final double RADIUS_OF_EARTH_M = 6371000;

    private static final double MS_IN_MIN = 60000;
    private static final double M_IN_KM = 1000;
    private static final double M_IN_MI = 1609.344;

    //used to scale the graph
    private int xMax;
    private int xMin;
    private int yMax;
    private int yMin;

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
     * Plots track's points as calories expended vs time
     *
     * @param track Track to plot
     */
    public void plotCaloriesExpendedVsT(Track track){
        XYChart.Series series = new XYChart.Series();
        series.setName(track.getName());

        setChartAxisLabels("Time Passed (min)", "Calories Expended");

        TrackPoint trackPointZero = track.getTrackPoint(0);
        Date firstDate = null;
        Date currentDate;

        double caloriesExpended = 0;

        for (int i = 0; i < track.getPointAmount(); i++) {

            TrackPoint currentPoint = track.getTrackPoint(i);
            TrackPoint previousPoint;
            double currentElevation = currentPoint.getElevation();
            double previousElevation;

            //Set first date to calculate time passed
            if (i == 0) {
                firstDate = currentPoint.getTime();
                previousPoint = track.getTrackPoint(i);
                previousElevation = currentPoint.getElevation();
            } else{
                previousPoint = track.getTrackPoint(i-1);
                previousElevation = previousPoint.getElevation();
            }

            //Get x and y values
            double prevX = calculateXCoord(previousPoint, trackPointZero);
            double prevY = calculateYCoord(previousPoint, trackPointZero);
            double currentX = calculateXCoord(currentPoint, trackPointZero);
            double currentY = calculateYCoord(currentPoint, trackPointZero);

            //Find point of time to plot
            currentDate = currentPoint.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            //Calculate calories expended
            double distance = calculateTwoDDistance(prevX, currentX, prevY, currentY);
            double elevationGain = calculateElevationGain(currentElevation, previousElevation);

            caloriesExpended+= calculateCaloriesExpended(distance, elevationGain);

            plotPoint(series, timePoint, caloriesExpended); //Plot point on LineChart

        }
        this.chart.getData().add(series);
    }

    public double calculateCaloriesExpended(double distance, double elevationGain){
        //Set values
        double caloriesPerFifteenKmPerHour = 1000;
        double caloriesPerMeterElevationGain = 2;

        //Calculate
        double distanceRatio = distance/15;
        double distanceCalories = distanceRatio * caloriesPerFifteenKmPerHour;
        double elevationCalories = elevationGain * caloriesPerMeterElevationGain;

        return (distanceCalories + elevationCalories);

    }

    private double calculateTwoDDistance(double x1, double x2, double y1, double y2){

        //Convert meters to kilometers
        x1/=M_IN_KM;
        x2/=M_IN_KM;
        y1/=M_IN_KM;
        y2/=M_IN_KM;

        double xComponent = (x2 - x1)*(x2 - x1);
        double yComponent = (y2 - y1)*(y2 - y1);

        double distance = Math.sqrt(xComponent + yComponent);
        return distance;
    }

    /**
     * Plots graph of distance vs time
     *
     * @param track track to plot
     * @param kilometers determines whether the distance displayed/calculated is in miles or kilometers (true: kilometers; false: miles)
     */
    public void plotDistanceVsTime(Track track, boolean kilometers){
        XYChart.Series series = new XYChart.Series();
        series.setName(track.getName());
        if(kilometers) {
            setChartAxisLabels("Time Passed (min)", "Distance (km)");
        } else{
            setChartAxisLabels("Time Passed (min)", "Distance (mi)");
        }

        TrackPoint trackPointZero = track.getTrackPoint(0);
        Date firstDate = null;
        Date currentDate;
        double distanceTraveled = 0;

        for (int i = 0; i < track.getPointAmount(); i++) {


            TrackPoint currentPoint = track.getTrackPoint(i);
            TrackPoint previousPoint;
            double currentElevation = currentPoint.getElevation();
            double previousElevation;
            //Set first date to calculate time passed
            if (i == 0) {
                firstDate = currentPoint.getTime();
                previousPoint = track.getTrackPoint(i);
                previousElevation = currentPoint.getElevation();
            } else{
                previousPoint = track.getTrackPoint(i-1);
                previousElevation = previousPoint.getElevation();
            }

            double prevX = calculateXCoord(previousPoint, trackPointZero);
            double prevY = calculateYCoord(previousPoint, trackPointZero);

            double currentX = calculateXCoord(currentPoint, trackPointZero);
            double currentY = calculateYCoord(currentPoint, trackPointZero);

            currentDate = currentPoint.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            double currentDistance = calculateThreeDimensionalDistance(prevX, currentX, prevY, currentY, previousElevation, currentElevation, kilometers);

            distanceTraveled += currentDistance;

            plotPoint(series, timePoint, distanceTraveled); //Plot point on LineChart

        }
        this.chart.getData().add(series);
    }

    private double calculateThreeDimensionalDistance(double x1, double x2, double y1, double y2, double z1, double z2, boolean kilometers){

        double divisor = kilometers ? M_IN_KM: M_IN_MI;

        //Convert distances to proper units (miles or kilometers)
        x1/=divisor;
        x2/=divisor;
        y1/=divisor;
        y2/=divisor;
        z1/=divisor;
        z2/=divisor;

        double xComponent = (x2 - x1)*(x2 - x1);
        double yComponent = (y2 - y1)*(y2 - y1);
        double zComponent = (z2 - z1)*(z2 - z1);

        double distance = Math.sqrt(xComponent + yComponent + zComponent);
        return distance;

    }


    /**
     * Plots elevation at each TrackPoint's date along the graph
     *
     * @param track track from which points will be plotted
     * @return elevation gain from track
     */
    public double plotElevationVsTime(Track track){
        XYChart.Series series = new XYChart.Series();
        series.setName(track.getName());
        setChartAxisLabels("Time Passed (min)", "Elevation (m)");

        Date firstDate = null;
        Date currentDate;

        TrackPoint previousPoint = track.getTrackPoint(0);

        double elevationGain = 0;

        int i = 0;
        for (TrackPoint point: track.getTrackPoints()) {

            //Set first date to calculate time passed
            if (i == 0) {
                firstDate = point.getTime();
            } else{
                previousPoint = track.getTrackPoint(i-1);
            }

            currentDate = point.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            double currentElevation = point.getElevation();
            double previousElevation = previousPoint.getElevation();

            elevationGain += calculateElevationGain(currentElevation, previousElevation);

            plotPoint(series, timePoint, currentElevation); //Plot point on LineChart

            i++;
        }
        this.chart.getData().add(series);
        return elevationGain;
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

        double previousElevation = track.getTrackPoint(0).getElevation();
        double elevationPoint = 0;
        Date firstDate = null;
        Date currentDate;

        int i = 0;
        for (TrackPoint point: track.getTrackPoints()) {

            double currentElevation = point.getElevation();

            //Set first date to calculate time passed
            if (i == 0) {
                firstDate = point.getTime();
            }

            currentDate = point.getTime();
            double timePoint = timePassedInMin(currentDate, firstDate);

            double elevationGain = calculateElevationGain(currentElevation, previousElevation);
            elevationPoint += elevationGain; //Add the change in elevation to total change

            plotPoint(series, timePoint, elevationPoint); //Plot point on LineChart

            previousElevation = point.getElevation();

            i++;
        }
        this.chart.getData().add(series);
    }

    /**
     * Returns the gain in elevation from the previous elevation
     *
     * @param current current elevation
     * @param previous previous elevation
     * @return difference between them; 0 if difference is < 0
     */
    public double calculateElevationGain(double current, double previous) {

        double result = current - previous;

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
        resetMinMax();
        //Clears the graph when window opens and a series exists.
        checkGraph();
        //Brings up the alternative legend.
        plotterController.setLegendTextVisible(true);
        plotterController.setLegendText("Dark Blue = < 3 MPH     Light Blue = Between 3 & 7 MPH     Green = Between 7 & 10 MPH" +
                "\nYellow = Between 10 & 15 MPH     Orange = Between 15 & 20 MPH     Red = Over 20 MPH");
        //Set chart name
        plotterController.setChartTitle("Instantaneous Speed Along Path");
        //Gets track handler, which holds all the tracks to be found.
        TracksHandler tracksHandler = plotterController.getTracksHandler();
        //Configures axises if there are tracks.
        if (tracksHandler != null) {
            setChartAxisLabels("Kilometer(east and west)", "Kilometers(north and south)");
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
                            double x = calculateXCoord(currentTrackPoint, trackZero)/M_IN_KM;
                            double y = calculateYCoord(currentTrackPoint, trackZero)/M_IN_KM;
                            plotPoint(series, x, y);
                            //Second point
                            TrackPoint nextTrackPoint = track.getTrackPoint(z + 1);
                            x = calculateXCoord(nextTrackPoint, trackZero)/M_IN_KM;
                            y = calculateYCoord(nextTrackPoint, trackZero)/M_IN_KM;
                            checkMinMax(x, y);
                            plotPoint(series, x, y);
                            //Adds the series to the chart
                            chart.getData().add(series);
                            //Gets the node property for the line of the newly created series.
                            Node line = series.getNode().lookup(".chart-series-line");

                            //Decides line color
                            color = setColor(speeds.get(z));

                            //Sets the line color for the series.
                            line.setStyle("-fx-stroke: rgb(" + rgbFormat(color) + ");");
                        }
                        plotterController.addTable(track.getTrackStats());
                    }
                }
                chart.setLegendVisible(false);
            }
        } else {
            throw new NullPointerException("No Tracks are Loaded!");
        }
        plotterController.scaleAxis(xMax, xMin, yMax, yMin);
    }


    /**
     * converts all selected tracks to cartesian coordinates to be displayed on the graph and on the chart
     * when the graph 2D plot button is pressed
     *
     * @throws throws a null pointer exception when this is called and no tracks have been loaded
     */
    public void convertToCartesian() throws NullPointerException {
        resetMinMax();
        chart.axisSortingPolicyProperty().setValue(LineChart.SortingPolicy.NONE);
        //Clears the graph when window opens and a series exists.
        checkGraph();

        plotterController.reenableLegend();
        plotterController.setChartTitle("Cartesian Coordinates");
        TracksHandler tracksHandler = plotterController.getTracksHandler();
        if (tracksHandler != null) {
            setChartAxisLabels("Kilometers(east and west)", "Kilometers(north and south)");
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
                            double x = calculateXCoord(currentTrackPoint, trackZero)/M_IN_KM;
                            double y = calculateYCoord(currentTrackPoint, trackZero)/M_IN_KM;
                            checkMinMax(x, y);
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
        plotterController.scaleAxis(xMax, xMin, yMax, yMin);
    }

    private void resetMinMax() {
        xMax = 0;
        xMin = 0;
        yMax = 0;
        yMin = 0;
    }

    private void checkMinMax(double xCheck, double yCheck) {
        //multiply by 1.1 to scale the axis so the bounds are not right on the edge of the graph
        int x = (int) Math.round(xCheck * 1.05);
        int y = (int) Math.round(yCheck * 1.05);
        if (x > xMax){
            xMax = x;
        } else if(x < xMin){
            xMin = x;
        }
        if (y > yMax){
            yMax = y;
        } else if(y < yMin){
            yMin = y;
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

    private double calculateXCoord(TrackPoint currentTrackPoint, TrackPoint trackZero) {
        return (RADIUS_OF_EARTH_M + ((trackZero.getElevation() + currentTrackPoint.getElevation()) / 2)) *
                (trackZero.getLongitude() * DEG_TO_RAD - currentTrackPoint.getLongitude() * DEG_TO_RAD) *
                Math.cos((trackZero.getLatitude() * DEG_TO_RAD + currentTrackPoint.getLatitude() * DEG_TO_RAD) / 2) * -1;
    }

    private double calculateYCoord(TrackPoint currentTrackPoint, TrackPoint trackZero) {
        return (RADIUS_OF_EARTH_M + (trackZero.getElevation() + currentTrackPoint.getElevation()) / 2) *
                (trackZero.getLatitude() * DEG_TO_RAD - currentTrackPoint.getLatitude() * DEG_TO_RAD) * -1;
    }

    //Format color object to string.
    private String rgbFormat(Color color) {
        return String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    //Method to select which color to represent speed with
    private Color setColor(Double speed) {
        //Dark blue- any speed less than 3 MPH
        if (speed < 3) {
            return Color.BLUE;
        }
        //Light blue- any speed that's >= 3 MPH and < than 7MPH.
        else if (speed >= 3 && speed < 7) {
            return Color.AQUA;
        }
        //Green- any speed that's >= 7 MPH and < than 10MPH.
        else if (speed >= 7 && speed < 10) {
            return Color.GREEN;
        }
        //Yellow- any speed that's >= 10 MPH and < than 15MPH.
        else if (speed >= 10 && speed < 15) {
            return Color.YELLOW;
        }
        //Orange- any speed that's >= 15 MPH and < than 15MPH.
        else if (speed >= 15 && speed < 20) {
            return Color.ORANGE;
        }
        //Red- Any speed that's over or equal to 20 MPH.
        else {
            return Color.RED;
        }
    }
}

