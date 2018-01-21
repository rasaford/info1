package aufgabe11_7;

public enum Comp {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  LessEqual(7),
  Less(7),
  GreaterEqual(7),
  Greater(7),
  Equals(8),
  NotEquals(8);

  private int order;

  Comp(int precedence) {
    this.order = precedence;
  }

  public int getPriority() {
    return order;
  }
}
