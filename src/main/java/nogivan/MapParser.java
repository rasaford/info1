package nogivan;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * Diese Klasse erlaubt es, aus einer Datei im OSM-Format ein MapGraph-Objekt zu erzeugen. Sie nutzt
 * dazu einen XML-Parser.
 */
public class MapParser {

  public static MapGraph parseFile(String fileName) {
    File inputFile = new File(fileName);
    SAXParserFactory factory = SAXParserFactory.newInstance();
    OSMHandler handler = new OSMHandler();
    try {
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(inputFile, handler);
    } catch (IOException e) {
      System.err.println(e);
    } catch (ParserConfigurationException | SAXException e) {
      System.err.println(e);
    }
    return handler.getGraph();
  }
}
