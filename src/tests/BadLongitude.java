package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadLongitude {

    private GPXHandler handler = new GPXHandler();
    private String filename = "gpstest-bad longitudes.txt";

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
    }
}
