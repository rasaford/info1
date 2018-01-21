
package nogivan;
/**
 * Diese Klasse repräsentiert eine Kante im Graphen.
 *
 */
class MapEdge implements Comparable<MapEdge> {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  private long to;

  public long getTo() {
    return to;
  }

  private OSMWay way;

  public OSMWay getWay() {
    return way;
  }

  public MapEdge(long to, OSMWay way) {
    this.to = to;
    this.way = way;
  }

  @Override
  public int compareTo(MapEdge o) {
    return ((Long) to).compareTo(o.to);
  }

  @Override
  public String toString() {
    return way + " -> " + to;
  }
}