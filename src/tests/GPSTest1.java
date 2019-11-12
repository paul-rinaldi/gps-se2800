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

public class GPSTest1 {

    private static final double DELTA = 0.03;
    private TrackStats ts;

    @Test
    public void testMaxLat() {
        calculations();
        assertEquals(43.3, ts.getMaxLat(), DELTA);
    }
    @Test
    public void testMinLat() {
        calculations();
        assertEquals(43.3, ts.getMinLat(), DELTA);
    }
    @Test
    public void testMaxElev() {
        calculations();
        assertEquals(500.0, ts.getMaxElev(), DELTA);
    }
    @Test
    public void testMinElev() {
        calculations();
        assertEquals(500.0, ts.getMinElev(), DELTA);

    }
    @Test
    public void testMaxLong() {
        calculations();
        assertEquals(-87.9, ts.getMaxLong(), DELTA);

    }
    @Test
    public void testMinLong() {
        calculations();
        assertEquals(-87.9, ts.getMinLong(), DELTA);

    }
    private void calculations() {
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
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }
}
