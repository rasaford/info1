package nogivan;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Nogivan {

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {

    System.out.println("Reading OSM data...");
    MapGraph g;
    if (args.length > 0) {
      g = MapParser.parseFile(args[0]);
    } else {
      g = MapParser.parseFile("campus_garching.osm");
//      g = MapParser.parseFile("oberbayern-latest.osm");
    }
    System.out.println("Finished reading OSM data...");

    Instant start = Instant.now();
    RoutingResult rr = g
        .route(new MapPoint(48.2690197, 11.6751468), new MapPoint(48.2638814, 11.6661943));
//    RoutingResult rr = g
//        .route(new MapPoint(48.2690197, 11.6751468), new MapPoint(48.003833, 11.317972)); //<55km
//    RoutingResult rr = g
//        .route(new MapPoint(48.2690197, 11.6751468), new MapPoint(48.098, 11.508833));
//    RoutingResult rr = g
//        .route(new MapPoint(47.862916, 11.0275), new MapPoint(48.349388, 11.768416)); //looong
    System.out.println("route calculation time " + Duration.between(start, Instant.now()));

    if (rr == null) {
      System.out.println("No route :-(");
    } else {
      System.out.println("Distance: " + rr.getDistance());
      System.out.println("Writing GPX track to route.gpx...");
      GPXWriter gw = new GPXWriter("route.gpx");
      gw.writeGPX(rr);
      gw.close();
    }

    System.out.println("Finished SSSP");
  }
}
