package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissingElevation {

    private GPXHandler handler = new GPXHandler();
    private String filename = "gpstest-missing elevation.txt";

    @Test
    public void testSAXExceptionIsThrown() {
        SAXException sax = assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
        assertEquals("<trkpt> element is missing an <ele> subelement or attribute! line 17, col 15", sax.getMessage());
    }
}
