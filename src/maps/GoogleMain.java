package maps;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Temporary class for testing Capture FXML's webengine for html etc.
 */
public class GoogleMain extends Application {
    private Scene scene;
    private Browser browser;
    private double lat;
    private double lon;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        /*
        Parent root = FXMLLoader.load(getClass().getResource("Capture.fxml"));
        primaryStage.setTitle("Google Maps App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        */
        browser = new Browser();
        Scene scene = new Scene(browser);

        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class Browser extends Pane {
        private double lat;
        private double lon;

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        public Browser() {
            final URL urlGoogleMaps = getClass().getResource("client.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
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
    }
}
