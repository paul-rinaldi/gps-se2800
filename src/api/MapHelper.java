package api;

import gps.TrackPoint;

import java.util.ArrayList;

/**
 * This class creates requests and parses them for google maps static images
 */
@Deprecated
public class MapHelper {
    private static final String GOOGLE_MAPS_STATIC_API_KEY = "AIzaSyD3Jb1jp2-D5Xi0kfEL5jEATK4c34nUDCY";

    /**
     * This method requests a Google Map image from the Google Maps Static API providing
     *   gps (from gpx files) locations
     * @param trackPoints
     * @param centerLatitude
     * @param centerLongitude
     * @param width
     * @param height
     * @param zoom
     * @param mapType
     * @param path
     */
    public static void getMapData(ArrayList<TrackPoint> trackPoints, double centerLatitude, double centerLongitude, int width, int height, int zoom,
                                  String mapType, String path) {
        String url = "https://maps.googleapis.com/maps/api/staticmap?sensor=false&size=" + width + "x" + height +
                "&zoom=" + zoom + "&maptype=" + mapType + "&center=" +centerLatitude + "," + centerLongitude +
                "&key=" + GOOGLE_MAPS_STATIC_API_KEY;

        // decided on not using static as https://openjfx.io/javadoc/11/javafx.web/javafx/scene/web/WebEngine.html
        // along with js, css, and html knowledge from SE2040, we should be able to do a lot more now faster and just have
        // to learn how to get the html up and running
    }

    // Use
    // https://developer.ibm.com/technologies/java/tutorials/java-theory-and-practice-3/
    // java.net docs https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html
    // mykong https://mkyong.com/java/java-11-httpclient-examples/
    // intro openjdk https://openjdk.java.net/groups/net/httpclient/intro.html
    // Compare: https://www.innovation.ch/java/HTTPClient/urlcon_vs_httpclient.html
}
