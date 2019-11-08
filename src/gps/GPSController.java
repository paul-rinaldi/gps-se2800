package gps;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import java.io.File;

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
	private SpinnerValueFactory<String> valueFactory;

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
	private TextField maxElev;
	@FXML
	private TextField minElev;
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

	public GPSController(){
		trackNames = FXCollections.observableArrayList();
	}

	/**
	 * Changes the displayed stats to whatever track is selected if the stats aren't null
	 */
	public void changeTrackStatDisplay() {

		if (tracksHandler != null) {

			if(tracksHandler.getTrack(trackSpinner.getValue()).getTrackStats() != null){
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
	public void calcTrackStats(){

		if(tracksHandler != null) {

			String test = trackSpinner.getValue();

			if (tracksHandler.getTrack(test).getTrackStats() == null) {
				tracksHandler.calculateTrackStats(trackSpinner.getValue());
				displayTrackStats();
			}
		}
	}

	/**
	 * Gets the currently selected track in the Spinner and displays all the calculated metrics.
	 */
	private void displayTrackStats(){
		Track track = tracksHandler.getTrack(trackSpinner.getValue());
		TrackStats stats = track.getTrackStats();

		//Set stats in boxes
		maxLat.setText(Double.toString(stats.getMaxLat()));
		minLat.setText(Double.toString(stats.getMinLat()));
		maxLong.setText(Double.toString(stats.getMaxLong()));
		minLong.setText(Double.toString(stats.getMinLong()));
		maxElev.setText(Double.toString(stats.getMaxElev()));
		minElev.setText(Double.toString(stats.getMinElev()));
		dMiles.setText(Double.toString(stats.getDistM()));
		dKilometers.setText(Double.toString(stats.getDistK()));
		avSpeedMPH.setText(Double.toString(stats.getAvgSpeedM()));
		avSpeedKPH.setText(Double.toString(stats.getAvgSpeedK()));
		maxSpeedMPH.setText(Double.toString(stats.getMaxSpeedM()));
		maxSpeedKPH.setText(Double.toString(stats.getMaxSpeedK()));

	}

	/**
	 * Clears displayed stats of a track
	 */
	private void clearStatsDisplay(){
		maxLat.setText("");
		minLat.setText("");
		maxLong.setText("");
		minLong.setText("");
		maxElev.setText("");
		minElev.setText("");
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
	 * Creates an information dialog to show the user
	 *
	 * @param infoType type of information provided
	 * @param infoMsg information message provided
	 */
	private void createInfoDialog(String infoType, String infoMsg){

		Alert headerError = new Alert(Alert.AlertType.INFORMATION);
		headerError.setTitle("Information Dialog");
		headerError.setHeaderText(infoType);
		headerError.setContentText(infoMsg);
		headerError.showAndWait();

	}

	/**
	 * Loads a track by using a GPS file selected by the user
	 */
	public void loadTrack(){

		if(tracksRemaining != 0) {

			FileChooser chooser = new FileChooser();
			chooser.setInitialDirectory(new File(System.getProperty("user.dir")));


			File inputFile = chooser.showOpenDialog(null);

			if (inputFile != null) {

				parseTrackFile(inputFile.getAbsolutePath());
				tracksRemaining--;

				//This takes the most recently added track and displays its name and points loaded
				int trackIdx = tracksHandler.getTrackAmount()-1;
				Track trackLoaded = tracksHandler.getTrack(trackIdx);
				createInfoDialog("Track Successfully Created",
						"Name of track: " + trackLoaded.getName()
								+ "\nPoints loaded: " + trackLoaded.getPointAmount());


				tracksRemainingBox.setText(Integer.toString(tracksRemaining));
				trackNames.add(trackLoaded.getName());

				valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(trackNames);
				trackSpinner.setValueFactory(valueFactory);


			}
		} else {
			//Inform them their tracks have reached maximum
			createInfoDialog("Maximum Tracks Loaded",
					"You have loaded the maximum allowable tracks");
		}

	}

	/**
	 * Uses Parser class to parse the GPS file selected by the user
	 * (Used inside of loadTrack)
	 *
	 * @param filename String filename of GPS file selected
	 */
	private void parseTrackFile(String filename){

		handler.enableLogging(true); // enable debug logging to System.out

		Parser parser;
		try {
			parser = new Parser( handler ); // create the Parser
			parser.parse(filename);		// parse a file!
		} catch (SAXException e) {
			// something went wrong during parsing; print to the console since this is a console demo app.
			System.out.println("ParserDemoApp: Parser threw SAXException: " + e.getMessage() );
			System.out.println("The error occurred near line " + handler.getLine() + ", col "+ handler.getColumn());

			createErrorDialog("Parsing Error", e.getLocalizedMessage() +
					"\nThe error occurred near line " + handler.getLine() + ", col "+ handler.getColumn());
		} catch (Exception e) {
			// something went wrong when initializing the Parser; print to the console since this is a console demo app.
			System.out.println("ParserDemoApp: Parser threw Exception: " + e.getMessage() );

			createErrorDialog("Parsing Error", e.getLocalizedMessage());
		}

		if(tracksRemaining == 10) {
			tracksHandler = ((GPXHandler) handler).getTrackHandler();
		}

	}

	/**
	 * @author Austin Demars
	 *
	 * Called when 'File/Exit' is pressed
	 * Closes (exits) the application
	 */
	public void exit() { //Austin
		Platform.exit();
	}

}