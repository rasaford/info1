package aufgabe9_6;

public class Number extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private int value;

  public Number(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
