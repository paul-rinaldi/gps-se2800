package tests;

import gps.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestElevationFeet {

    @Test
    public void testGPS10MaxFeet() {
        //Arrange
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

        //Act
        double elevationFeet = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 10 points. 73.9km").getTrackStats().getMaxElevFt();
        double elevationMeters = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 10 points. 73.9km").getTrackStats().getMaxElevM();

        //Assert
        assertEquals(elevationMeters*3.2808, elevationFeet);
    }

    @Test
    public void testGPS1MinFeet() {
        //Arrange
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

        //Act
        double elevationFeet = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 10 points. 73.9km").getTrackStats().getMinElevFt();
        double elevationMeters = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 10 points. 73.9km").getTrackStats().getMinElevM();
        //Assert
        assertEquals(elevationMeters*3.2808, elevationFeet);
    }

    @Test
    public void testGPS2MaxFeet() {
        //Arrange
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest2.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS testSpeeds.gpx: 2 points. 8260 meters");
        } catch (Exception e) {
            fail("Exception Thrown");
        }

        //Act
        double elevationFeet = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 2 points. 8260 meters").getTrackStats().getMaxElevFt();
        double elevationMeters = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 2 points. 8260 meters").getTrackStats().getMaxElevM();
        //Assert
        assertEquals(elevationMeters*3.2808, elevationFeet);
    }

    @Test
    public void testGPS2MinFeet() {
        //Arrange
        GPXHandler gpxHandler = new GPXHandler();
        String filename = System.getProperty("user.dir") + "\\docs\\GPSTest2.gpx";
        Parser parser = null;
        try {
            parser = new Parser(gpxHandler);
            parser.parse(filename);
            gpxHandler.getTrackHandler().calculateTrackStats("GPS testSpeeds.gpx: 2 points. 8260 meters");
        } catch (Exception e) {
            fail("Exception Thrown");
        }

        //Act
        double elevationFeet = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 2 points. 8260 meters").getTrackStats().getMinElevFt();
        double elevationMeters = gpxHandler.getTrackHandler().getTrack("GPS testSpeeds.gpx: 2 points. 8260 meters").getTrackStats().getMinElevM();
        //Assert
        assertEquals(elevationMeters*3.2808, elevationFeet);
    }
}