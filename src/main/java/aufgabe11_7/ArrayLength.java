package aufgabe11_7;

public class ArrayLength extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Expression array;

  public ArrayLength(Expression array) {
    this.array = array;
  }

  public Expression getArray() {
    return array;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
