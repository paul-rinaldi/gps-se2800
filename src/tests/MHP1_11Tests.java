package tests;

import gps.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MHP1_11Tests {
    //the amount that each result can be off by
    private static final double DELTA = 0.03;
    private static final double DEG_TO_RAD = 0.0174533;
    private static final double RADIUS_OF_EARTH_M = 6371000;

    /**
     * tests the GPXtest10.gpx file for correct plot coordinates
     */
    @Test
    public void testTenPointGraphValue() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest10.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS Test: 10 points. 73.9km");
        } catch (Exception e) {
            fail("Exception Thrown");
        }

        //All these values are with respect to the first point and are in meters
        //Taken from the GPSTest10.gpx file and converted using https://www.geodatasource.com/distance-calculator
        double[] xMeters = {0, 8090, 16180, 24280, 32370, 40460, 48550, 56640, 64740, 72830};
        double[] yMeters = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[][] simulatedPoints = convertToCartesian(gpxHandler.getTrackHandler());
        for(int i = 0; i < xMeters.length; i ++){
            assertEquals(xMeters[i], simulatedPoints[i][0], xMeters[i]*DELTA);
            assertEquals(yMeters[i], simulatedPoints[i][1], yMeters[i]*DELTA);
        }
    }

    /**
     * tests the file GPSTest2.gpx file for correct plot coordinates
     */
    @Test
    public void testTwoPointGraphValue() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest2.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS Test: 2 points. 8260 meters");
        } catch (Exception e) {
            fail("Exception Thrown");
        }

        //All these values are with respect to the first point and are in meters
        //Taken from the GPSTest2.gpx file and converted using https://www.geodatasource.com/distance-calculator
        double[] xMeters = {0, 8090};
        double[] yMeters = {0, 0};
        double[][] simulatedPoints = convertToCartesian(gpxHandler.getTrackHandler());
        for(int i = 0; i < xMeters.length; i ++){
            assertEquals(xMeters[i], simulatedPoints[i][0], xMeters[i]*DELTA);
            assertEquals(yMeters[i], simulatedPoints[i][1], yMeters[i]*DELTA);
        }
    }

    /**
     * tests the file constantLong.gpx has a constant longitude but varying latitude for for correct plot coordinates
     */
    @Test
    public void testConstantLong(){
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\constantLong.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("constant longitude varying latitude");
        } catch (Exception e) {
            fail("Exception Thrown");
        }

        //All these values are with respect to the first point and are in meters
        //Taken from the constantLong.gpx file and converted using https://www.geodatasource.com/distance-calculator
        double[] xMeters = {0, 0, 0, 0};
        double[] yMeters = {0, -11120, -22240, -44480};
        double[][] simulatedPoints = convertToCartesian(gpxHandler.getTrackHandler());
        for(int i = 0; i < xMeters.length; i ++){
            assertEquals(xMeters[i], simulatedPoints[i][0], Math.abs(xMeters[i]*DELTA));
            assertEquals(yMeters[i], simulatedPoints[i][1], Math.abs(yMeters[i]*DELTA));
        }
    }

    /**
     * checks to make sure that the table dispalys the correct result for kilometers, it takes values
     * calculated by the tracksCalculator so it doesn't need many tests because tracksCalculator was already tested
     */
    @Test
    public void testTableGPX10Km(){
        //given from track name
        double distanceKm = 73.9;
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest10.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS Test: 10 points. 73.9km");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        double result = gpxHandler.getTrackHandler().getTrack("GPS Test: 10 points. 73.9km").getTrackStats().getDistK();
        assertEquals(distanceKm, result, distanceKm*DELTA);
    }

    /**
     * checks to make sure that the table dispalys the correct result for miles, it takes values
     * calculated by the tracksCalculator so it doesn't need many tests because tracksCalculator was already tested
     */
    @Test
    public void testTableGPX10Mi(){
        //given from track name and converting to miles using google km to mi converter
        double distanceMi = 45.91933;
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest10.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS Test: 10 points. 73.9km");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        double result = gpxHandler.getTrackHandler().getTrack("GPS Test: 10 points. 73.9km").getTrackStats().getDistM();
        assertEquals(distanceMi, result, distanceMi*DELTA);
    }

    /**
     * a slightly modified version of the method in the Plotter class. modified to not use javafx items and to return
     * a double array of points instead of adding them to the graph
     * @param tracksHandler the tracks handler that simulates the one gotten from plotterController
     * @return a double array of points to replace the adding to graph function
     */
    private double[][] convertToCartesian(TracksHandler tracksHandler){
        double[][] listOfPoints = new double[10][2];
        if(tracksHandler != null) {
            int index = 0;
            if (index != -1) {
                TrackPoint trackZero = tracksHandler.getTrack(index).getTrackPoint(0);
                for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
                    Track track = tracksHandler.getTrack(i);
                    for (int z = 0; z < track.getPointAmount(); z++) {
                        TrackPoint currentTrackPoint = track.getTrackPoint(z);
                        double x = (RADIUS_OF_EARTH_M + ((trackZero.getElevation() + currentTrackPoint.getElevation()) / 2)) *
                                (trackZero.getLongitude() * DEG_TO_RAD - currentTrackPoint.getLongitude() * DEG_TO_RAD) *
                                Math.cos((trackZero.getLatitude() * DEG_TO_RAD + currentTrackPoint.getLatitude() * DEG_TO_RAD) / 2);
                        double y = (RADIUS_OF_EARTH_M + (trackZero.getElevation() + currentTrackPoint.getElevation()) / 2) *
                                (trackZero.getLatitude() * DEG_TO_RAD - currentTrackPoint.getLatitude() * DEG_TO_RAD);
                        listOfPoints[z][0] = x;
                        listOfPoints[z][1] = y;
                    }
                }
            }
        } else{
            throw new NullPointerException("No Tracks are Loaded!");
        }
        return listOfPoints;
    }
}
