package tests;

import gps.*;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class GPSTest10 {

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
        assertEquals(3000.0, ts.getMaxElevM(), DELTA);
    }
    @Test
    public void testMinElev() {
        calculations();
        assertEquals(500.0, ts.getMinElevM(), DELTA);
    }
    @Test
    public void testMaxLong() {
        calculations();
        assertEquals(-88.0, ts.getMaxLong(), DELTA);
    }
    @Test
    public void testMinLong() {
        calculations();
        assertEquals(-88.9, ts.getMinLong(), DELTA);
    }
    @Test
    public void testAvgSpeedK() {
        calculations();
        assertEquals(48.6615, ts.getAvgSpeedK(), 48.6615*DELTA);
    }
    @Test
    public void testAvgSpeedM() {
        calculations();
        assertEquals(30.2368, ts.getAvgSpeedM(), 30.2368*DELTA);
    }
    @Test
    public void testMaxSpeedK() {
        calculations();
        assertEquals(48.668, ts.getMaxSpeedK(), 48.668*DELTA);
    }
    @Test
    public void testMaxSpeedM() {
        calculations();
        assertEquals(30.2410, ts.getMaxSpeedM(), 30.2410*DELTA);
    }
    @Test
    public void testDistK() {
        calculations();
        System.out.println(ts.getDistK());
        assertEquals(73.9, ts.getDistK(), 73.9*DELTA);
    }
    @Test
    public void testDistM() {
        calculations();
        System.out.println(ts.getDistM());
        assertEquals(45.91933, ts.getDistM(), DELTA*45.91933);
    }
    
    @Test
    public void testValidPoints(){
        GPXHandler handler = new GPXHandler();

        handler.enableLogging(false);

        Parser parser;
        try {
            parser = new Parser( handler );
            parser.parse("docs\\GPSTest10.gpx");

            TracksHandler tracksHandler = handler.getTrackHandler();
            Track track = tracksHandler.getTrack(0);

            ArrayList<TrackPoint> manual_ponits = calculations(); //Manual made list of points
            if(manual_ponits == null){
                fail("Calculation method didn't work properly");
            }

            assertEquals(manual_ponits.size(), track.getPointAmount());

            for(int i = 0; i < 10; i++){
                TrackPoint expected = manual_ponits.get(i);
                TrackPoint actual = track.getTrackPoint(i);

                assertEquals(expected.getElevation(), actual.getElevation());
                assertEquals(expected.getLatitude(), actual.getLatitude());
                assertEquals(expected.getLongitude(), actual.getLongitude());
                assertEquals(expected.getTime(), actual.getTime());
            }


        } catch (SAXException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private ArrayList<TrackPoint> calculations() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateInString1 = "2016-02-10T13:10:00Z";
        String dateInString2 = "2016-02-10T13:20:00Z";
        String dateInString3 = "2016-02-10T13:30:00Z";
        String dateInString4 = "2016-02-10T13:40:00Z";
        String dateInString5 = "2016-02-10T13:50:00Z";
        String dateInString6 = "2016-02-10T14:00:00Z";
        String dateInString7 = "2016-02-10T14:10:00Z";
        String dateInString8 = "2016-02-10T14:20:00Z";
        String dateInString9 = "2016-02-10T14:30:00Z";
        String dateInString10 = "2016-02-10T14:40:00Z";
        ArrayList<TrackPoint> pList = new ArrayList<>();
        try {
        	// if your data format was "yyyy-MM-dd'T'HH:mm:ss'Z'", then you wouldn't have to replace the Z
            Date time1 = formatter.parse(dateInString1);
            Date time2 = formatter.parse(dateInString2);
            Date time3 = formatter.parse(dateInString3);
            Date time4 = formatter.parse(dateInString4);
            Date time5 = formatter.parse(dateInString5);
            Date time6 = formatter.parse(dateInString6);
            Date time7 = formatter.parse(dateInString7);
            Date time8 = formatter.parse(dateInString8);
            Date time9 = formatter.parse(dateInString9);
            Date time10 = formatter.parse(dateInString10);
            TrackPoint p1 = new TrackPoint(43.3, -88.0, 1000.0, time1);
            TrackPoint p2 = new TrackPoint(43.3, -88.1, 1500.0, time2);
            TrackPoint p3 = new TrackPoint(43.3, -88.2, 2000.0, time3);
            TrackPoint p4 = new TrackPoint(43.3, -88.3, 2500.0, time4);
            TrackPoint p5 = new TrackPoint(43.3, -88.4, 3000.0, time5);
            TrackPoint p6 = new TrackPoint(43.3, -88.5, 2500.0, time6);
            TrackPoint p7 = new TrackPoint(43.3, -88.6, 2000.0, time7);
            TrackPoint p8 = new TrackPoint(43.3, -88.7, 1500.0, time8);
            TrackPoint p9 = new TrackPoint(43.3, -88.8, 1000.0, time9);
            TrackPoint p10 = new TrackPoint(43.3, -88.9, 500.0, time10);
            pList.add(p1);
            pList.add(p2);
            pList.add(p3);
            pList.add(p4);
            pList.add(p5);
            pList.add(p6);
            pList.add(p7);
            pList.add(p8);
            pList.add(p9);
            pList.add(p10);
            Track t = new Track("GPSTest10", pList);
            TracksCalculator tc = new TracksCalculator();
            tc.calculateMetrics(t);
            ts = t.getTrackStats();
            return pList;
        } catch (ParseException pe) {
            fail(pe);
            pe.printStackTrace();
        }
        return null;
    }
}
