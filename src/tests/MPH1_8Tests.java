package tests;

import gps.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MPH1_8Tests {

    /**
     * tests the GPSSpeedTest.gpx file for correct colors. Should be Dark Blue,
     * Light Blue, Green, Yellow, Orange, Red.
     */
    @Test
    public void testGPSSpeedTest() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSSpeedTest.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS testSpeeds.gpx: Various speeds");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        //Calculated by calculating the distance and time between two points, then
        //calculating the speed in MPH, and finally choose the color based on what the speed was.
        Color[] expectedOutput = {Color.BLUE, Color.AQUA, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED};
        Color[] methodOutput = plotSpeedOverPath(gpxHandler.getTrackHandler());
        assertArrayEquals(expectedOutput, methodOutput);
    }

    /**
     * tests the MHP1-8_VariousSpeeds.gpx file for correct colors. Should be Green,
     * Red, Yellow, Light Blue, Light Blue, Red, Yellow, Yellow, Orange.
     */
    @Test
    public void testMHP18VariousSpeeds() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\MHP1-8_VariousSpeeds.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("MHP1-8 Various Speeds");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        //Calculated by calculating the distance and time between two points, then
        //calculating the speed in MPH, and finally choose the color based on what the speed was.
        Color[] expectedOutput = {Color.GREEN, Color.RED, Color.YELLOW, Color.AQUA, Color.AQUA, Color.RED, Color.YELLOW, Color.YELLOW, Color.ORANGE};
        Color[] methodOutput = plotSpeedOverPath(gpxHandler.getTrackHandler());
        assertArrayEquals(expectedOutput, methodOutput);
    }

    /**
     * tests the GPSTest10.gpx file for correct colors. As a constant speed GPS file,
     * should be Red, Red, Red, Red, Red, Red, Red, Red, Red.
     */
    @Test
    public void testGPSTest10() {
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest10.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS testSpeeds.gpx: 10 points. 73.9km");
        } catch (Exception e) {
            fail("Exception Thrown");
        }
        //Calculated by calculating the distance and time between two points, then
        //calculating the speed in MPH, and finally choose the color based on what the speed was.
        Color[] expectedOutput = {Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED};
        Color[] methodOutput = plotSpeedOverPath(gpxHandler.getTrackHandler());
        assertArrayEquals(expectedOutput, methodOutput);
    }

    /**
     * a modified version of the method in plotter to not use javafxml objects. returns an
     * array of colors, each Color representing what color a line between two points should be.
     *
     * @param tracksHandler the tracks handler that simulates the one gotten from plotterController
     * @return a double array of points to replace the adding to graph function
     */
    private Color[] plotSpeedOverPath(TracksHandler tracksHandler) {
        ArrayList<Color> array = new ArrayList<>();
        if (tracksHandler != null) {
            int index = 0;
            if (index != -1) {
                TrackPoint trackZero = tracksHandler.getTrack(index).getTrackPoint(0);
                //Color variable for future use.
                Color color;
                for (int i = 0; i < tracksHandler.getTrackAmount(); i++) {
                    Track track = tracksHandler.getTrack(i);
                    //Gets the instantaneous speeds for the track.
                    ArrayList<Double> speeds = track.getTrackStats().getSpeeds();
                    //Most of this method was dedicated to plotting the points correctly.
                    for (int z = 0; z < track.getPointAmount() - 1; z++) {
                        //Decides line color
                        color = setColor(speeds.get(z));
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