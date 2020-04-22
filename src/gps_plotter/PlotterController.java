package gps_plotter;

import gps.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Controller in charge of plotting window
 */
public class PlotterController {


    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private TableView tableView;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CheckBox showHideButton;
    @FXML
    private Spinner<String> trackSpinner;

    private GPSController gpsController;
    private Plotter plotter;
    private TracksHandler tracksHandler;

    private Stage plotterStage;

    private boolean[] showOnGraph = {true, true, true, true, true, true, true, true, true, true};

    @FXML
    public void initialize(){
        //sets up the table so it can be populated
        plotter = new Plotter(lineChart, this);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn distMColumn = new TableColumn("Distance(Mi)");
        distMColumn.setCellValueFactory(new PropertyValueFactory<>("distM"));

        TableColumn distKColumn = new TableColumn("Distance(Km)");
        distKColumn.setCellValueFactory(new PropertyValueFactory<>("distK"));

        tableView.getColumns().addAll(nameColumn, distKColumn, distMColumn);

        trackSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(showHideButton.isDisable()){
                showHideButton.disableProperty().setValue(false);
            }
            try {
                int index = tracksHandler.getTrackIndex(newValue);
                showHideButton.selectedProperty().setValue(showOnGraph[index]);
            } catch (NullPointerException e){
                //this is thrown when a new track is loaded but doesn't affect anything
            }
        });
    }

    public void setMainController(GPSController gpsController){
        this.gpsController = gpsController;
    }

    /**
     * Plots all selected Tracks' elevation gains vs time
     */
    public void graphElevationGainVsTime(){
        showHideButton.disableProperty().setValue(false);

        this.tracksHandler = gpsController.getTracksHandler();

        try {

            if(this.lineChart.getData() != null && this.lineChart.getData().size() != 0){ //Clears graph when window is opened only if series exists
                this.plotter.clearChart();
            }

            for(int i = 0; i < this.tracksHandler.getTrackAmount(); i++){
                if(showOnGraph[i]){
                    Track t = this.tracksHandler.getTrack(i);

                    if(t.getPointAmount() > 1) {
                        this.plotter.plotElevationGain(t);
                    } else{
                        createErrorDialog("Elevation Gain vs Time Plotting Error", "Track: " + t.getName() + " doesn't have enough points to graph Elevation Gain vs Time");
                    }
                }
            }

        } catch(NullPointerException n){
            showHideButton.disableProperty().setValue(true);
            createErrorDialog("Elevation Gain vs Time Plotting Error", "No tracks are loaded.");
        }
    }

    public void setTracksHandler(TracksHandler tracksHandler){
        this.tracksHandler = tracksHandler;
    }

    public TracksHandler getTracksHandler(){
        return tracksHandler;
    }

    public void graphTwoDPlot(){
        showHideButton.disableProperty().setValue(false);
        this.tracksHandler = gpsController.getTracksHandler();
        try {
            plotter.convertToCartesian();
        } catch(NullPointerException n){
            showHideButton.disableProperty().setValue(true);
            createErrorDialog("2D Graph Plotting Error", "No tracks are loaded.");
        }
    }

    public void addTable(TrackStats trackStats){
        tableView.getItems().add(trackStats);
    }

    public NumberAxis getXAxis(){
        return this.xAxis;
    }

    public NumberAxis getYAxis(){
        return this.yAxis;
    }

    public void clearTable() {
        tableView.getItems().clear();
    }

    public void setStage(Stage stage){
        this.plotterStage = stage;
    }

    /**
     * Creates an error dialog from a type and message
     *
     * @param errorType type of error
     * @param errorMsg message error provides
     */
    private void createErrorDialog(String errorType, String errorMsg){

        Alert headerError = new Alert(Alert.AlertType.ERROR);
        headerError.setTitle("Error Dialog");
        headerError.setHeaderText(errorType);
        headerError.setContentText(errorMsg);
        headerError.showAndWait();

    }

    /**
     * @author Austin Demars
     *
     * Called when 'File/Exit' is pressed
     * Closes (exits) the application
     */
    public void exit() {
        this.plotterStage.hide();
    }

    public void showOrHide(ActionEvent actionEvent) {
        int index = tracksHandler.getTrackIndex(trackSpinner.getValue());
        if(index == -1){
            createErrorDialog("Track not loaded", "The track that is to be shown is not loaded!");
        } else {
            showOnGraph[index] = !showOnGraph[index];
        }
    }

    public void updateSpinner(SpinnerValueFactory<String> valueFactory, String trackName){
        trackSpinner.setValueFactory(valueFactory);
        trackSpinner.getValueFactory().setValue(trackName);
    }


    public boolean[] getshowOnGraph(){
        return showOnGraph;
    }
}
