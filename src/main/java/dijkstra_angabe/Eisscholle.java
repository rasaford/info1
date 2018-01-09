package dijkstra_angabe;

public class Eisscholle {

  public final static int UNBEKANNT = 0;
  public final static int VERMUTET = 1;
  public final static int BEKANNT = 2;


  private int distance;
  private Eisscholle vorgaenger;
  private final String name;
  private int state = UNBEKANNT;


  public Eisscholle(String name) {
    this.name = name;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance < 0 ? Integer.MAX_VALUE : distance;
  }

  public Eisscholle getVorgaenger() {
    return vorgaenger;
  }

  public void setVorgaenger(Eisscholle vorgaenger) {
    this.vorgaenger = vorgaenger;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    if (state < UNBEKANNT || state > BEKANNT) {
      return;
    }
    this.state = state;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Eisscholle)) {
      return false;
    }
    return name.equals(((Eisscholle) obj).getName());
  }

  @Override
  public String toString() {
//    return String.format("%s: name = %s, distance = %d, state = %d",
//        this.getClass().getName(), name, distance, state);
    return name;
  }
}
