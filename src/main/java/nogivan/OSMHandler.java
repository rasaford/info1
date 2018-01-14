package nogivan;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OSMHandler extends DefaultHandler {

  private Node node;
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
    } else if (node != null) {
      if (qName.equals("tag")) {
        node.addTag(attributes.getValue(0));
      }
    } else if (qName.equals("way")) {
      parseWay(attributes);
    } else if (way != null) {
      if (qName.equals("tag")) {
      } else if (qName.equals("nd")) {
//        way.addRef(attributes.getValue(0));
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
    node = new Node(id, new OSMNode(id, lat, lon));
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
    if (qName.equals("node") && node != null) {
      if (node.isValid()) {
        graph.addNode(node.getId(), node.getOSMNode());
        System.out.println("added node");
      }
      node = null;
    } else if (qName.equals("way") && way != null) {
      OSMWay newWay = new OSMWay(
          way.getId(),
          way.getRefs().toArray(new Long[0]),
          way.isOneWay(),
          way.getName()
      );
      graph.addEdge(way.getId(), new MapEdge(way.getId(), newWay));
      way = null;
    }
  }


  public MapGraph getGraph() {
    return graph;
  }

  class Node {

    private boolean valid;
    private boolean hasTags;
    private long id;
    private OSMNode osmNode;

    public Node(long id, OSMNode osmNode) {
      this.id = id;
      this.osmNode = osmNode;
    }

    public OSMNode getOSMNode() {
      return osmNode;
    }

    public long getId() {
      return id;
    }

    /**
     * a valid node can either have 0 tags or has to have at least one of the following: - highway
     */
    public void addTag(String tag) {
      hasTags = true;
      valid |= tag.matches("highway");
    }

    public boolean isValid() {
      return !hasTags || valid;
    }
  }

  class Way {

    private long id;
    private boolean oneWay;
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

    public void addRef(long ref) {
      refs.add(ref);
    }

    public List<Long> getRefs() {
      return refs;
    }

    public void setOneWay() {
      oneWay = true;
    }

    public boolean isOneWay() {
      return oneWay;
    }

    public long getId() {
      return id;
    }
  }
}
