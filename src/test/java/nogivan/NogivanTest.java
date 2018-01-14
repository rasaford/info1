package nogivan;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class NogivanTest {
  private int lengthAndCheck(MapGraph g, OSMNode[] path) {
    int length = 0;
    for (int i = 1; i < path.length; i++) {
      assertTrue(g.hasEdge(path[i - 1], path[i]));
      length += path[i - 1].getLocation().distance(path[i].getLocation());
    }
    return length;
  }

  @Test
  public void testabcd1() throws SAXException, IOException, ParserConfigurationException {
    System.out.println("Reading OSM data...");
    MapGraph g = MapParser.parseFile("campus_garching.osm");
    System.out.println("Finished reading OSM data...");

    RoutingResult rr = g.route(new MapPoint(48.2626633, 11.6689035), new MapPoint(48.2622312, 11.6662273));
    
    assertNotNull(rr);
    
    OSMNode[] path = rr.getPath();
    assertTrue(path.length > 0);
    assertTrue(path[0].getId() == 277698459L);
    assertTrue(path[path.length - 1].getId() == 277698572L);
    int pathLength = lengthAndCheck(g, rr.getPath());
    assertTrue(pathLength == rr.getDistance());
    
    assertTrue(rr.getDistance() == 254);
    
//    GPXWriter.write("/home/jucs/Desktop/testabcd1.gpx", rr);
  }
  
  @Test
  public void testabcd2() throws SAXException, IOException, ParserConfigurationException {
    System.out.println("Reading OSM data...");
    MapGraph g = MapParser.parseFile("campus_garching.osm");
    System.out.println("Finished reading OSM data...");

    RoutingResult rr = g.route(new MapPoint(48.26313, 11.67459), new MapPoint(48.26632, 11.66750));
    
    assertNotNull(rr);
    
    OSMNode[] path = rr.getPath();
    assertTrue(path.length > 0);
    assertTrue(path[0].getId() == 2496758189L);
    assertTrue(path[path.length - 1].getId() == 277698564L);
    int pathLength = lengthAndCheck(g, rr.getPath());
    assertTrue(pathLength == rr.getDistance());
    
    assertTrue(rr.getDistance() == 804);
  }
}
