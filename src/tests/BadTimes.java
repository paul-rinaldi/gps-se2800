package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadTimes {

    private GPXHandler handler = new GPXHandler();
    private String filename = "C:\\GPS\\docs\\gpstest-bad times.txt";

    @Test
    public void testSAXExceptionIsThrown() {
        SAXException sax = assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
        assertEquals("</time> attribute is formatted incorrectly!", sax.getMessage());
    }
}
