package api;

import gps.TrackPoint;

import java.util.ArrayList;

/**
 * This class creates requests and parses them for google maps static images
 */
public class MapHelper {
    // Please do NOT send tons of requests, just enough, be nice, please
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

        // google documentation on api https://developers.google.com/maps/documentation/maps-static/intro?hl=en_US
    }

    // Use
    // https://developer.ibm.com/technologies/java/tutorials/java-theory-and-practice-3/
    // java.net docs https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html
    // mykong https://mkyong.com/java/java-11-httpclient-examples/
    // intro openjdk https://openjdk.java.net/groups/net/httpclient/intro.html
    // Compare: https://www.innovation.ch/java/HTTPClient/urlcon_vs_httpclient.html
}
