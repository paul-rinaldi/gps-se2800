package gps;


import javafx.application.Platform;
import javafx.scene.control.Spinner;
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
	private Spinner trackSpinner;
	private AbstractParserEventHandler handler = new GPXHandler();
	private int tracksLoaded = 0;


	public void calcTrackStats(){

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

<<<<<<< HEAD
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));


		File inputFile = chooser.showOpenDialog(null);

		if(inputFile != null) {

			parseTrackFile(inputFile.getAbsolutePath());
			tracksLoaded ++;


=======
		if(tracksLoaded != 10) {

			FileChooser chooser = new FileChooser();
			chooser.setInitialDirectory(new File(System.getProperty("user.dir")));


			File inputFile = chooser.showOpenDialog(null);

			if (inputFile != null) {

				parseTrackFile(inputFile.getAbsolutePath());
				tracksLoaded++;


			}
		} else {
			//Inform them their tracks have reached maximum
>>>>>>> 0e13ed8cb2586f9195a6d456d3d33ec6cba09952
		}

	}

	/**
	 * 
	 * @param filename
	 */
	private void parseTrackFile(String filename){

		handler.enableLogging(true); // enable debug logging to System.out

		Parser parser = null;
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

		if(tracksLoaded == 0) {
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