package aufgabe11_7;

public class EmptyStatement extends Statement {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
