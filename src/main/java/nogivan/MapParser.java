package nogivan;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
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
      Instant start = Instant.now();
      saxParser.parse(inputFile, handler);
      System.out.printf("done parsing. elapsed time: %s\n", Duration.between(start, Instant.now()));
    } catch (IOException | ParserConfigurationException | SAXException e) {
      System.err.println(e);
    }
    MapGraph graph = handler.getGraph();
    graph.cleanup();
    return graph;
  }
}
