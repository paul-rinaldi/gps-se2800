package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissingLatitude {

    private GPXHandler handler = new GPXHandler();
    private String filename = "gpstest-missing latiitude.txt";

    @Test
    public void testSAXExceptionIsThrown() {
       SAXException sax = assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
       assertEquals("<trkpt> element has an illegal number of attributes: 1", sax.getMessage());
    }
}
