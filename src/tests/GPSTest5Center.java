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

public class GPSTest5Center {

    private static final double DELTA = 0.03;
    private TrackStats ts;

    @Test
    public void testMaxLat() {
        calculations();
        assertEquals(43.4, ts.getMaxLat(), DELTA);
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
        assertEquals(-88.0, ts.getMinLong(), DELTA);
    }
    @Test
    public void testAvgSpeedK() {
        calculations();
        assertEquals(57.615, ts.getAvgSpeedK(), 57.615*DELTA);
    }
    @Test
    public void testAvgSpeedM() {
        calculations();
        assertEquals(35.80365, ts.getAvgSpeedM(), 35.80365*DELTA);

    }
    @Test
    public void testMaxSpeedK() {
        calculations();
        assertEquals(66.72, ts.getMaxSpeedK(), 66.72*DELTA);
    }
    @Test
    public void testMaxSpeedM() {
        calculations();
        assertEquals(41.46, ts.getMaxSpeedM(), 41.46*DELTA);

    }
    @Test
    public void testDistK() {
        calculations();
        assertEquals(38.464, ts.getDistK(), DELTA*8.260);
    }

    @Test
    public void testDistM() {
        calculations();
        assertEquals(23.9, ts.getDistM(), DELTA*5.132526);
    }

    private void calculations() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateInString1 = "2010-10-19T13:00:00Z";
        String dateInString2 = "2010-10-19T13:10:00Z";
        String dateInString3 = "2010-10-19T13:20:00Z";
        String dateInString4 = "2010-10-19T13:30:00Z";
        String dateInString5 = "2010-10-19T13:40:00Z";
        ArrayList<TrackPoint> pList = new ArrayList<>();
        try {
        	// if your data format was "yyyy-MM-dd'T'HH:mm:ss'Z'", then you wouldn't have to replace the Z
            Date time1 = formatter.parse(dateInString1);
            Date time2 = formatter.parse(dateInString2);
            Date time3 = formatter.parse(dateInString3);
            Date time4 = formatter.parse(dateInString4);
            Date time5 = formatter.parse(dateInString5);
            TrackPoint p1 = new TrackPoint(43.3, -87.9, 500.0, time1);
            TrackPoint p2 = new TrackPoint(43.3, -88.0, 500.0, time2);
            TrackPoint p3 = new TrackPoint(43.4, -88.0, 500.0, time3);
            TrackPoint p4 = new TrackPoint(43.4, -87.9, 500.0, time4);
            TrackPoint p5 = new TrackPoint(43.3, -87.9, 500.0, time5);
            pList.add(p1);
            pList.add(p2);
            pList.add(p3);
            pList.add(p4);
            pList.add(p5);
            Track t = new Track("GPSTest2", pList);
            TracksCalculator tc = new TracksCalculator();
            tc.calculateMetrics(t);
         // you should have at least one test that validates the data structure itself - in order to make sure that the parser
            // is correctly generating the correct ArrayList<TrackPoint>
            ts = t.getTrackStats();
        } catch (ParseException pe) {
            fail(pe);
            pe.printStackTrace();
        }
    }
}


