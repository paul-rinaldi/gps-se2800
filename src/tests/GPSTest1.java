package tests;

import gps.Track;
import gps.TrackPoint;
import gps.TrackStats;
import gps.TracksCalculator;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GPSTest1 {

    private static final double DELTA = 0.03;
    private TrackStats ts;
    private double maxLat;
    private double minLat;
    private double maxLong;
    private double minLong;
    private double maxElev;
    private double minElev;
    private Track t;

    @Test
    public void testMaxLat() {
        calculations();
        assertEquals(43.3, maxLat, DELTA);
    }
    @Test
    public void testMinLat() {
        calculations();
        assertEquals(43.3, minLat, DELTA);
    }
    @Test
    public void testMaxElev() {
        calculations();
        assertEquals(500.0, maxElev, DELTA);
    }
    @Test
    public void testMinElev() {
        calculations();
        assertEquals(500.0, minElev, DELTA);

    }
    @Test
    public void testMaxLong() {
        calculations();
        assertEquals(-87.9, maxLong, DELTA);

    }
    @Test
    public void testMinLong() {
        calculations();
        assertEquals(-87.9, minLong, DELTA);

    }

    @Test
    public void testUnsupportedOperationExceptionThrown() {
        UnsupportedOperationException uoe = assertThrows(UnsupportedOperationException.class, () -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String dateInString1 = "2016-02-10T13:00:00Z";
            ArrayList<TrackPoint> pList = new ArrayList<>();
            try {
                Date time1 = formatter.parse(dateInString1.replaceAll("Z$", "+0000"));
                TrackPoint p1 = new TrackPoint(43.3, -87.9, 500.0, time1);
                pList.add(p1);
                Track t = new Track("GPSTest2", pList);
                TracksCalculator tc = new TracksCalculator();
                tc.calculateMetrics(t);
                ts = t.getTrackStats();
                maxLat = ts.getMaxLat();
                minLat = ts.getMinLat();
                maxLong = ts.getMaxLong();
                minLong = ts.getMinLong();
                maxElev = ts.getMaxElevM();
                minElev = ts.getMinElevM();
            } catch (ParseException pe) {
                // fail() should be called here; if an exception is thrown, something is wrong and the test should fail
            	pe.printStackTrace();
            }
        });
        assertEquals("Track only has one point", uoe.getMessage());
    }

    private void calculations() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String dateInString1 = "2016-02-10T13:00:00Z";
        ArrayList<TrackPoint> pList = new ArrayList<>();
        try {
        	// if your data format was "yyyy-MM-dd'T'HH:mm:ss'Z'", then you wouldn't have to replace the Z
            Date time1 = formatter.parse(dateInString1.replaceAll("Z$", "+0000"));
            TrackPoint p1 = new TrackPoint(43.3, -87.9, 500.0, time1);
            pList.add(p1);
            t = new Track("GPSTest2", pList);
            TracksCalculator tc = new TracksCalculator();
            tc.calculateMetrics(t);
        } catch (ParseException pe) {
            pe.printStackTrace();
            // fail() should be called here; if an exception is thrown, something is wrong and the test should fail
        } catch (UnsupportedOperationException uoe) {
        	// this exception is expected due to 1 point?
            ts = t.getTrackStats();
            maxLat = ts.getMaxLat();
            minLat = ts.getMinLat();
            maxLong = ts.getMaxLong();
            minLong = ts.getMinLong();
            maxElev = ts.getMaxElevM();
            minElev = ts.getMinElevM();
        }
    }
}
