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
import static org.junit.jupiter.api.Assertions.fail;

public class GPSTest2 {

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
        assertEquals(2500.0, ts.getMaxElev(), DELTA);
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
        assertEquals(-88.0, ts.getMinLong(), DELTA);

    }
    @Test
    public void testAvgSpeedK() {
        calculations();
        assertEquals(50.026730, ts.getAvgSpeedK(), DELTA);
    }
    @Test
    public void testAvgSpeedM() {
        calculations();
        assertEquals(31.085159, ts.getAvgSpeedM(), DELTA);

    }
    @Test
    public void testMaxSpeedK() {
        calculations();
        assertEquals(50.026730, ts.getMaxSpeedK(), 50.026730*DELTA);

    }
    @Test
    public void testMaxSpeedM() {
        calculations();
        assertEquals(31.085159, ts.getMaxSpeedM(), 31.085159*DELTA);

    }
    @Test
    public void testDistK() {
        calculations();
        assertEquals(8.260, ts.getDistK(), DELTA*8.260);

    }
    @Test
    public void testDistM() {
        calculations();
        assertEquals(5.132526, ts.getDistM(), DELTA*5.132526);

    }

    private void calculations() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateInString1 = "2016-02-10T13:00:00Z";
        String dateInString2 = "2016-02-10T13:10:00Z";
        ArrayList<TrackPoint> pList = new ArrayList<>();
        try {
        	// if your data format was "yyyy-MM-dd'T'HH:mm:ss'Z'", then you wouldn't have to replace the Z
           Date time1 = formatter.parse(dateInString1);
            Date time2 = formatter.parse(dateInString2);
            TrackPoint p1 = new TrackPoint(43.3, -87.9, 500.0, time1);
            TrackPoint p2 = new TrackPoint(43.3, -88.0, 2500.0, time2);
            pList.add(p1);
            pList.add(p2);
            Track t = new Track("GPSTest2", pList);
            TracksCalculator tc = new TracksCalculator();
            tc.calculateMetrics(t);
            ts = t.getTrackStats();
        } catch (ParseException pe) {
            fail(pe.getMessage());
            pe.printStackTrace();
        }
    }
}
