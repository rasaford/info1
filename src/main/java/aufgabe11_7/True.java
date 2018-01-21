package aufgabe11_7;

public class True extends Condition {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
