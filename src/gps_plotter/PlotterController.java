package gps_plotter;

import gps.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class PlotterController {

    @FXML
    private LineChart<Double, Double> lineChart;
    private GPSController gpsController;
    Plotter plotter = new Plotter(lineChart);

    public void setMainController(GPSController gpsController){
        this.gpsController = gpsController;
    }

    public void graphElevationGainVsTime(){

        Track t = gpsController.getTracksHandler().getTrack(gpsController.getSpinner().getValue());
        this.plotter.plotElevationGain(t);
    }

}
