package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class ArrayLength extends Expression {

  private Expression array;

  public ArrayLength(Expression array) {
    super();
    this.array = array;
  }

  public Expression getArray() {
    return array;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 0;
  }

}
