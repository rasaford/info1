package aufgabe11_7;

public abstract class Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  public abstract void accept(Visitor visitor);

  public int firstLevelPriority() {
    return -1;
  }
}
