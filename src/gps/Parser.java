package gps;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/** This class encapsulates the setup and usage of the SAX (Simple API for XML)
 * parser supplied as part of the JAXP libraries.
 *
 * DO NOT MODIFY THE CODE IN THIS FILE.
 *
 * @author hornick
 */
public class Parser {
    private AbstractParserEventHandler parserEventHandler = null;
    private XMLReader xmlReader;

    /**
     * In this constructor, the parser is initialized and connected to its event handlers.
     * Documentation for the the underlying JAXP library classes used internally can be found at
     * http://docs.oracle.com/javase/tutorial/jaxp/TOC.html.
     * This parser uses only a subset of the entire JAXP library
     * that demonstrates the use of the SAX parser, whose documentation is found at
     * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
     * @param eventHandler the class that gets notified of parsing events as the parser processes a file.
     * The event handler must extend the AbstractParserEventHandler described elsewhere.
     * @throws NullPointerException if the eventHandler is null. The reason is supplied in the accompanying message.
     * @throws Exception if something goes wrong creating the parser (highly unlikely). Details are supplied in the accompanying message.
     */
    public Parser(AbstractParserEventHandler eventHandler) throws Exception {
        if( eventHandler == null )
            throw new NullPointerException( "Parser constructor: Event Handler reference cannot be null!");

        parserEventHandler = eventHandler;

        System.getProperty("javax.xml.parsers.SAXParserFactory"); // gets default factory
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true); // configure factory to create a namespace-aware parser
        factory.setValidating(false); // set to true if factory should create a DTD validating parser

        SAXParser saxParser = null;
        // next, we create the actual SAX parser, set up its event handlers
        try {
            saxParser = factory.newSAXParser();
            xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler( parserEventHandler );
            xmlReader.setErrorHandler(parserEventHandler);

            // configure the parser to use a particular lexical handler
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", parserEventHandler);

        } catch (ParserConfigurationException e ) {
            throw new Exception("Parser constructor: A ParserConfiguration exception occurred during construction. ParserConfiguration is: " + e.getMessage() );
        }

    }

    /**
     * This method starts the parser parsing the specified file.
     * Operation:
     * As the parser reads through the file, it encounters various lexical elements (ex the <gpx> tag).
     * It recognizes these as parsing "events".
     * The parser calls the eventHandler's methods to notify the it as each element is encountered.
     * It is up to the eventHandler to decide what to do with the notifications.
     * @param filename path of the gps file to parse
     * @throws Exception if the filename is null; details in the accompanying message
     * @throws SAXException if parse errors are encountered in the file being parsed; details in the accompanying message
     * @throws IOException if the parser encounters errors reading; details in the accompanying message
     */
    public void parse(String filename) throws Exception {
        if( filename == null )
            throw new Exception( "Parser.parse(filename): filename was null!");

        String url = convertToFileURL(filename);

        xmlReader.parse( url );
    }


    /* This method converts a filename to a formal "url" format that is needed
     * by the SAX parser (since the SAX parser can also parse input coming from
     * other non-file sources, such as a network)
     * Ex: input filename ".\\Sunday_ride.gpx" gets converted to
     * "file://D:/MyDocs/Sunday_ride.gpx"
     * @param filename the file to be parsed
     */
    private static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }

}
