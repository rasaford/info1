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
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

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
    Map<Long, Long> previous = new HashMap<>();
    Map<Long, Object> handles = new HashMap<>();

    Map<Long, Long> knownDist = new HashMap<>();
    Set<Long> known = new HashSet<>();
    Set<Long> unknown = new HashSet<>();

    OSMNode first = closest(from);
    OSMNode last = closest(to);
    Node start = new Node(first, first.distance(last));
    Object h = heap.insert(start);
    handles.put(first.getId(), h);
    unknown.add(first.getId());
    knownDist.put(first.getId(), 0L);

    while (!unknown.isEmpty()) {
      OSMNode min = heap.poll().getValue();
      if (min.equals(last)) {
        break;
      }
      unknown.remove(min.getId());
      known.add(min.getId());
      for (MapEdge edge : edges.get(min.getId())) {
        OSMNode neighbour = nodes.get(edge.getTo());
        if (neighbour == null || known.contains(neighbour.getId())) {
          continue;
        }
        Long id = neighbour.getId();
        if (!unknown.contains(id)) {
          // heap insert
          unknown.add(id);
          long dist = knownDist.getOrDefault(id, Long.MAX_VALUE) + neighbour.distance(last);
          dist = dist < 0 ? Long.MAX_VALUE : dist;
          Object handle = heap.insert(new Node(neighbour, dist));
          handles.put(id, handle);
        }
        long newDist = knownDist.get(min.getId()) + min.distance(neighbour);
        if (newDist < knownDist.getOrDefault(id, Long.MAX_VALUE)) {
          previous.put(id, min.getId());
          Node n = new Node(neighbour, newDist + neighbour.distance(last));
          // heap update
          Object handle = handles.get(id);
          heap.replaceWithSmallerElement(handle, n);
          knownDist.put(id, newDist);
        }
      }
    }

    LinkedList<OSMNode> path = new LinkedList<>();
    OSMNode current = last;
    long pathLength = knownDist.get(last.getId());
    while (!current.equals(first)) {
      path.addFirst(current);
      OSMNode old = current;
      current = nodes.get(previous.get(current.getId()));
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

    private OSMNode value;
    private long distance = Long.MAX_VALUE;

    Node(OSMNode value, long distance) {
      this.value = value;
      this.distance = distance;
    }

    public long getDistance() {
      return distance;
    }

    public OSMNode getValue() {
      return value;
    }

    @Override
    public int compareTo(Node o) {
      return Long.compare(distance, o.getDistance());
    }
  }

}
