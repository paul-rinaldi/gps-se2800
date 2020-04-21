package gps_plotter;

import gps.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PlotterController {


    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private TableView tableView;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private GPSController gpsController;
    private Plotter plotter;
    private TracksHandler tracksHandler;

    private Stage plotterStage;

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
    }

    public void setMainController(GPSController gpsController){
        this.gpsController = gpsController;
    }

    public void graphElevationGainVsTime(){

        try {
            Track t = gpsController.getTracksHandler().getTrack(gpsController.getSpinner().getValue());
            this.plotter.plotElevationGain(t);
        } catch(NullPointerException n){
            createErrorDialog("Elevation Gain vs Time Plotting Error", "No tracks are loaded.");
        } catch(IllegalArgumentException e){
            createErrorDialog("Elevation Gain vs Time Plotting Error", e.getLocalizedMessage());
        }
    }

    public void setTracksHandler(TracksHandler tracksHandler){
        this.tracksHandler = tracksHandler;
    }

    public TracksHandler getTracksHandler(){
        return tracksHandler;
    }

    public void graphTwoDPlot(){
        try {
            plotter.convertToCartesian();
        } catch(NullPointerException n){
            createErrorDialog("2D Graph Plotting Error", "No tracks are loaded.");
        }
    }

    public void addPointToGraph(XYChart.Series series){
        lineChart.getData().add(series);
    }

    public void addTable(TrackStats trackStats){
        tableView.getItems().add(trackStats);
    }

    public Plotter getPlotter(){
        return plotter;
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
}
