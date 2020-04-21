/*
 * Course: SE 2800 - 041
 * Spring 2020
 * Names: Paul Rinaldi
 * Created 20 April 2020
 * Unlicensed.
 */

package maps;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * This class is a replacement controller for the GPX Project including map options
 * The design is based on the master controller created by @author Joseph Skubal
 *  in SE2030 with Paul Rinaldi, Jesse Sierra, and Alexander Neuwirth
 *
 * @author Paul Rinaldi
 * @version 4 April 2020
 */
public class MasterController {
    // Controllers controlled by this controller
    private CaptureController captureController;
    private Map map;

    // Constants

    // Main objects
    private Stage mainStage;

    // FXML Scene Components
    @FXML
    private ToggleGroup mapTypeSelect;

    @FXML
    private ToggleGroup zoomSelect;

    @FXML
    public void onMapChange() {
        map.update();
    }

    @FXML
    public void openJSONCapture() {
        // todo
    }
}
