// paul rinaldi branch -- 
package gps;


import gps_plotter.PlotterController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
	private PlotterController plotterController;
	private Stage plotterStage;

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
	private void calcTrackStats(){

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
	private void displayTrackStats(){
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
	private void displayTrackMinMaxValues(TrackStats stats){

		double maxLati = stats.getMaxLat();
		double minLati = stats.getMinLat();
		double maxLongi = stats.getMaxLong();
		double minLongi = stats.getMinLong();
		double maxElevM = stats.getMaxElevM();
		double minElevM = stats.getMinElevM();
		double maxElevFt = stats.getMaxElevFt();
		double minElevFt = stats.getMinElevFt();

		if(maxLati > Double.MAX_VALUE*-1){
			maxLat.setText(Double.toString(maxLati));
		} else {
			maxLat.setText("N/A");
		}

		if(minLati < Double.MAX_VALUE){
			minLat.setText(Double.toString(minLati));
		} else{
			minLat.setText("N/A");
		}

		if(maxLongi > Double.MAX_VALUE*-1){
			maxLong.setText(Double.toString(maxLongi));
		} else {
			maxLong.setText("N/A");
		}

		if(minLongi < Double.MAX_VALUE){
			minLong.setText(Double.toString(minLongi));
		} else {
			minLong.setText("N/A");
		}

		if(maxElevM > Double.MAX_VALUE*-1) {
			maxElevMet.setText(Double.toString(maxElevM));
		} else{
			maxElevMet.setText("N/A");
		}

		if(minElevM < Double.MAX_VALUE) {
			minElevMet.setText(Double.toString(minElevM));
		} else {
			minElevMet.setText("N/A");
		}

		if(maxElevFt > Double.MAX_VALUE*-1) {
			maxElevFeet.setText(Double.toString(maxElevFt));
		} else{
			maxElevFeet.setText("N/A");
		}

		if(minElevFt < Double.MAX_VALUE) {
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
	private void displayTrackDistancesSpeeds(TrackStats stats){

		double distMiles = stats.getDistM();
		double distKilo = stats.getDistK();
		double avSpeedM = stats.getAvgSpeedM();
		double avSpeedK = stats.getAvgSpeedK();
		double maxSpeedM = stats.getMaxSpeedM();
		double maxSpeedK = stats.getMaxSpeedK();

		if(distMiles == 0) {
			dMiles.setText("N/A");
		} else{
			dMiles.setText(Double.toString(stats.getDistM()));
		}

		if(distKilo == 0){
			dKilometers.setText("N/A");
		} else {
			dKilometers.setText(Double.toString(stats.getDistK()));
		}

		if(avSpeedM == 0){
			avSpeedMPH.setText("N/A");
		} else {
			avSpeedMPH.setText(Double.toString(stats.getAvgSpeedM()));
		}

		if(avSpeedK == 0){
			avSpeedKPH.setText("N/A");
		} else {
			avSpeedKPH.setText(Double.toString(stats.getAvgSpeedK()));
		}

		if(maxSpeedM == 0){
			maxSpeedMPH.setText("N/A");
		} else {
			maxSpeedMPH.setText(Double.toString(stats.getMaxSpeedM()));
		}

		if(maxSpeedK == 0){
			maxSpeedKPH.setText("N/A");
		} else {
			maxSpeedKPH.setText(Double.toString(stats.getMaxSpeedK()));
		}


	}

	/**
	 * Clears displayed stats of a track
	 */
	private void clearStatsDisplay(){
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

		try {

			if(tracksRemaining != 0) {

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

				}

			} else {
				//Inform them their tracks have reached maximum
				createInfoDialog("Maximum Tracks Loaded",
						"You have loaded the maximum allowable tracks");
			}

		} catch (SAXException e) {

			createErrorDialog("Parsing Error", e.getLocalizedMessage() +
					"\nThe error occurred near line " +
						handler.getLine() + ", col "+ handler.getColumn());

			((GPXHandler)handler).resetAttributes();

		} catch (UnsupportedOperationException uoe) {

			createInfoDialog("Track with name already exists", uoe.getLocalizedMessage());

			((GPXHandler)handler).resetAttributes();

		} catch (Exception e) {
			createErrorDialog("Parsing Error", e.getLocalizedMessage());
			((GPXHandler)handler).resetAttributes();
		}



	}

	/**
	 * Uses Parser class to parse the GPS file selected by the user
	 * (Used inside of loadTrack)
	 *
	 * @param filename String filename of GPS file selected
	 * @exception SAXException thrown if parser encounters an exception
	 * @exception Exception thrown when any other exception is caught from the parser
	 */
	private void parseTrackFile(String filename) throws SAXException, Exception{

		handler.enableLogging(false);

		Parser parser;
		try {
			parser = new Parser( handler );
			parser.parse(filename);

			if(tracksRemaining == 10) {
				tracksHandler = ((GPXHandler) handler).getTrackHandler();
			}

		} catch (SAXException e) {
			throw e; //Throws sax
		} catch (Exception e) {
			throw e;
		}


	}

	public TracksHandler getTracksHandler(){
		return this.tracksHandler;
	}

	public void setPlotterController(PlotterController plotterController){
		this.plotterController = plotterController;
	}

	public void setPlotterStage(Stage stage){
		this.plotterStage = stage;
	}

	/**
	 * Opens plotter window and immediately graphs all loaded tracks with 2D Plotter in cartesian coordinates
	 * Only graphs selected tracks if window was already modified (saves state)
	 */
	public void showPlotter(){
		plotterController.setTracksHandler(tracksHandler);
		plotterStage.show();
		plotterController.graphTwoDPlot();
	}

	/**
	 * Opens plotter window and immediately graphs all loaded tracks with ElevationGain vs Time
	 * Only graphs selected tracks if window was already modified (saves state)
	 */
	public void showElevationGainVsTime(){
		plotterController.setTracksHandler(tracksHandler);
		plotterStage.show();
		plotterController.graphElevationGainVsTime();
	}

	/**
<<<<<<< HEAD
	 * Opens plotter window and immediately graphs all loaded tracks with ElevationGain vs Time
=======
	 * Opens plotter window and immediately graphs all loaded tracks with Plot Speed Along Path
>>>>>>> 3e454af644ebe33d6bb6cf1143efa0f8bc27c9ae
	 * Only graphs selected tracks if window was already modified (saves state)
	 */
	public void showSpeedPlot(){
		plotterController.setTracksHandler(tracksHandler);
		plotterStage.show();
		plotterController.graphPlotSpeedAlongPath();
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
