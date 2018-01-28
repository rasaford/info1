package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class EmptyStatement extends Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
