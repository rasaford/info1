package aufgabe11_7;

public class Number extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private int value;

  public Number(int value) {
    super();
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
