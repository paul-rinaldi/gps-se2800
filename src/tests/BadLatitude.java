package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadLatitude {

    private GPXHandler handler = new GPXHandler();
    private String filename = "C:\\GPS\\docs\\gpstest-bad latitudes.txt";

    @Test
    public void testSAXExceptionIsThrown() {
        SAXException sax = assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
        assertEquals("Invalid value for latitude! Latitude must be between -90 and 90 degrees, " +
                "it was found to be -90.1", sax.getMessage());
    }
}
