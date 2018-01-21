package nogivan;

/**
 * Diese Klasse implementiert einen Kartenpunkt. Ein Kartenpunkt hat einen Position in Form eines
 * Länge- und Breitengrades.
 */
public class MapPoint {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  /**
   * Der Breitengrad
   */
  private double lat;

  public double getLat() {
    return lat;
  }

  /**
   * Der Längengrad
   */
  private double lon;

  public double getLon() {
    return lon;
  }

  public MapPoint(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }

  /**
   * Diese Methode berechnet den Abstand dieses Kartenpunktes zu einem anderen Kartenpunkt.
   *
   * @param other der andere Kartenpunkt
   * @return der Abstand in Metern
   */
  public int distance(MapPoint other) {
    double earthRadius = 6371e3;
    double deltaLat = Math.toRadians(other.getLat() - lat);
    double deltaLon = Math.toRadians(other.getLon() - lon);
    double a = Math.sin(deltaLat / 2) *
        Math.sin(deltaLat / 2) +
        Math.cos(Math.toRadians(lat)) *
            Math.cos(Math.toRadians(other.getLat())) *
            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return (int) (earthRadius * c);
  }

  @Override
  public String toString() {
    return "lat = " + lat + ", lon = " + lon;
  }

}
