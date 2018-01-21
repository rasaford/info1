package aufgabe11_7;

public enum Bbinop {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  And(12),
  Or(13);

  private int order;

  Bbinop(int precedence) {
    this.order = precedence;
  }

  public int getPriority() {
    return order;
  }

}
