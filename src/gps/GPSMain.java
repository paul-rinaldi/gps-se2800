package gps;


import gps_plotter.PlotterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maps.CaptureController;

/**
 * Main class of the program. Initializes controller and FXML
 * Ensure that vm options are:
 * --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.fxml,javafx.web
 * For all javafx components to run correctly.
 */
public class GPSMain extends Application {

	@Override
	public void start(Stage stage) throws Exception{
		Parent rootGPS;
		FXMLLoader gpsLoader = new FXMLLoader(getClass().getResource("GPSFXML.fxml"));
		rootGPS = gpsLoader.load();
		Stage gpsWindow = new Stage();
		gpsWindow.setTitle("GPS App");
		gpsWindow.setScene(new Scene(rootGPS));
		gpsWindow.show();
		GPSController gpsController = gpsLoader.getController();

		//Set up Plotter window
		Parent rootPlotter;
		FXMLLoader plotterLoader = new FXMLLoader(getClass().getResource("../gps_plotter/PlotterFXML.fxml"));
		rootPlotter = plotterLoader.load();
		Stage plotterWindow = new Stage();
		PlotterController plotterController = plotterLoader.getController();
		plotterWindow.setTitle("Plotter");
		plotterWindow.setScene(new Scene(rootPlotter));
		plotterWindow.hide();
		plotterController.setStage(plotterWindow);

		gpsController.setPlotterController(plotterController);
		gpsController.setPlotterStage(plotterWindow);
		plotterController.setMainController(gpsController);


		//Set up Google maps window
        Parent rootMaps;
        FXMLLoader mapsLoader = new FXMLLoader(getClass().getResource("../maps/Capture.fxml"));
        rootMaps = mapsLoader.load();
        Stage mapsWindow = new Stage();
        CaptureController captureController = mapsLoader.getController();
        mapsWindow.setTitle("Google Maps");
        mapsWindow.setScene(new Scene(rootMaps));
        mapsWindow.hide();
        captureController.setStage(mapsWindow);

        gpsController.setMapsController(captureController);
        gpsController.setMapsStage(mapsWindow);
        captureController.setMainController(gpsController);
	}



	public static void main(String[] args) {
		launch(args);
	}
}