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

public class GPSTest5Center {

    private static final double DELTA = 0.03;

    @Test
    public void test() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String dateInString = "2016-02-10T13:00:00Z";
        ArrayList<TrackPoint> pList = new ArrayList<>();
        try {
            Date d =formatter.parse(dateInString.replaceAll("Z$", "+0000"));
            TrackPoint p1 = new TrackPoint(43.3, -87.9, 500.0, d);
            pList.add(p1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        Track t = new Track("GPSTest1", pList);
        TracksCalculator tc = new TracksCalculator();
        tc.calculateMetrics(t);
        TrackStats ts = t.getTrackStats();
        assertEquals(43.4, ts.getMaxLat(), DELTA);
        assertEquals(43.3, ts.getMinLat(), DELTA);
        assertEquals(500.0, ts.getMaxElev(), DELTA);
        assertEquals(500.0, ts.getMinElev(), DELTA);
        assertEquals(-87.9, ts.getMaxLong(), DELTA);
        assertEquals(-88.0, ts.getMinLong(), DELTA);
        assertEquals(500.0, ts.getAvgSpeedK(), DELTA);
        assertEquals(500.0, ts.getAvgSpeedM(), DELTA);
        assertEquals(500.0, ts.getMaxSpeedK(), DELTA);
        assertEquals(500.0, ts.getMaxSpeedM(), DELTA);
        assertEquals(38.464, ts.getDistK(), DELTA);
        assertEquals(23.9, ts.getDistM(), DELTA);
    }
}
