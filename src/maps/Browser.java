package maps;

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

public class Browser extends Pane {
    private double lat;
    private double lon;

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    public Browser() {
        final URL urlGoogleMaps = getClass().getResource("/maps/client.html");

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("finished loading");
            }
        }); // addListener()
        webEngine.load(urlGoogleMaps.toString());

        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> stringWebEvent) {
                System.out.println(stringWebEvent.toString());
            }
        });

        getChildren().add(webView);

        final TextField latitude = new TextField("0.0");
        final TextField longitude = new TextField("0.0");
        Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // send our lat lon from java to javascript
                lat = Double.parseDouble(latitude.getText());
                lon = Double.parseDouble(longitude.getText());

                System.out.println("(Lat,Lon)=("+lat+", "+lon+")");

                // load js
                try {
                    String js = readFile("./src/maps/client.js", StandardCharsets.UTF_8);
                    webEngine.executeScript(js);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                webEngine.executeScript("" +
                        "window.lat = " + lat + ";" +
                        "window.lon = " + lon + ";"
                );
            }
        });

        HBox toolbar = new HBox();
        // first parts
        toolbar.getChildren().addAll(latitude, longitude, update);

        // second level of hbox
        getChildren().addAll(toolbar);
    }

    /**
     * Loads tracks into maps view
     * @param track - TrackPoints array to add to
     */
    @FXML
    public void loadTrack(TrackPoint[] track) {

    }

    public static String readFile(String path, Charset encoding) throws IOException {
        return Files.readString(Paths.get(path), encoding);
    }
}
