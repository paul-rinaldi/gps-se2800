package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadTimes {

    private GPXHandler handler = new GPXHandler();
    private String filename = "gpstest-bad times.txt";

    @Test
    public void testSAXExceptionIsThrown() {
        assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
    }
}
