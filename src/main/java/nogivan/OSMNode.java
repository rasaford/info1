package nogivan;

/**
 * Diese Klasse implementiert einen OSM-Knoten.
 */
public class OSMNode implements Comparable<OSMNode> {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  /**
   * Die ID des Knotens.
   */
  private long id;

  public long getId() {
    return id;
  }

  /**
   * Die Koordinaten des Punktes.
   */
  private MapPoint location;

  public MapPoint getLocation() {
    return location;
  }


  public long distance(OSMNode other) {
    return location.distance(other.getLocation());
  }


  public OSMNode(long id, double lat, double lon) {
    this.id = id;
    this.location = new MapPoint(lat, lon);
  }

  @Override
  public String toString() {
    return "Node {id = " + id + ", " + location + "}";
  }

  @Override
  public int compareTo(OSMNode o) {
    return ((Long) id).compareTo(o.getId());
  }
}
