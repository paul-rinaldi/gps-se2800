package gps_plotter;

import gps.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlotterController {

    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private TableView tableView;
    private GPSController gpsController;
    Plotter plotter = new Plotter(lineChart, this);
    private TracksHandler tracksHandler;

    @FXML
    public void initialize(){
        //sets up the table so it can be populated
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

        Track t = gpsController.getTracksHandler().getTrack(gpsController.getSpinner().getValue());
        this.plotter.plotElevationGain(t);
    }

    public void setTracksHandler(TracksHandler tracksHandler){
        this.tracksHandler = tracksHandler;
    }

    public TracksHandler getTracksHandler(){
        return tracksHandler;
    }

    public void graphTwoDPlot(XYChart.Series series){
        lineChart.getData().add(series);
    }

    public void addTable(TrackStats trackStats){
        tableView.getItems().add(trackStats);
    }

    public Plotter getPlotter(){
        return plotter;
    }
}
