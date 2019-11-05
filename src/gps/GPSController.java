package gps;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import java.io.File;

/**
 * @author demarsa
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class GPSController {

	private TracksHandler tracksHandler;
	private AbstractParserEventHandler handler = new GPXHandler();
	private int tracksRemaining = 10;

	@FXML
	private TextField trackName;
	@FXML
	private Spinner<Integer> trackSpinner;


	public void changeTrackSelected() {

		if (tracksHandler != null) {
			int tracksLoaded = tracksHandler.getTrackAmount();
			int trackSelected = trackSpinner.getValue();

			if (trackSelected <= tracksLoaded) {
				trackName.setText(tracksHandler.getTrack(trackSelected - 1).getName());
			}
		}

	}

	public void calcTrackStats(){
		tracksHandler.calculateTrackStats(trackSpinner.getValue().toString());
		displayTrackStats();
	}

	private void displayTrackStats(){
		Track track = tracksHandler.getTrack(trackSpinner.getValue().toString());
	}

	/**
	 * 
	 * @param errorType
	 * @param errorMsg
	 */
	private void createErrorDialog(String errorType, String errorMsg){

	}

	/**
	 * 
	 * @param infoType
	 * @param infoMsg
	 */
	private void createInfoDialog(String infoType, String infoMsg){

	}

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

				if(tracksRemaining == 9){
					trackName.setText(trackLoaded.getName());
				}


			}
		} else {
			//Inform them their tracks have reached maximum
		}

	}

	/**
	 * 
	 * @param filename
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
		} catch (Exception e) {
			// something went wrong when initializing the Parser; print to the console since this is a console demo app.
			System.out.println("ParserDemoApp: Parser threw Exception: " + e.getMessage() );
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