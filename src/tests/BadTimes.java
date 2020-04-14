package tests;

import gps.GPXHandler;
import gps.Parser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadTimes {

    private GPXHandler handler = new GPXHandler();
    private String filename = System.getProperty("user.dir") + "\\docs\\gpstest-bad times.txt";

    @Test
    public void testSAXExceptionIsThrown() {
        SAXException sax = assertThrows(SAXException.class,
                ()-> {
                    handler.enableLogging(true);
                    Parser parser = new Parser(handler);
                    parser.parse(filename);
                });
        assertEquals("</time> attribute is formatted incorrectly!", sax.getMessage());
        // the line and column numbers should be validated as well.
        assertEquals(1, handler.getLine()); //Checks line
        assertEquals(1, handler.getColumn()); //Checks column
    }
}
