package maps;

import gps.Track;
import gps.TrackPoint;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class extends Pane functionality with a Webview to browse a webpage loaded in specifically to interact with
 * Google Maps over Javascript
 */
public class Browser extends Pane {
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

    private static String CLIENT_URL = "/maps/client.html";

    public Browser() {
        final URL urlGoogleMaps = getClass().getResource(CLIENT_URL);
        webEngine.load(urlGoogleMaps.toString());

        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> stringWebEvent) {
                System.out.println(stringWebEvent.toString());
            }
        });

        getChildren().add(webView);
    }

    /**
     * Loads tracks into maps view
     * @param track - TrackPoints array to add to
     * @param trackNumber the index of the trackshandler that the current track is located at
     */
    @FXML
    public void loadTrack(Track track, int trackNumber) {
        String linePath ="[";
        for (int i = 0; i < track.getPointAmount(); i++) {
            TrackPoint currentPoint = track.getTrackPoint(i);
            if(i == 0) {
                webEngine.executeScript("addMarker(" + currentPoint.getLatitude() + ", " +
                        currentPoint.getLongitude() + ", " + '"' + track.getName() + " Start" + '"'+ ");");
            } else if(i == track.getPointAmount()-1){
                webEngine.executeScript("addMarker(" + currentPoint.getLatitude() + ", " +
                        currentPoint.getLongitude() + ", " + '"' + track.getName() + " End" + '"' + ");");
            }
            linePath += "{lat: " + currentPoint.getLatitude() + ", lng: " + currentPoint.getLongitude() + "},";
        }
        linePath = linePath.substring(0, linePath.length()-1);
        linePath += "]";
        webEngine.executeScript("addLine(" + linePath  + ", " + trackNumber  + ");");
    }

    /**
     * removes all markers from the map
     */
    public void clearMap(){
        webEngine.executeScript("clearMap();");
    }
}
