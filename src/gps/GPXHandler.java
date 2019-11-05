package gps;


import java.util.jar.Attributes;

/**
 * @author demarsa
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class GPXHandler extends AbstractParserEventHandler {

	private TracksHandler tracksHandler;

	public GPXHandler(){
		super();
		tracksHandler = new TracksHandler();
	}

	/**
	 * 
	 * @param ch
	 * @param start
	 * @param length
	 */
	public void characters(char [] ch, int start, int length){

	}

	public void endDocument(){

	}

	/**
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 */
	public void endElement(String uri, String localName, String qName){

	}

	public TracksHandler getTrackHandler(){
		return tracksHandler;
	}

	/**
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts){

	}

}