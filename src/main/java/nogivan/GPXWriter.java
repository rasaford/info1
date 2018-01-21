package nogivan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nogivan.MapGraph.Node;

public class GPXWriter {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private PrintWriter writer;

  public GPXWriter(String fileName) throws FileNotFoundException {
    writer = new PrintWriter(new File(fileName));
  }

  private void writeLine(String line) {
    writer.println(line);
  }

  public void close() {
    writer.close();
  }

  public void writeGPX(RoutingResult rr) {
    writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n"
        + "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"Wikipedia\"\n"
        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "    xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
    writeLine("<rte>");
    for (OSMNode node : rr.getPath()) {
      writeLine(String.format("<rtept lat=\"%f\" lon=\"%f\"></rtept>",
          node.getLocation().getLat(), node.getLocation().getLon()));
    }
    writeLine("</rte>");
  }

  public void writeGPX2(Map<Long, OSMNode> nodes) {
    writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n"
        + "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"Wikipedia\"\n"
        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "    xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
    writeLine("<wpt>");
    nodes.forEach((k, v) -> {
          writeLine(String.format("<wpt lat=\"%f\" lon=\"%f\">"
                  + "<name>%s</name>"
                  + "</wpt>",
              v.getLocation().getLat(), v.getLocation().getLon(), v.getId()));
        }
    );
    writeLine("</wpt>");
  }

  public void writeGPX(Map<Long, OSMNode> nodes, Set<OSMWay> way) {
    writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n"
        + "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"Wikipedia\"\n"
        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "    xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");

    way.forEach(w -> {
      writeLine("<rte>");
      writeLine(String.format("<name>%s : %d</name>", w.getId(), w.getNodes().length));
      for (Long node : w.getNodes()) {
        OSMNode n = nodes.get(node);
        if (n == null) {
          continue;
        }
        writeLine(String.format("<rtept lat=\"%f\" lon=\"%f\"></rtept>",
            n.getLocation().getLat(), n.getLocation().getLon()));
      }
      writeLine("</rte>");
    });
  }
}
