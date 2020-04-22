package gps;


import gps_plotter.PlotterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * Main class of the program. Initializes controller and FXML
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
	}



	public static void main(String[] args) {
		launch(args);
	}
}