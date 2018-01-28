package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class ArrayAccess extends Expression {

  private Expression array;
  private Expression index;

  public ArrayAccess(Expression array, Expression index) {
    super();
    this.array = array;
    this.index = index;
  }

  public Expression getArray() {
    return array;
  }

  public Expression getIndex() {
    return index;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 1;
  }

}
