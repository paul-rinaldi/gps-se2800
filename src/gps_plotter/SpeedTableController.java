package gps_plotter;

import gps.TracksHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SpeedTableController {

    @FXML
    private TableView speedTable;
    @FXML
    private CheckBox showHideButton;
    @FXML
    private Spinner trackSpinner;

    private TracksHandler tracksHandler;
    private boolean[] showOnGraph = {true, false, false, false, false, false, false, false, false, false};

    /**
     * Initializes JavaFX table element
     */
    @FXML
    public void initialize() {
        TableColumn nameColumn = new TableColumn("Track Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn lessThanThreeColumn = new TableColumn("Distance traveled at less than 3mph");
        lessThanThreeColumn.setCellValueFactory(new PropertyValueFactory<>("lessThanThree"));

        TableColumn threeAndSevenColumn = new TableColumn("Distance traveled between 3mph and 7mph");
        threeAndSevenColumn.setCellValueFactory(new PropertyValueFactory<>("threeAndSeven"));

        TableColumn sevenAndTenColumn = new TableColumn("Distance traveled between 7mph and 10mph");
        sevenAndTenColumn.setCellValueFactory(new PropertyValueFactory<>("sevenAndTen"));

        TableColumn tenAndFifteenColumn = new TableColumn("Distance traveled between 10mph and 15mph");
        tenAndFifteenColumn.setCellValueFactory(new PropertyValueFactory<>("tenAndFifteen"));

        TableColumn fifteenAndTwentyColumn = new TableColumn("Distance traveled between 15mph and 20mph");
        fifteenAndTwentyColumn.setCellValueFactory(new PropertyValueFactory<>("fifteenAndTwenty"));

        TableColumn greaterThanTwentyColumn = new TableColumn("Distance traveled at more than 20mph");
        greaterThanTwentyColumn.setCellValueFactory(new PropertyValueFactory<>("greaterThanTwenty"));

        speedTable.getColumns().addAll(nameColumn, lessThanThreeColumn, threeAndSevenColumn, sevenAndTenColumn,
                tenAndFifteenColumn, fifteenAndTwentyColumn, greaterThanTwentyColumn);

        //Adds event listener to spinner when the track gets changed to update state of select button
        trackSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {

            try {
                int index = tracksHandler.getTrackIndex((String) newValue);
                showHideButton.selectedProperty().setValue(showOnGraph[index]);
            } catch (NullPointerException e) {
                //this is thrown when a new track is loaded but doesn't affect anything
            }
        });
    }

    public void setTracksHandler(TracksHandler tracksHandler){
        this.tracksHandler = tracksHandler;
    }



}
