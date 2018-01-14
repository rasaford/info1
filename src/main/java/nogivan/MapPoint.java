package nogivan;

/**
 * Diese Klasse implementiert einen Kartenpunkt. Ein
 * Kartenpunkt hat einen Position in Form eines Länge-
 * und Breitengrades.
 */
public class MapPoint {
  /**
   * Der Breitengrad
   */
  private double lat;
  
  public double getLat () {
    return lat;
  }
  
  /**
   * Der Längengrad
   */
  private double lon;
  
  public double getLon () {
    return lon;
  }
  
  public MapPoint (double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }
  
  /**
   * Diese Methode berechnet den Abstand dieses Kartenpunktes
   * zu einem anderen Kartenpunkt.
   * 
   * @param other der andere Kartenpunkt
   * @return der Abstand in Metern
   */
  public int distance(MapPoint other) {

    return -1;
  }
  
  @Override public String toString () {
    return  "lat = " + lat + ", lon = " + lon;
  }

}
