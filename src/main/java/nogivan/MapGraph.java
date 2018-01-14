package nogivan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import nogivan.OSMNode;

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

  public void addEdge(long id, MapEdge edge) {
    Set<MapEdge> ed = edges.get(id);
    if (ed == null) {
      Set<MapEdge> newEdges = new HashSet<>();
      newEdges.add(edge);
      edges.put(id, newEdges);
    } else {
      ed.add(edge);
    }
  }

  /**
   * Ermittelt, ob es eine Kante im Graphen zwischen zwei Knoten gibt.
   *
   * @param from der Startknoten
   * @param to der Zielknoten
   * @return 'true' falls es die Kante gibt, 'false' sonst
   */
  boolean hasEdge(OSMNode from, OSMNode to) {
    /*
     * Todo
     */
    return false;
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
    /*
     * Todo
     */
    return null;
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
    /*
     * Todo
     */
    return null;
  }
}
