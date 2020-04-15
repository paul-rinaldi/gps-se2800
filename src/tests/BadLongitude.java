package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadLongitude {

    private GPXHandler handler = new GPXHandler();
    private String filename = System.getProperty("user.dir") + "\\docs\\gpstest-bad longitudes.txt";

    @Test
    public void testSAXExceptionIsThrown() {
        SAXException sax = assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
        assertEquals("Invalid value for longitude! Longitude must be between -180 and 180 degrees, " +
                "it was found to be -180.1", sax.getMessage());
        // the line and column numbers should be validated as well.
        assertEquals(11, handler.getLine()); //Checks line
        assertEquals(61, handler.getColumn()); //Checks column
    }
}
