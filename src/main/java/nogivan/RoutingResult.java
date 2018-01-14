package nogivan;

/**
 * Die Klasse implementiert das Ergebnis einer
 * Suche nach einem kürzeste Weg.
 */
public class RoutingResult {
  /**
   * Der gefundene Weg
   */
  private OSMNode[] path;
  
  public OSMNode[] getPath () {
    return path;
  }
  
  /**
   * Die Länge des gefundenen Weges
   */
  private int distance;
  
  public int getDistance () {
    return distance;
  }
  
  public RoutingResult (OSMNode[] path, int distance) {
    this.path = path;
    this.distance = distance;
  }
  
  @Override public boolean equals (Object obj) {
    RoutingResult objCasted = (RoutingResult)obj;
    if(distance != objCasted.distance)
      return false;
    if(path.length != objCasted.path.length)
      return false;
    for (int i = 0; i < path.length; i++)
      if(!path[i].equals(objCasted.path[i]))
        return false;
    return true;
  }
}
