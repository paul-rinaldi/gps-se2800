package tests;

import gps.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MPH1_15Tests {
    private static final double DEG_TO_RAD = 0.0174533;
    private static final double RADIUS_OF_EARTH_M = 6371000;

     /**
     * tests the GPSBellCurve.gpx file for correct colors. This file is meant to
      * test if all 7 colors appear, so the order should be Blue, Aqua, Green, Yellow, Orange, Red.
     */
    @Test
    public void testGPSBellCurve() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\MHP1-15 Test Files\\GPSBellCurve.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS Test: 10 points. 73.9km");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        //Calculated by calculating the distance and time between two points, then
        //calculating the speed in MPH, and finally choose the color based on what the speed was.
        Color[] expectedOutput = {Color.BLUE, Color.AQUA,Color.GREEN,Color.YELLOW, Color.ORANGE, Color.RED};
        Color[] methodOutput = plotGrade(gpxHandler.getTrackHandler());
        assertArrayEquals(expectedOutput, methodOutput);
    }

    /**
     * tests the GPSEvenElevation.gpx file for correct colors. Since this file always has a
     * constant slope, it should result in 9 greens.
     */
    @Test
    public void testGPSEvenElevation() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\MHP1-15 Test Files\\GPSEvenElevation.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS Test: 10 points. 73.9km");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        //Calculated by calculating the distance and time between two points, then
        //calculating the speed in MPH, and finally choose the color based on what the speed was.
        Color[] expectedOutput = {Color.GREEN, Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN};
        Color[] methodOutput = plotGrade(gpxHandler.getTrackHandler());
        assertArrayEquals(expectedOutput, methodOutput);
    }

     /**
     * tests the GPSTest10.gpx file for correct colors. Since this file for the first 4 sections rises,
      * then drops at the same rate, this should result in red, red ,red, red, blue, blue, blue, blue
     */
    @Test
    public void testGPSTest10() {
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
        //Calculated by calculating the distance and time between two points, then
        //calculating the speed in MPH, and finally choose the color based on what the speed was.
        Color[] expectedOutput = {Color.RED, Color.RED, Color.RED, Color.RED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};
        Color[] methodOutput = plotGrade(gpxHandler.getTrackHandler());
        assertArrayEquals(expectedOutput, methodOutput);
    }

    /**
     * a modified version of the method in plotter to not use javafxml objects. returns an
     * array of colors, each Color representing what color a line between two points should be.
     *
     * @param tracksHandler the tracks handler that simulates the one gotten from plotterController
     * @return a Color array of lines to replace the adding to graph function
     */
    private Color[] plotGrade(TracksHandler tracksHandler) {
        ArrayList<Color> array = new ArrayList<>();
        if (tracksHandler != null) {
            int index = 0;
            if (index != -1) {
                TrackPoint trackZero = tracksHandler.getTrack(index).getTrackPoint(0);
                //Color variable for future use.
                Color color;
                for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
                    Track track = tracksHandler.getTrack(i);
                    //Most of this method was dedicated to plotting the points correctly.
                    for (int z = 0; z < track.getPointAmount() - 1; z++) {

                        TrackPoint point1 = track.getTrackPoint(z);
                        TrackPoint point2 = track.getTrackPoint(z+1);

                        //Calculates the grade
                        double grade = calculateGrade(point1, point2);

                        //Decides line color, and adds to list.
                        color = setGradeColor(grade);
                        array.add(color);
                    }
                }
            }
        } else {
            throw new NullPointerException("No Tracks are Loaded!");
        }
        //Converts and returns the arraylist of colors
        Color[] colors = new Color[array.size()];
        return array.toArray(colors);
    }

    //Essentially calculates the grade between two points. Returns a number
    private double calculateGrade(TrackPoint point1, TrackPoint point2) {
        double elevationChange = point2.getElevation() - point1.getElevation();
        double distance = calculateDistance(point1, point2);

        return (elevationChange / distance) * 100.0;
    }

    //Calculates the distance between two points in terms of x & y.
    private double calculateDistance(TrackPoint point1, TrackPoint point2) {
        double deltaX = (RADIUS_OF_EARTH_M + ((point2.getElevation() + point1.getElevation()) / 2)) *
                (point2.getLongitude() * DEG_TO_RAD - point1.getLongitude() * DEG_TO_RAD) *
                Math.cos((point2.getLatitude() * DEG_TO_RAD + point1.getLatitude() * DEG_TO_RAD) / 2);
        double deltaY = (RADIUS_OF_EARTH_M + (point2.getElevation() + point1.getElevation()) / 2) *
                (point2.getLatitude() * DEG_TO_RAD - point1.getLatitude() * DEG_TO_RAD);

        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        return distance;
    }

    //Method to select which color to represent grade with
    private Color setGradeColor(Double grade) {
        //Dark blue- any grade less than -5% Grade
        if (grade < -5) {
            return Color.BLUE;
        }
        //Light blue- any grade that's >= -5% and < than -1%.
        else if (grade >= -5 && grade < -1) {
            return Color.AQUA;
        }
        //Green- any grade that's >=-1% MPH and < than 1%.
        else if (grade >= -1 && grade < 1) {
            return Color.GREEN;
        }
        //Yellow- any grade that's >= 1% MPH and < 3%.
        else if (grade >= 1 && grade < 3) {
            return Color.YELLOW;
        }
        //Orange- any grade that's >= 3% MPH and < than 5%.
        else if (grade >= 3 && grade < 5) {
            return Color.ORANGE;
        }
        //Red- Any grade that's over or equal to 5% Grade.
        else {
            return Color.RED;
        }
    }
}
