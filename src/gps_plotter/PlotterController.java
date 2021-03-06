package gps_plotter;

import gps.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    @FXML
    private TextArea LegendText;
    @FXML
    private Label chartTitle;
    @FXML
    private RadioButton distanceKM;
    @FXML
    private RadioButton distanceMI;
    @FXML
    private Label distanceLabel;

    private GPSController gpsController;
    private Plotter plotter;
    private TracksHandler tracksHandler;
    private String lastGraphLoaded = "";
    private boolean firstTimePlottingDvsT = true;

    private Stage plotterStage;

    private boolean[] showOnGraph = {true, false, false, false, false, false, false, false, false, false};

    private boolean graphDistanceVsTimeInKM = true;

    private void showDistanceVsTimeUnits(boolean visible){
        this.distanceKM.setVisible(visible);
        this.distanceMI.setVisible(visible);
        this.distanceLabel.setVisible(visible);
    }

    /**
     * Called when Distance Vs Time menu item is pressed
     */
    public void graphDistanceVsTimeStartup(){
        if(firstTimePlottingDvsT) {
            this.distanceKM.setSelected(true); //Initially sets KM radio button to selected
            this.firstTimePlottingDvsT = false;
        }
        showDistanceVsTimeUnits(true);
        graphDistanceVsTime();
    }

    /**
     * Called when Kilometers button is pressed
     */
    public void graphDistanceVsTimeKM(){
        graphDistanceVsTimeInKM = true;
        graphDistanceVsTime();

    }

    /**
     * Called when Miles button is pressed
     */
    public void graphDistanceVsTimeMI(){
        graphDistanceVsTimeInKM = false;
        graphDistanceVsTime();

    }

    /**
     * Initializes JavaFX table element
     */
    @FXML
    public void initialize() {
        //sets up the table so it can be populated
        plotter = new Plotter(lineChart, this);

        //Disables the dot symbols generated by the graph on load.
        lineChart.setCreateSymbols(false);
        
        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn distMColumn = new TableColumn("Distance(Mi)");
        distMColumn.setCellValueFactory(new PropertyValueFactory<>("distM"));

        TableColumn distKColumn = new TableColumn("Distance(Km)");
        distKColumn.setCellValueFactory(new PropertyValueFactory<>("distK"));

        tableView.getColumns().addAll(nameColumn, distKColumn, distMColumn);

        //Adds event listener to spinner when the track gets changed to update state of select button
        trackSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {

            try {
                int index = tracksHandler.getTrackIndex(newValue);
                showHideButton.selectedProperty().setValue(showOnGraph[index]);
            } catch (NullPointerException e) {
                //this is thrown when a new track is loaded but doesn't affect anything
            }
        });
    }

    /**
     * Plots all selected Tracks' distance vs time - distance unit is based on user selection
     * Default distance unit is kilometers
     */
    private void graphDistanceVsTime(){
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        lastGraphLoaded = "Distance Vs Time";

        showHideButton.disableProperty().setValue(false);

        this.tracksHandler = gpsController.getTracksHandler();

        try {

            if (this.lineChart.getData() != null && this.lineChart.getData().size() != 0) { //Clears graph when window is opened only if series exists
                this.plotter.clearChart();
            }

            reenableLegend();

            setChartTitle("Distance Vs Time");

            for (int i = 0; i < this.tracksHandler.getTrackAmount(); i++) {
                if (showOnGraph[i]) {
                    Track t = this.tracksHandler.getTrack(i);

                    if (t.getPointAmount() > 1) {
                       plotter.plotDistanceVsTime(t, graphDistanceVsTimeInKM);
                    } else {
                        createErrorDialog("Distance vs Time Plotting Error", "Track: " + t.getName() + " doesn't have enough points to graph Elevation Gain vs Time");
                    }
                }
            }

        } catch (NullPointerException n) {
            showHideButton.disableProperty().setValue(true);
            createErrorDialog("Distance vs Time Plotting Error", "No tracks are loaded.");
        }
    }

    /**
     * Plots all selected Tracks' elevation gains vs time
     */
    public void graphElevationGainVsTime() {
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        lastGraphLoaded = "Elevation Gain Vs Time";

        showHideButton.disableProperty().setValue(false);
        showDistanceVsTimeUnits(false);

        this.tracksHandler = gpsController.getTracksHandler();

        chartTitle.setText("Elevation Gain Vs. Time");

        try {

            if (this.lineChart.getData() != null && this.lineChart.getData().size() != 0) { //Clears graph when window is opened only if series exists
                this.plotter.clearChart();
            }

            reenableLegend();

            for (int i = 0; i < this.tracksHandler.getTrackAmount(); i++) {
                if (showOnGraph[i]) {
                    Track t = this.tracksHandler.getTrack(i);

                    if (t.getPointAmount() > 1) {
                        this.plotter.plotElevationGain(t);
                    } else {
                        createErrorDialog("Elevation Gain vs Time Plotting Error", "Track: " + t.getName() + " doesn't have enough points to graph Elevation Gain vs Time");
                    }
                }
            }
        } catch (NullPointerException n) {
            showHideButton.disableProperty().setValue(true);
            createErrorDialog("Elevation Gain vs Time Plotting Error", "No tracks are loaded.");
        }
    }

    /**
     * graphs all selected tracks on a 2D plot
     */
    public void graphTwoDPlot() {
        lastGraphLoaded = "2DPlot";

        showDistanceVsTimeUnits(false);
        showHideButton.disableProperty().setValue(false);
        this.tracksHandler = gpsController.getTracksHandler();
        try {
            plotter.convertToCartesian();
        } catch (NullPointerException n) {
            showHideButton.disableProperty().setValue(true);
            createErrorDialog("2D Graph Plotting Error", "No tracks are loaded.");
        }
    }

    /**
     * graphs all selected tracks on a 2D plot
     */
    public void graphPlotSpeedAlongPath() {
        lastGraphLoaded = "SpeedPlot";
        showDistanceVsTimeUnits(false);
        showHideButton.disableProperty().setValue(false);
        tracksHandler = gpsController.getTracksHandler();
        try {
            plotter.plotSpeedOverPath();
        } catch (NullPointerException n) {
            showHideButton.disableProperty().setValue(true);
            createErrorDialog("2D Graph Plotting Error", "No tracks are loaded.");
        }
    }

    /**
     * adds the tracks passed in to the spinner to allow it to be graphed
     *
     * @param valueFactory the value factory for the spinner to add to the spinner
     * @param trackName    the name of the track to display on the spinner
     */
    public void updateSpinner(SpinnerValueFactory<String> valueFactory, String trackName) {
        trackSpinner.setValueFactory(valueFactory);
        trackSpinner.getValueFactory().setValue(trackName);
    }

    /**
     * takes the track passed in gets the name of the track and the distance in kilometers and miles to display on the table
     *
     * @param trackStats the track to get the information from to display
     */
    public void addTable(TrackStats trackStats) {
        tableView.getItems().add(trackStats);
    }

    /**
     * event that occurs when the show/Hide button is selected or unselected
     *
     * @param actionEvent the event that caused this to be called
     */
    public void showOrHide(ActionEvent actionEvent) {
        int index = gpsController.getTracksHandler().getTrackIndex(trackSpinner.getValue());
        if (index == -1) {
            createErrorDialog("Track not loaded", "The track that is to be shown is not loaded!");
        } else {
            showOnGraph[index] = !showOnGraph[index];
        }
        switch (lastGraphLoaded) {
            case "2DPlot":
                graphTwoDPlot();
                break;
            case "Elevation Gain Vs Time":
                graphElevationGainVsTime();
                break;
            case "SpeedPlot":
                graphPlotSpeedAlongPath();
                break;
            case "Distance Vs Time":
                graphDistanceVsTime();
                break;
            default:
                System.out.println("Error unrecognized graph name: " + lastGraphLoaded);
        }
    }

    /**
     * Disables show and hide checkbox for tracks
     */
    public void disableShowHideButton(){
        showHideButton.disableProperty().setValue(true);
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
     * @author Austin Demars
     * <p>
     * Called when 'File/Exit' is pressed
     * Closes (exits) the application
     */
    public void exit() {
        this.plotterStage.hide();
    }

    public NumberAxis getXAxis() {
        return this.xAxis;
    }

    public NumberAxis getYAxis() {
        return this.yAxis;
    }

    public void clearTable() {
        tableView.getItems().clear();
    }

    public void setStage(Stage stage) {
        this.plotterStage = stage;
    }

    public void setMainController(GPSController gpsController) {
        this.gpsController = gpsController;
    }

    public boolean[] getShowOnGraph() {
        return showOnGraph;
    }

    public void setTracksHandler(TracksHandler tracksHandler) {
        this.tracksHandler = tracksHandler;
    }

    public TracksHandler getTracksHandler() {
        return tracksHandler;
    }

    //Re-enables the JavaFXML legend for graphs
    public void reenableLegend() {
        lineChart.setLegendVisible(true);
        LegendText.setVisible(false);
    }

    public void setLegendTextVisible(boolean b) {
        LegendText.setVisible(b);
    }

    public void setLegendText(String message) {
        LegendText.setText(message);
    }

    public void setChartTitle(String title){
        chartTitle.setText(title);
    }

    /**
     * sets both axis to the same scale based on the variables passed in, sets the max value for both the x and y axis
     * based on what number is greater, similarly sets the x and y axis min's to which number is smaller.
     * @param xMax the max x axis value
     * @param xMin the min x axis value
     * @param yMax the max y axis value
     * @param yMin the min y axis value
     */
    public void scaleAxis(int xMax, int xMin, int yMax, int yMin){
        int tickUnit;
        int tempMax;
        int tempMin;
        int xAdjust = centerX(xMax, xMin, yMax, yMin);
        int yAdjust = centerY(xMax, xMin, yMax, yMin);
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        //added so edge cases at small points are easier to see
        if (xMax < 10){
            xMax++;
        }
        if (xMin > -10){
            xMin--;
        }
        if (yMax < 10){
            yMax++;
        }
        if (yMin > -10){
            yMin--;
        }
        if(yMax > xMax){
            xAxis.setUpperBound(yMax + xAdjust);
            yAxis.setUpperBound(yMax + yAdjust);
            tempMax = yMax;
        } else {
            xAxis.setUpperBound(xMax + xAdjust);
            yAxis.setUpperBound(xMax + yAdjust);
            tempMax = xMax;
        }
        if (yMin < xMin){
            xAxis.setLowerBound(yMin+xAdjust);
            yAxis.setLowerBound(yMin+yAdjust);
            tempMin = yMin;
        } else {
            xAxis.setLowerBound(xMin+xAdjust);
            yAxis.setLowerBound(xMin+yAdjust);
            tempMin = xMin;
        }
        tickUnit = (tempMax- tempMin)/10;
        yAxis.setTickUnit(tickUnit);
        xAxis.setTickUnit(tickUnit);
    }

    private int centerY(int xMax, int xMin, int yMax, int yMin){
        int minDiff;
        int maxDiff;
        minDiff = yMin - xMin;
        maxDiff = yMax - xMax;
        if(minDiff < 0){
            minDiff = 0;
        }
        if (maxDiff > 0){
            maxDiff = 0;
        }
        return (maxDiff + minDiff)/2;
    }

    private int centerX(int xMax, int xMin, int yMax, int yMin){
        int minDiff;
        int maxDiff;
        minDiff = xMin - yMin;
        maxDiff = xMax - yMax;
        if(minDiff < 0){
            minDiff = 0;
        }
        if (maxDiff > 0){
            maxDiff = 0;
        }
        return (maxDiff + minDiff)/2;
    }

}




