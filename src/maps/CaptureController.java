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
 * This class controls the Google Maps Browser View to allow for tracks to be loaded in
 * Ensure that javafx.web is added to the vm options for vm option --add-modules=
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
        if (gpsController.getTracksHandler() != null) {
            for(int i = 0; i < gpsController.getTracksHandler().getTrackAmount(); i++){
                browser.loadTrack(gpsController.getTracksHandler().getTrack(i), i);
            }
        } else {
            // Alert user to load a file or allow user to recover by starting a filechooser to load the file
            // FUTURE IMPL: browser.displayError
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
