package gps;


import gps_plotter.PlotterController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import maps.CaptureController;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.ArrayList;

/**
 * Handles all GUI events and distributes tasks to other classes
 *
 * @author demarsa
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class GPSController {

    private TracksHandler tracksHandler;
    private AbstractParserEventHandler handler = new GPXHandler();
    private int tracksRemaining = 10;
    private ObservableList<String> trackNames;

    @FXML
    private Spinner<String> trackSpinner;
    @FXML
    private TextField tracksRemainingBox;
    @FXML
    private TextField maxLat;
    @FXML
    private TextField minLat;
    @FXML
    private TextField maxLong;
    @FXML
    private TextField minLong;
    @FXML
    private TextField maxElevMet;
    @FXML
    private TextField minElevMet;
    @FXML
    private TextField maxElevFeet;
    @FXML
    private TextField minElevFeet;
    @FXML
    private TextField dMiles;
    @FXML
    private TextField dKilometers;
    @FXML
    private TextField avSpeedMPH;
    @FXML
    private TextField avSpeedKPH;
    @FXML
    private TextField maxSpeedMPH;
    @FXML
    private TextField maxSpeedKPH;
    @FXML
    private TableView speedTable;
    @FXML
    private Menu tableOptions;
    @FXML
    private CheckMenuItem trackOne;
    @FXML
    private CheckMenuItem trackTwo;
    @FXML
    private CheckMenuItem trackThree;
    @FXML
    private CheckMenuItem trackFour;
    @FXML
    private CheckMenuItem trackFive;
    @FXML
    private CheckMenuItem trackSix;
    @FXML
    private CheckMenuItem trackSeven;
    @FXML
    private CheckMenuItem trackEight;
    @FXML
    private CheckMenuItem trackNine;
    @FXML
    private CheckMenuItem trackTen;

    private ArrayList<DistanceTraveledAtSpeed> tableData = new ArrayList<>();
    private PlotterController plotterController;
    private Stage plotterStage;
    private CaptureController mapsController;
    private Stage mapsStage;

    private boolean firstTimePlotting = true;

    /**
     * Initializes JavaFX table element
     */
    @FXML
    public void initialize() {
        //set up table rows and columns
        TableColumn nameColumn = new TableColumn("Track Name");
        nameColumn.setPrefWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn lessThanThreeColumn = new TableColumn("less than 3mph");
        lessThanThreeColumn.setPrefWidth(100);
        lessThanThreeColumn.setCellValueFactory(new PropertyValueFactory<>("lessThanThree"));

        TableColumn threeAndSevenColumn = new TableColumn("3mph and 7mph");
        threeAndSevenColumn.setPrefWidth(100);
        threeAndSevenColumn.setCellValueFactory(new PropertyValueFactory<>("threeAndSeven"));

        TableColumn sevenAndTenColumn = new TableColumn("7mph and 10mph");
        sevenAndTenColumn.setPrefWidth(115);
        sevenAndTenColumn.setCellValueFactory(new PropertyValueFactory<>("sevenAndTen"));

        TableColumn tenAndFifteenColumn = new TableColumn("10mph and 15mph");
        tenAndFifteenColumn.setPrefWidth(115);
        tenAndFifteenColumn.setCellValueFactory(new PropertyValueFactory<>("tenAndFifteen"));

        TableColumn fifteenAndTwentyColumn = new TableColumn("5mph and 20mph");
        fifteenAndTwentyColumn.setPrefWidth(115);
        fifteenAndTwentyColumn.setCellValueFactory(new PropertyValueFactory<>("fifteenAndTwenty"));

        TableColumn greaterThanTwentyColumn = new TableColumn("more than 20mph");
        greaterThanTwentyColumn.setPrefWidth(115);
        greaterThanTwentyColumn.setCellValueFactory(new PropertyValueFactory<>("greaterThanTwenty"));

        speedTable.getColumns().addAll(nameColumn, lessThanThreeColumn, threeAndSevenColumn, sevenAndTenColumn,
                tenAndFifteenColumn, fifteenAndTwentyColumn, greaterThanTwentyColumn);
    }

    public GPSController() {
        trackNames = FXCollections.observableArrayList();
    }

    /**
     * Changes the displayed stats to whatever track is selected if the stats aren't null
     */
    public void changeTrackStatDisplay() {

        if (tracksHandler != null) {

            if (tracksHandler.getTrack(trackSpinner.getValue()).getTrackStats() != null) {
                displayTrackStats();
            } else {
                clearStatsDisplay();
            }
        }

    }

    /**
     * Calculates metrics of selected Track in the Spinner
     * Won't calculate stats if stats have been calculated
     */
    private void calcTrackStats() {

        try {
            if (tracksHandler != null) {

                String trackName = trackSpinner.getValue();

                //Only calculates stats if there are none already
                if (tracksHandler.getTrack(trackName).getTrackStats() == null) {
                    tracksHandler.calculateTrackStats(trackSpinner.getValue());
                    displayTrackStats();
                }
            }
        } catch (UnsupportedOperationException e) {
            createInfoDialog(e.getLocalizedMessage(), "Distance and Speed statistics" +
                    " couldn't be calculated because there is only one point in the track.");
            displayTrackStats();
        }
    }

    /**
     * Gets the currently selected track in the Spinner and displays all the calculated metrics.
     */
    private void displayTrackStats() {
        Track track = tracksHandler.getTrack(trackSpinner.getValue());
        TrackStats stats = track.getTrackStats();

        //Set stats in boxes
        displayTrackMinMaxValues(stats);
        //Decides how to display distances/ speeds
        displayTrackDistancesSpeeds(stats);

    }

    /**
     * Determines how to display the stats based on value retreived
     *
     * @param stats TrackStats of Track selected
     */
    private void displayTrackMinMaxValues(TrackStats stats) {

        double maxLati = stats.getMaxLat();
        double minLati = stats.getMinLat();
        double maxLongi = stats.getMaxLong();
        double minLongi = stats.getMinLong();
        double maxElevM = stats.getMaxElevM();
        double minElevM = stats.getMinElevM();
        double maxElevFt = stats.getMaxElevFt();
        double minElevFt = stats.getMinElevFt();

        if (maxLati > Double.MAX_VALUE * -1) {
            maxLat.setText(Double.toString(maxLati));
        } else {
            maxLat.setText("N/A");
        }

        if (minLati < Double.MAX_VALUE) {
            minLat.setText(Double.toString(minLati));
        } else {
            minLat.setText("N/A");
        }

        if (maxLongi > Double.MAX_VALUE * -1) {
            maxLong.setText(Double.toString(maxLongi));
        } else {
            maxLong.setText("N/A");
        }

        if (minLongi < Double.MAX_VALUE) {
            minLong.setText(Double.toString(minLongi));
        } else {
            minLong.setText("N/A");
        }

        if (maxElevM > Double.MAX_VALUE * -1) {
            maxElevMet.setText(Double.toString(maxElevM));
        } else {
            maxElevMet.setText("N/A");
        }

        if (minElevM < Double.MAX_VALUE) {
            minElevMet.setText(Double.toString(minElevM));
        } else {
            minElevMet.setText("N/A");
        }

        if (maxElevFt > Double.MAX_VALUE * -1) {
            maxElevFeet.setText(Double.toString(maxElevFt));
        } else {
            maxElevFeet.setText("N/A");
        }

        if (minElevFt < Double.MAX_VALUE) {
            minElevFeet.setText(Double.toString(minElevFt));
        } else {
            minElevFeet.setText("N/A");
        }


    }

    /**
     * Sets distances and speeds of the Track stats depending on their value
     * Stats > 0 will be displayed, otherwise it displays N/A
     * However, max speeds that equal the minimum double value will be displayed with N/A as well
     *
     * @param stats Track Stats of calculator
     */
    private void displayTrackDistancesSpeeds(TrackStats stats) {

        double distMiles = stats.getDistM();
        double distKilo = stats.getDistK();
        double avSpeedM = stats.getAvgSpeedM();
        double avSpeedK = stats.getAvgSpeedK();
        double maxSpeedM = stats.getMaxSpeedM();
        double maxSpeedK = stats.getMaxSpeedK();

        if (distMiles == 0) {
            dMiles.setText("N/A");
        } else {
            dMiles.setText(Double.toString(stats.getDistM()));
        }

        if (distKilo == 0) {
            dKilometers.setText("N/A");
        } else {
            dKilometers.setText(Double.toString(stats.getDistK()));
        }

        if (avSpeedM == 0) {
            avSpeedMPH.setText("N/A");
        } else {
            avSpeedMPH.setText(Double.toString(stats.getAvgSpeedM()));
        }

        if (avSpeedK == 0) {
            avSpeedKPH.setText("N/A");
        } else {
            avSpeedKPH.setText(Double.toString(stats.getAvgSpeedK()));
        }

        if (maxSpeedM == 0) {
            maxSpeedMPH.setText("N/A");
        } else {
            maxSpeedMPH.setText(Double.toString(stats.getMaxSpeedM()));
        }

        if (maxSpeedK == 0) {
            maxSpeedKPH.setText("N/A");
        } else {
            maxSpeedKPH.setText(Double.toString(stats.getMaxSpeedK()));
        }


    }

    /**
     * Clears displayed stats of a track
     */
    private void clearStatsDisplay() {
        maxLat.setText("");
        minLat.setText("");
        maxLong.setText("");
        minLong.setText("");
        maxElevMet.setText("");
        minElevMet.setText("");
        maxElevFeet.setText("");
        minElevFeet.setText("");
        dMiles.setText("");
        dKilometers.setText("");
        avSpeedMPH.setText("");
        avSpeedKPH.setText("");
        maxSpeedMPH.setText("");
        maxSpeedKPH.setText("");
    }

    /**
     * Creates an error dialog from a type and message
     *
     * @param errorType type of error
     * @param errorMsg  message error provides
     */
    private void createErrorDialog(String errorType, String errorMsg) {

        Alert headerError = new Alert(Alert.AlertType.ERROR);
        headerError.setTitle("Error Dialog");
        headerError.setHeaderText(errorType);
        headerError.setContentText(errorMsg);
        headerError.showAndWait();

    }

    /**
     * Creates an information dialog to show the user
     *
     * @param infoType type of information provided
     * @param infoMsg  information message provided
     */
    private void createInfoDialog(String infoType, String infoMsg) {

        Alert headerError = new Alert(Alert.AlertType.INFORMATION);
        headerError.setTitle("Information Dialog");
        headerError.setHeaderText(infoType);
        headerError.setContentText(infoMsg);
        headerError.showAndWait();

    }

    /**
     * Loads a track by using a GPS file selected by the user
     */
    public void loadTrack() {

        try {

            if (tracksRemaining != 0) {

                FileChooser chooser = new FileChooser();
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));


                File inputFile = chooser.showOpenDialog(null);

                if (inputFile != null) {


                    parseTrackFile(inputFile.getAbsolutePath());
                    tracksRemaining--;

                    //This takes the most recently added track and displays its name and points loaded
                    int trackIdx = tracksHandler.getTrackAmount() - 1;
                    Track trackLoaded = tracksHandler.getTrack(trackIdx);
                    createInfoDialog("Track Successfully Created",
                            "Name of track: " + trackLoaded.getName()
                                    + "\nPoints loaded: " + trackLoaded.getPointAmount());


                    tracksRemainingBox.setText(Integer.toString(tracksRemaining));
                    trackNames.add(trackLoaded.getName());

                    SpinnerValueFactory<String> valueFactory =
                            new SpinnerValueFactory.ListSpinnerValueFactory<>(trackNames);
                    trackSpinner.setValueFactory(valueFactory);

                    trackSpinner.getValueFactory().setValue(trackLoaded.getName());

                    plotterController.updateSpinner(new SpinnerValueFactory.ListSpinnerValueFactory<>(trackNames),
                            trackLoaded.getName());

                    calcTrackStats();

                    updateTableMenu(trackLoaded);
                }

            } else {
                //Inform them their tracks have reached maximum
                createInfoDialog("Maximum Tracks Loaded",
                        "You have loaded the maximum allowable tracks");
            }

        } catch (SAXException e) {

            createErrorDialog("Parsing Error", e.getLocalizedMessage() +
                    "\nThe error occurred near line " +
                    handler.getLine() + ", col " + handler.getColumn());

            ((GPXHandler) handler).resetAttributes();

        } catch (UnsupportedOperationException uoe) {

            createInfoDialog("Track with name already exists", uoe.getLocalizedMessage());

            ((GPXHandler) handler).resetAttributes();

        } catch (Exception e) {
            createErrorDialog("Parsing Error", e.getLocalizedMessage());
            ((GPXHandler) handler).resetAttributes();
        }


    }
    //adds the track data to an arraylist as they are loaded so the data doesn't have to be calculated several times
    private void updateTableMenu(Track trackLoaded) {
        tableOptions.setDisable(false);
        DistanceTraveledAtSpeed temp = new DistanceTraveledAtSpeed(trackLoaded);
        try {
            temp.calculateData();
            tableData.add(temp);
        } catch (RuntimeException e) {
            tableData.add(null);
        }
        switch (10-tracksRemaining){
            case 1: trackOne.setText(trackLoaded.getName());
                trackOne.setVisible(true);
                break;
            case 2: trackTwo.setText(trackLoaded.getName());
                trackTwo.setVisible(true);
                break;
            case 3: trackThree.setText(trackLoaded.getName());
                trackThree.setVisible(true);
                break;
            case 4: trackFour.setText(trackLoaded.getName());
                trackFour.setVisible(true);
                break;
            case 5: trackFive.setText(trackLoaded.getName());
                trackFive.setVisible(true);
                break;
            case 6: trackSix.setText(trackLoaded.getName());
                trackSix.setVisible(true);
                break;
            case 7: trackSeven.setText(trackLoaded.getName());
                trackSeven.setVisible(true);
                break;
            case 8: trackEight.setText(trackLoaded.getName());
                trackEight.setVisible(true);
                break;
            case 9: trackNine.setText(trackLoaded.getName());
                trackNine.setVisible(true);
                break;
            case 10: trackTen.setText(trackLoaded.getName());
                trackTen.setVisible(true);
                break;
        }
    }

    /**
     * Uses Parser class to parse the GPS file selected by the user
     * (Used inside of loadTrack)
     *
     * @param filename String filename of GPS file selected
     * @throws SAXException thrown if parser encounters an exception
     * @throws Exception    thrown when any other exception is caught from the parser
     */
    private void parseTrackFile(String filename) throws SAXException, Exception {

        handler.enableLogging(false);

        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse(filename);

            if (tracksRemaining == 10) {
                tracksHandler = ((GPXHandler) handler).getTrackHandler();
            }

        } catch (SAXException e) {
            throw e; //Throws sax
        } catch (Exception e) {
            throw e;
        }


    }

    public TracksHandler getTracksHandler() {
        return this.tracksHandler;
    }

    public void setPlotterController(PlotterController plotterController) {
        this.plotterController = plotterController;
    }

    public void setPlotterStage(Stage stage) {
        this.plotterStage = stage;
    }

    public void setMapsController(CaptureController controller) {
        this.mapsController = controller;
    }

    public void setMapsStage(Stage stage) {
        this.mapsStage = stage;
    }

    /**
     * Opens plotter window and immediately graphs all loaded tracks with 2D Plotter in cartesian coordinates
     * Only graphs selected tracks if window was already modified (saves state)
     */
    public void showPlotter() {
        plotterController.setTracksHandler(tracksHandler);

        if(tracksHandler == null || firstTimePlotting){
            plotterController.disableShowHideButton();
            this.firstTimePlotting = false;
        }
        if (tracksHandler.getTrackAmount() > 1){
            plotterController.setButtonValue(false);
        }
        plotterStage.show();
    }

    /**
     * event handler for the check menu items in the plot menu, shows or hides the specified data on the table
     * @param actionEvent the event that caused this method to be called
     */
    public void updateTable(ActionEvent actionEvent){
            if (actionEvent.getSource().toString().contains("id=trackOne")) {
                if (tableData.get(0) == null){
                    trackOne.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackOne.isSelected()) {
                    speedTable.getItems().add(tableData.get(0));
                } else {
                    speedTable.getItems().remove(tableData.get(0));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackTwo")) {
                if (tableData.get(1) == null){
                    trackTwo.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackTwo.isSelected()) {
                    speedTable.getItems().add(tableData.get(1));
                } else {
                    speedTable.getItems().remove(tableData.get(1));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackThree")) {
                if (tableData.get(2) == null){
                    trackThree.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackThree.isSelected()) {
                    speedTable.getItems().add(tableData.get(2));
                } else {
                    speedTable.getItems().remove(tableData.get(2));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackFour")) {
                if (tableData.get(3) == null){
                    trackFour.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackFour.isSelected()) {
                    speedTable.getItems().add(tableData.get(3));
                } else {
                    speedTable.getItems().remove(tableData.get(3));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackFive")) {
                if (tableData.get(4) == null){
                    trackFive.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackFive.isSelected()) {
                    speedTable.getItems().add(tableData.get(4));
                } else {
                    speedTable.getItems().remove(tableData.get(4));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackSix")) {
                if (tableData.get(5) == null){
                    trackSix.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackSix.isSelected()) {
                    speedTable.getItems().add(tableData.get(5));
                } else {
                    speedTable.getItems().remove(tableData.get(5));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackSeven")) {
                if (tableData.get(6) == null){
                    trackSeven.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackSeven.isSelected()) {
                    speedTable.getItems().add(tableData.get(6));
                } else {
                    speedTable.getItems().remove(tableData.get(6));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackEight")) {
                if (tableData.get(7) == null){
                    trackEight.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackEight.isSelected()) {
                    speedTable.getItems().add(tableData.get(7));
                } else {
                    speedTable.getItems().remove(tableData.get(7));
                }
            } else if (actionEvent.getSource().toString().contains("id=trackNine")) {
                if (tableData.get(8) == null){
                    trackNine.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackNine.isSelected()) {
                    speedTable.getItems().add(tableData.get(8));
                } else {
                    speedTable.getItems().remove(tableData.get(8));
                }
            } else {
                if (tableData.get(9) == null){
                    trackTen.setSelected(false);
                    createInfoDialog("Not Enough Points", "The track must have at least two track points to " +
                            "create a table of speeds traveled");
                }else if (trackTen.isSelected()) {
                    speedTable.getItems().add(tableData.get(9));
                } else {
                    speedTable.getItems().remove(tableData.get(9));
                }
            }
    }

    /**
     * Shows Google Maps window
     */
    public void showMapsWindow() {
        this.mapsStage.show();
    }
    /**
     * @author Austin Demars
     * <p>
     * Called when 'File/Exit' is pressed
     * Closes (exits) the application
     */
    public void exit() { //Austin
        Platform.exit();
    }
}
