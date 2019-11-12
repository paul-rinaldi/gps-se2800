package gps;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.SimpleDateFormat;


/**
 * handles the events that the parser encounters and creates track points and tracks from the information in the file.
 *
 * @author Hunter Hess
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class GPXHandler extends AbstractParserEventHandler {

	private enum PossibleStates {INITIAL, GPX, TRK, NAME, TRKSEG, TRKPT, ELE, TIME, FINAL}
	private PossibleStates currentState = PossibleStates.INITIAL;

	private TracksHandler tracksHandler;
	private String name;
	private ArrayList<TrackPoint> TrackPointList;
	private double latitude;
	private double longitude;
	private Date time;
	private double elevation;
	private SimpleDateFormat simpleDateFormat;

	public GPXHandler(){
		super();
		tracksHandler = new TracksHandler();
		TrackPointList = new ArrayList<>();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		latitude = 0;
		longitude = 0;
		elevation = -10000;
		time = null;
	}

	/**
	 * Allows the this class to reset attributes if a track was loaded successfully or
	 * allows the controller to call this to reset attributes if an error is thrown
	 */
	public void resetAttributes(){
		TrackPointList = new ArrayList<>();
		latitude = 0;
		longitude = 0;
		elevation = -10000;
		time = null;
		currentState = PossibleStates.INITIAL;
	}

	/**
	 *  gets the characters between open and end tags
	 *
	 * @param ch an array of characters between the start and end tags
	 * @param start the start index of the array
	 * @param length the length of the array
	 */
	public void characters(char [] ch, int start, int length) throws SAXException{
		// convert the char[] array to a String for convenience
		String s = new String(ch);
		s = s.substring(start, start+length);
		s = s.trim(); // remove leading and trailing whitespace

		if( !s.isEmpty() ) { // ignore whitespace
			int line = locator.getLineNumber(); // current line being parsed
			int column = locator.getColumnNumber(); // current column being parsed
		}

		if( currentState == PossibleStates.NAME ) { // We're in the NAME state, so these are the chars found between <name> and </name>
			if(s.isEmpty()){
				throw new SAXException("</name> attribute is formatted incorrectly");
			}
			name = s;
		}

		if( currentState == PossibleStates.TIME ) {// We're in the TIME state, so these are the chars found between <time> and </time>
			try {
				this.time = simpleDateFormat.parse(s); // set the current Student object's time attribute
			} catch (ParseException e) {
				throw new SAXException("</time> attribute is formatted incorrectly!");
			}
		}
		if( currentState == PossibleStates.ELE ) {// We're in the ELE state, so these are the chars found between <ele> and </ele>
			if( s.isEmpty() )
				throw new SAXException("</ele> attribute is empty!");
			elevation = Double.parseDouble(s);
		}
	}

	/**
	 * called when the end of a document is reached
	 *
	 * @throws SAXException if final is not the last state of the document
	 */
	public void endDocument() throws SAXException {
		if( currentState != PossibleStates.FINAL ){
			throw new SAXException("Document structure error. Not in FINAL state at the end of the document!");
		} else {

			tracksHandler.addTrack(new Track(name, TrackPointList));
			resetAttributes();
		}
	}

	/**
	 * handles end element tags and makes sure that they are encountered in the correct order
	 *
	 * @param uri the name of the file being parsed
	 * @param localName the state that the parser is in when it finds an end element
	 * @throws SAXException if end element is encountered out of order
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException{
		int line = locator.getLineNumber(); // current line being parsed
		int column = locator.getColumnNumber(); // current column being parsed

		// localName contains the name of the element - e.g. gpx, name, trk, etc
		if( localName.equalsIgnoreCase("time") ) {
			if( currentState != PossibleStates.TIME ) { // should be back in TIME state when </time> is encountered
				throw new SAXException("</time> element found in illegal location! "+line + ", col "+column );
			}
			currentState = PossibleStates.TRKPT; // </time> signals that we're back in the TRKPT state!
		}

		if( localName.equalsIgnoreCase("ele") ) {
			if( currentState != PossibleStates.ELE ) { // should be back in ELE state when </ele> is encountered
				throw new SAXException("</ele> element found in illegal location!");
			}
			currentState = PossibleStates.TRKPT; // </ELE> signals that we're back in the TRKPT state!
		}


		if( localName.equalsIgnoreCase("trkpt") ) {
			if( currentState != PossibleStates.TRKPT ) { // should be back in TRKPT state when </trkpt> is encountered
				throw new SAXException("</trkpt> element found in illegal location!");
			}
			// We must make sure that we've completed the tracks object - that is, the <time> and <ele>
			// elements must have been encountered and their values must have been added to the trkpt object.
			if(time == null) // null means that the time was never set!
				throw new SAXException("<trkpt> element is missing a <time> subelement or attribute! line "+line + ", col "+column );
			if( elevation == -10000) // -10000 means that the elevation was never set!
				throw new SAXException("<trkpt> element is missing an <ele> subelement or attribute! line "+line + ", col "+column );

			// if everything is OK, add the Student to the list!
			TrackPointList.add(new TrackPoint(latitude, longitude, elevation, time));
			latitude = 0;
			longitude = 0;
			elevation = -10000;
			time = null;

			currentState = PossibleStates.TRKSEG; // </trkseg> signals that we're back in the TRK state!
		}

		if( localName.equalsIgnoreCase("trkseg") ) {
			if( currentState != PossibleStates.TRKSEG ) { // should be back in TRKSEG state when </trkseg> is encountered
				throw new SAXException("</trkseg> element found in illegal location!");
			}
			currentState = PossibleStates.TRK; // </trkseg> signals that we're back in the TRK state!
		}

		if( localName.equalsIgnoreCase("name") ) {
			if( currentState != PossibleStates.NAME ) { // should be back in NAME state when </name> is encountered
				throw new SAXException("</name> element found in illegal location!");
			}
			currentState = PossibleStates.TRK; // </name> signals that we're back in the TRKSEG state!
		}

		if( localName.equalsIgnoreCase("trk") ) {
			if( currentState != PossibleStates.TRK ) { // should be back in TRK state when </trk> is encountered
				throw new SAXException("</trk> element found in illegal location!");
			}
			currentState = PossibleStates.GPX; // </trk> signals that we're back in the GPX state!
		}

		if( localName.equalsIgnoreCase("gpx") ) {
			if( currentState != PossibleStates.GPX ) { // should be back in GPX state when </gpx> is encountered
				throw new SAXException("</gpx> element found in illegal location!");
			}
			currentState = PossibleStates.FINAL; // </gpx> signals that we've reached the end state!
			// When the FINAL state is reached, our collection of tracks should be complete.
		}
	}

	/**
	 * handles start element tags
	 *
	 * @param uri the uri of the file to parse
	 * @param localName the name to give the file
	 * @param atts the attributes being parsed
	 * @throws SAXException if start element is encountered out of order
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		int line = locator.getLineNumber(); // current line being parsed
		int column = locator.getColumnNumber(); // current column being parsed

		if( currentState == PossibleStates.INITIAL ) {
			if( !localName.equalsIgnoreCase("gpx") ) { // <gpx> should be the first element found
				throw new SAXException("Expected <gpx> element at this location! line "+line + ", col "+column);
				// We must make sure that no other element besides <gpx> is at the start of the xml file;
				// if some other element is found, we have an invalid file, so throw a SAXException.
			}
			currentState = PossibleStates.GPX; // once <gpx> is found, we're in the GPX state!
		}

		if( localName.equalsIgnoreCase("trk")) {
			if( currentState != PossibleStates.GPX ) { // <trk> should only be found within <gpx>
				throw new SAXException("<trk> element found in illegal location!");
			}
			currentState = PossibleStates.TRK; // once <trk> is found, we're in the TRK state!
		}

		if( localName.equalsIgnoreCase("name")) {
			if( currentState != PossibleStates.TRK) { // <name> should only be found within <trk>
				throw new SAXException("<name> element found in illegal location!");
			}
			currentState = PossibleStates.NAME; // once <name> is found, we're in the NAME state!
		}

		if( localName.equalsIgnoreCase("trkseg")) {
			if( currentState != PossibleStates.TRK) { // <trkseg> should only be found within <trk>
				throw new SAXException("<trkseg> element found in illegal location!");
			}
			currentState = PossibleStates.TRKSEG; // once <trkseg> is found, we're in the TRKSEG state!
		}

		if( localName.equalsIgnoreCase("trkpt")) {
			if( currentState != PossibleStates.TRKSEG ) { // <trkpt> should only be found within <trkseg>
				throw new SAXException("<trkpt> element found in illegal location!");
			}
			// We must advance the state to STUDENT here.
			currentState = PossibleStates.TRKPT; // once <trkpt> is found, we're in the TRKPT state!


			// Every <trkpt> element should have 2 attributes (lat and lon)
			if( atts.getLength() != 2 ) {
				throw new SAXException("<trkpt> element has an illegal number of attributes: " + atts.getLength() );
			}

			// We must check to make sure that the <tkpt> element has both lat and lon
			// attributes, so that we can set those values in the Student constructor below.
			// If either of those attributes don't exist or are invalid, we throw a SAXException.
			String firstAtt = atts.getLocalName(0); // should be "lat" or "lon"
			String firstAttValue = atts.getValue(0);
			String secondAtt = atts.getLocalName(1); // should be "lat" or "lon"
			String secondAttValue = atts.getValue(1);

			Double latitude, longitude;

			// attributes in normal order
			if( firstAtt.equalsIgnoreCase("lat") &&  secondAtt.equalsIgnoreCase("lon") ) {
				latitude = Double.parseDouble(firstAttValue);
				longitude = Double.parseDouble(secondAttValue);
			}
			// attributes in reverse order
			else if( firstAtt.equalsIgnoreCase("lon") &&  secondAtt.equalsIgnoreCase("lat") ) {
				latitude = Double.parseDouble(secondAttValue);
				longitude = Double.parseDouble(firstAttValue);
			}
			// incorrect attributes!
			else
				throw new SAXException("<trkpt> element has one or more illegal attributes: " + firstAtt + ":" + secondAtt );

			//checks to make sure that latitude and longitude are valid values
			if(-90>latitude || latitude>90) {
				throw new SAXException("Invalid value for latitude! Latitude must be between -90 and 90 degrees, " +
						"it was found to be " + latitude);
			} else if(-180>longitude || longitude>180){
				throw new SAXException("Invalid value for longitude! Longitude must be between -180 and 180 degrees, " +
						"it was found to be " + longitude);
			} else {
				// We can now set latitude and longitude values because we have valid attributes.
				this.latitude = latitude;
				this.longitude = longitude;
			}

			// We will set the ele and time and create a new trackpoint to add the the list of trackpoints when the
			// characters() method (below) is called by the Parser.
		}
		if( localName.equalsIgnoreCase("ele")) {
			if( currentState != PossibleStates.TRKPT ) { // <ele> should only be found within <trkpt>
				throw new SAXException("<ele> element found in illegal location!");
			}
			currentState = PossibleStates.ELE; // once <ele> is found, we're in the ELE state!
		}
		if( localName.equalsIgnoreCase("time")) {
			if( currentState != PossibleStates.TRKPT ) { // <time> should only be found within <trkpt>
				throw new SAXException("<time> element found in illegal location!");
			}
			currentState = PossibleStates.TIME; // once <time> is found, we're in the TIME state!
		}
	}

	public TracksHandler getTrackHandler(){
		return tracksHandler;
	}
}