package dijkstra_angabe;

public class Seeweg {

  private int distance;
  private Eisscholle from;
  private Eisscholle to;


  public Seeweg(int distance, Eisscholle from, Eisscholle to) {
    if (distance < 0) {
      throw new RuntimeException("distance cannot be negative");
    }
    this.distance = distance;
    this.from = from;
    this.to = to;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    if (distance < 0) {
      throw new RuntimeException("distance cannot be negative");
    }
    this.distance = distance;
  }

  public void setFrom(Eisscholle from) {
    this.from = from;
  }

  public Eisscholle getTo() {
    return to;
  }

  public void setTo(Eisscholle to) {
    this.to = to;
  }

  public Eisscholle getFrom() {
    return this.from;
  }
}
