package gps;


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
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("GPSFXML.fxml"));
		primaryStage.setTitle("GPS App");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}