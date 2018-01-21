package aufgabe11_7;

public enum Bunop {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  Not(3);

  private int order;

  Bunop(int precedence) {
    this.order = precedence;
  }

  public int getPriority() {
    return order;
  }
}
