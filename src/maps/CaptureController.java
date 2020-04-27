/*
 * Course: SE 2800 - 041
 * Spring 2020
 * Names: Paul Rinaldi
 * Created 20 April 2020
 * Unlicensed.
 */

package maps;

import gps.GPSController;
import gps.TrackPoint;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Date;

/**
 * This class queries map api for static map images
 */
public class CaptureController {
    // Constants

    // Main Objects
    @FXML
    private TextField output;

    private Stage captureStage; //This controller's stage
    private GPSController gpsController; //Main GPS program controller

    @FXML
    private Browser browser;

    @FXML
    public void sendGET() {
        browser.clearMap();
        // load set of tracks
        for(int i = 0; i < gpsController.getTracksHandler().getTrackAmount(); i++){
            browser.loadTrack(gpsController.getTracksHandler().getTrack(i), i);
        }
    }


    /**
     * Closes (exits) the window
     * Used for UI button to close window
     */
    public void exit() {
        this.captureStage.hide();
    }

    public void setStage(Stage stage){
        this.captureStage = stage;
    }

    public void setMainController(GPSController gpsController){
        this.gpsController = gpsController;
    }

}
