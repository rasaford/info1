package aufgabe11_7;

public enum Unop {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  // had to be changed 3 -> 4
  Minus(4);

  private int order;

  Unop(int precedence) {
    this.order = precedence;
  }

  public int getPriority() {
    return order;
  }
}

