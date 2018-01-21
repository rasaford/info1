package aufgabe11_7;

public class ArrayInitializer extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Expression size;

  public ArrayInitializer(Expression size) {
    this.size = size;
  }

  public Expression getSize() {
    return size;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
