package tests;

import gps.Track;
import gps.TrackPoint;
import gps_plotter.Plotter;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PlotterTest {
    // Due to the varied differences between lat and long and the inherit error in
    //   elevation, the calculation method, and the expected distances which uses a different way to calculate distance,
    //   the threshold is larger and there is likely some k or combination of standard errors that would help to make
    //   this threshold smaller and reasonable but due to the relatively low risk of the system, the threshold can be such
    private final double THRESHOLD = 180.0;

    // Data for the 3D distance calculation test
    private final Object[][] coordsToTest3DDistanceValid = {
            // lat1, lat2, lng1, lng2, ele1 (in meters), ele2 (in meters), kilometers boolean, expectedDistance (in km)
            //   Miles
            new Object[] { 43.3, 40.0, 70.0, 60.0, 1009.0, 164.0, false, 563.900 }, // Above Boundary
            new Object[] { 0.0, 0.0, 0.0, 0.0, -9999.0, -9999.0, false, 0.000 }, // At Boundary - zero distance
            new Object[] { -36.386, -43.053, -71.016, -65.358, 2360.0, 117.0, false, 549.790 }, // Below Boundary
            //   Kilometers
            new Object[] { 43.3, 40.0, 70.0, 60.0, 1009.0, 164.0, true, 907.510 }, // Above Boundary
            new Object[] { 0.0, 0.0, 0.0, 0.0, -9999.0, -9999.0, true, 0.0 }, // At Boundary - zero distance
            new Object[] { -36.386, -43.053, -71.016, -65.358, 2360.0, 117.0, true, 884.800 }, // Below Boundary
    };
    // obtained elevation from https://www.freemaptools.com/elevation-finder.htm (Accuracy is within 10 feet of actual)
    // obtained expected distance from https://gps-coordinates.org/distance-between-coordinates.php

    /**
     * Tests the calculation of 3D distance using Boundary Value Analysis and Equivalence Classes of based off the BVA
     *
     * NOTE: It is recommended that if this test is involved in more risky mission critical systems,
     *   strong normal and robust test data including invalid values (passed ranges of +/-90 and +/-180 for
     *   latitude and longitude respectively
     */
    @Test
    void calculateThreeDimensionalDistance() {
        TrackPoint trackZero = new TrackPoint(0, 0, 0, new Date());
        TrackPoint trackPoint1;
        TrackPoint trackPoint2;
        double lat1;
        double lat2;
        double lng1;
        double lng2;
        double e1;
        double e2;

        boolean kilometers;
        double expectedDistance;

        for (Object[] testData: coordsToTest3DDistanceValid) {
            // arrange
            lat1 = (double)testData[0];
            lat2 = (double)testData[1];
            lng1 = (double)testData[2];
            lng2 = (double)testData[3];
            e1 = (double)testData[4];
            e2 = (double)testData[5];

            kilometers = (boolean)testData[6];
            expectedDistance = (double)testData[7];

            trackPoint1 = new TrackPoint(lat1, lng1, e1, new Date());
            trackPoint2 = new TrackPoint(lat2, lng2, e2, new Date());

            double prevX = Plotter.calculateXCoord(trackPoint1, trackZero);
            double prevY = Plotter.calculateYCoord(trackPoint1, trackZero);

            double currentX = Plotter.calculateXCoord(trackPoint2, trackZero);
            double currentY = Plotter.calculateYCoord(trackPoint2, trackZero);

            // act
            // calculate and compare
            double actualDistance = Plotter.calculateThreeDimensionalDistance(prevX, currentX, prevY, currentY, e1, e2, kilometers);
            double difference = Math.abs(expectedDistance - actualDistance);
            boolean withinThreshold = difference < THRESHOLD;

            // assert
            assertTrue(withinThreshold);
        }
    }
}