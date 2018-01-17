package nogivan;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OSMHandler extends DefaultHandler {

  private OSMNode node;
  private Way way;
  private MapGraph graph;

  public OSMHandler() {
    graph = new MapGraph();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (qName.equals("node")) {
      parseNode(attributes);
    } else if (qName.equals("way")) {
      parseWay(attributes);
    } else if (way != null) {
      if (qName.equals("tag")) {
        way.addTag(attributes.getValue("k"), attributes.getValue("v"));
      } else if (qName.equals("nd")) {
        way.addNd("ref", attributes.getValue("ref"));
      }
    }
  }

  private void parseNode(Attributes attributes) {
    long id = 0;
    double lat = 0;
    double lon = 0;
    try {
      id = Long.parseLong(attributes.getValue("id"));
      lat = Double.parseDouble(attributes.getValue("lat"));
      lon = Double.parseDouble(attributes.getValue("lon"));
    } catch (NumberFormatException e) {
      System.err.printf("could not parse node id:%d\n", id);
      return;
    }
    node = new OSMNode(id, lat, lon);
  }


  private void parseWay(Attributes attributes) {
    long id = 0;
    try {
      id = Long.parseLong(attributes.getValue("id"));
    } catch (NumberFormatException e) {
      System.err.printf("could not parse way id:%d\n", id);
      return;
    }
    way = new Way(id);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equals("node")) {
      graph.addNode(node.getId(), node);
      node = null;
    } else if (qName.equals("way") && way != null) {
      if (way.isValid()) {
        graph.addEdges(new OSMWay(
            way.getId(),
            way.getRefs().toArray(new Long[0]),
            way.isOneWay(),
            way.getName()
        ));
      }
      way = null;
    }
  }


  public MapGraph getGraph() {
    return graph;
  }

  private class Way {

    private long id;
    private boolean oneWay;
    private boolean valid;
    private String name;
    private List<Long> refs;

    public Way(long id) {
      this.id = id;
      this.refs = new ArrayList<>();
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public List<Long> getRefs() {
      return refs;
    }

    public void addNd(String key, String value) {
      if (value == null) {
        return;
      }
      switch (key) {
        case "ref":
          try {
            refs.add(Long.parseLong(value));
          } catch (NumberFormatException e) {
            System.err.printf("unable to parse Long %s\n", value);
          }
          break;
      }
    }

    public void addTag(String key, String value) {
      if (value == null) {
        return;
      }
      switch (key) {
        case "highway":
          valid = !(value.equals("construction") || value.equals("proposed"));
          break;
        case "oneway":
          oneWay = value.equalsIgnoreCase("yes");
          break;
      }
    }

    public boolean isOneWay() {
      return oneWay;
    }

    public long getId() {
      return id;
    }

    public boolean isValid() {
      return valid;
    }
  }
}
