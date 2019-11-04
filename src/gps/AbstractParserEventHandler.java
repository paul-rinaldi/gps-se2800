package gps;


import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

/**
 * 	DO NOT MODIFY THE CODE IN THIS ABSTRACT CLASS.
 *
 *  This is just a "convenience" class that provides default implementations
 *  of all of the various interface methods that the Parser's event handler needs
 *  to implement. The implementations here merely print the events to the console
 *  so that you can see them as they are generated.
 *
 *  You must implement a concrete class that extends this abstract class, and
 *  override the methods so that you can capture the event data of interest
 *  to your application.
 *
 *  You must pass the reference to your concrete class to the Parser constructor.
 *
 * @author hornick
 *
 */
public abstract class AbstractParserEventHandler implements ContentHandler, ErrorHandler, LexicalHandler {
    // the Locator object is created/used by the Parser to provide location (row, column) information
    // for errors in the xml file being read.
    protected Locator locator; // note that this object is inherited by concrete classes
    private String currentTag; // used for logging

    private boolean logEnabled = true;
    // This method simply logs the event information to the console. By overriding the implementation,
    // it can be made to log to a file instead.
    protected void log(String prefix, String data) {
        if( logEnabled )
            System.out.println( "DebugLog: " + prefix + ": " + data);
    }

    /*
     * This method enables/disables "debug" logging to System.out, which is enabled by default.
     * To enable/disable, call the method with true/false as an argument.
     */
    public void enableLogging( boolean enable ) {
        logEnabled = enable;
    }

    /*
     * This method retrieves the line number where an error was encountered
     * in cases where the Parser throws an exception.
     */
    public int getLine() {
        if( locator != null )
            return locator.getLineNumber();
        else
            return 0;
    }

    /*
     * This method retrieves the column number where an error was encountered
     * in cases where the Parser throws an exception.
     */
    public int getColumn() {
        if( locator != null )
            return locator.getColumnNumber();
        else
            return 0;
    }

    // Note: for complete Javadoc on the following methods, see the official
    // documentation

    // This method is called whenever the Parser is about to throw
    // a SAXException indicating a warning.
    public void warning(SAXParseException exception) throws SAXException {
        log("*warning event!", "The parser is going to throw a SAXException!");
    }

    // This method is called whenever the Parser is about to throw
    // a SAXException indicating an error.
    public void error(SAXParseException exception) throws SAXException {
        log("**error event!", "The parser is going to throw a SAXException!");
    }

    // This method is called whenever the Parser is about to throw
    // a SAXException indicating a fatal error.
    public void fatalError(SAXParseException exception) throws SAXException {
        log("**fatalerror event!", "The parser is going to throw a SAXException!");
    }

    //ContentHandler method
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        //log( "setDocumentLocator ", "Line:"+locator.getLineNumber()+", Col:"+locator.getColumnNumber() );
    }

    @Override
    //ContentHandler method
    public void startDocument() throws SAXException {
        log("startDocument event!", "start of xml document");
    }

    @Override
    //ContentHandler method
    public void endDocument() throws SAXException {
        log("endDocument event!", "end of xml document");
    }

    @Override
    // This ContentHandler method is called whenever the Parser encounters a new element tag
    // which may or may not have Attributes. For example,
    // <trkpt lat="43.30627219" lon="-87.98892299">
    // contains two attributes (lat and lon).
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        currentTag = localName;
        log("startElement found", "<"+localName+">" );
        String attributes = "none";
        if( atts.getLength()> 0 ) { // check for element attributes; print them only if they exist.
            attributes = "";
            for( int index=0; index<atts.getLength(); index++) {
                attributes += " "+ atts.getLocalName(index) + "=" + atts.getValue(index);
            }
            log("<"+localName+"> attributes", attributes);
        }
    }

    @Override
    // This ContentHandler method is called whenever the Parser encounters an END tag (e.g. </trkpt>)
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        log("endElement found", "</"+localName+">");
    }

    @Override
    // This ContentHandler method is called whenever the Parser encounters text between tags.
    // For example, for the tag <ele>249.5</ele>, the characters "249.5" are reported.
    public void characters(char[] ch, int start, int length)
            throws SAXException {
//		String chars = "";
//
//		for( int index=start; index<start+length; index++) {
//				chars += ch[index];
//		}
        String s = new String(ch);
        s = s.substring(start, start+length);
        s = s.trim(); // remove leading and trailing whitespace

        if( !s.isEmpty() ) // only whitespace was found, and ignored
            log("characters found between <"+currentTag+">...</"+currentTag+">", s );
    }


    @Override
    //ContentHandler method
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("ContentHandler startPrefixMapping", "" );
    }

    @Override
    //ContentHandler method
    public void endPrefixMapping(String prefix) throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("ContentHandler endPrefixMapping", "" );
    }

    @Override
    //ContentHandler method
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("ContentHandler ignorableWhitespace (c, start, length)", c + ","+ start + "," + length);
    }

    @Override
    //ContentHandler method
    public void processingInstruction(String target, String data)
            throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("ContentHandler processingInstruction (target, data)", target + "," + data);
    }

    @Override
    //ContentHandler method
    public void skippedEntity(String name) throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("ContentHandler skippedEntity (name)", name);
    }

    @Override
    // LexicalHandler method
    public void startDTD(String name, String publicId, String systemId)
            throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("LexicalHandler startDTD(name,publicId, systemId)", name + "," +publicId + "," + systemId );
    }

    @Override
    // LexicalHandler method
    public void endDTD() throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("LexicalHandler endDTD", "" );
    }

    @Override
    // LexicalHandler method
    public void startEntity(String name) throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("LexicalHandler startEntity(name)", name );
    }

    @Override
    // LexicalHandler method
    public void endEntity(String name) throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("LexicalHandler endEntity(name)", name );
    }

    @Override
    // LexicalHandler method
    public void startCDATA() throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("LexicalHandler startCDATA", "" );
    }

    @Override
    // LexicalHandler method
    public void endCDATA() throws SAXException {
        //Null implementation provided; this event can be ignored
        //log("LexicalHandler endCDATA", "" );
    }

    @Override
    // This LexicalHandler method is called whenever the Parser encounters a comment <!-- like this -->
    public void comment(char[] ch, int start, int length) throws SAXException {
        String c = "<!--";
        for( int index=start; index<start+length; index++)
            c += ch[index];
        c += ">";
        log("comment event!", c );
    }


}
