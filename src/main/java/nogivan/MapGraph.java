package nogivan;

import heap.BinomialHeap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Diese Klasse repräsentiert den Graphen der Straßen und Wege aus OpenStreetMap.
 */
public class MapGraph {

  private Map<Long, OSMNode> nodes;
  private Map<Long, Set<MapEdge>> edges;

  public MapGraph() {
    nodes = new HashMap<>();
    edges = new HashMap<>();
  }

  public void addNode(long id, OSMNode node) {
    nodes.put(id, node);
  }

  public void addEdges(OSMWay way) {
    for (int i = 0; i < way.getNodes().length - 1; i++) {
      addEdge(way.getNodes()[i], way.getNodes()[i + 1], way);
    }
    if (!way.isOneWay()) {
      for (int i = way.getNodes().length - 1; i > 0; i--) {
        addEdge(way.getNodes()[i], way.getNodes()[i - 1], way);
      }
    }
  }

  private void addEdge(Long current, Long next, OSMWay way) {
    edges.computeIfAbsent(current, k -> new HashSet<>());
    Set<MapEdge> neighbours = edges.get(current);
    neighbours.add(new MapEdge(next, way));
  }

  public Map<Long, OSMNode> getNodes() {
    return nodes;
  }

  public Set<OSMWay> getWays() {
    Set<OSMWay> way = new HashSet<>();
    edges.forEach((k, v) -> {
      v.forEach(edge -> {
        way.add(edge.getWay());
      });
    });
    return way;
  }

  /**
   * cleanup removes any nodes which are not connected to any edges
   */
  public void cleanup() {
    Iterator<Map.Entry<Long, OSMNode>> it = nodes.entrySet().iterator();
    int count = 0;
    while (it.hasNext()) {
      Map.Entry<Long, OSMNode> node = it.next();
      if (!edges.containsKey(node.getKey())) {
        it.remove();
        count++;
      }
    }
    System.out.printf("removed %d disconnected nodes from the MapGraph\n", count);
  }

  /**
   * Ermittelt, ob es eine Kante im Graphen zwischen zwei Knoten gibt.
   *
   * @param from der Startknoten
   * @param to der Zielknoten
   * @return 'true' falls es die Kante gibt, 'false' sonst
   */
  boolean hasEdge(OSMNode from, OSMNode to) {
    Set<MapEdge> neighbours = edges.get(from.getId());
    if (neighbours == null) {
      return false;
    }
    return neighbours.stream()
        .anyMatch(edge -> edge.getTo() == to.getId());
  }

  /**
   * Diese Methode findet zu einem gegebenen Kartenpunkt den nähesten OpenStreetMap-Knoten. Gibt es
   * mehrere Knoten mit dem gleichen kleinsten Abstand zu, so wird derjenige Knoten von ihnen
   * zurückgegeben, der die kleinste Id hat.
   *
   * @param p der Kartenpunkt
   * @return der OpenStreetMap-Knoten
   */
  public OSMNode closest(MapPoint p) {
    long currentDist = Long.MAX_VALUE;
    List<OSMNode> minNodes = new ArrayList<>();

    for (Map.Entry<Long, OSMNode> osm : nodes.entrySet()) {
      OSMNode osmNode = osm.getValue();
      long dist = p.distance(osmNode.getLocation());
      if (dist < currentDist) {
        currentDist = dist;
        minNodes.clear();
        minNodes.add(osmNode);
      } else if (dist == currentDist) {
        minNodes.add(osmNode);
      }
    }
    if (minNodes.size() > 1) {
      System.out.println("more than one min node");
    }
    return minNodes.stream()
        .min((o1, o2) -> (int) (o2.getId() - o1.getId()))
        .orElse(null);
  }

  /**
   * Diese Methode sucht zu zwei Kartenpunkten den kürzesten Weg durch das Straßen/Weg-Netz von
   * OpenStreetMap.
   *
   * @param from der Kartenpunkt, bei dem gestartet wird
   * @param to der Kartenpunkt, der das Ziel repräsentiert
   * @return eine mögliche Route zum Ziel und ihre Länge; die Länge des Weges bezieht sich nur auf
   * die Länge im Graphen, der Abstand von 'from' zum Startknoten bzw. 'to' zum Endknoten wird
   * vernachlässigt.
   */
  public RoutingResult route(MapPoint from, MapPoint to) {
    BinomialHeap<Node> heap = new BinomialHeap<>();
    Map<Long, Node> nodesDist = new HashMap<>();
    Set<Long> known = new HashSet<>();

    OSMNode first = closest(from);
    Node start = new Node(first, 0L);
    Object h = heap.insert(start);
    known.add(first.getId());
    nodesDist.put(first.getId(), start);
    for (Map.Entry<Long, OSMNode> nodeEntry : nodes.entrySet()) {
      if (nodeEntry.getKey().equals(first.getId())) {
        continue;
      }
      Node node = new Node(nodeEntry.getValue(), Long.MAX_VALUE);
      nodesDist.put(nodeEntry.getKey(), node);
    }

    while (heap.getSize() != 0) {
      Node min = heap.poll();
      min = nodesDist.get(min.getValue().getId());
      OSMNode minOSM = min.getValue();
      Set<MapEdge> neighbours = edges.get(minOSM.getId());
      for (MapEdge edge : neighbours) {
        Node neighbour = nodesDist.get(edge.getTo());
        if (neighbour == null) {
          continue;
        }

        OSMNode osm = neighbour.getValue();
        long newDist = min.getDistance() + minOSM.getLocation().distance(osm.getLocation());
        if (newDist < neighbour.getDistance()) {
          neighbour.setPrevious(min);
          neighbour.setDistance(newDist);
          if (!known.contains(osm.getId())) {
            known.add(osm.getId());
            heap.insert(new Node(osm, newDist));
          }
        }
      }
    }

    LinkedList<OSMNode> path = new LinkedList<>();
    OSMNode end = closest(to);
    Node current = nodesDist.get(end.getId());
    long pathLength = current.getDistance();
    while (!current.equals(start)) {
      path.addFirst(current.getValue());
      Node old = current;
      current = current.getPrevious();
      if (current == null) {
        System.err.printf("error in route reconstruction "
            + "%s does not have a parent\n", old);
        return null;
      }
    }
    path.addFirst(first);
    System.out.println("path length: " + path.size());
    return new RoutingResult(path.toArray(new OSMNode[0]), (int) pathLength);
  }


  class Node implements Comparable<Node> {

    private Node previous;
    private OSMNode value;
    private long distance = Long.MAX_VALUE;

    Node(OSMNode value, long distance) {
      this.value = value;
      this.distance = distance;
    }

    public void setDistance(long distance) {
      this.distance = distance;
    }

    public long getDistance() {
      return distance;
    }

    public OSMNode getValue() {
      return value;
    }

    public Node getPrevious() {
      return previous;
    }

    public void setPrevious(Node previous) {
      this.previous = previous;
    }

    @Override
    public int compareTo(Node o) {
      return Long.compare(distance, o.getDistance());
    }
  }

}
