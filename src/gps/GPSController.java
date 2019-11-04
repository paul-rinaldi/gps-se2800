package gps;


import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;

import java.nio.file.Path;

/**
 * @author demarsa
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class GPSController {

	private MenuItem loadButton;
	private GPXParser parser;
	private MenuItem statsButton;
	private TracksHandler tracksHandler;
	private Spinner trackSpinner;
	private GPXParser gpxParser;

	public GPSController(){

	}

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

	}

	/**
	 * 
	 * @param filename
	 */
	public void parseTrackFile(Path filename){

	}

}