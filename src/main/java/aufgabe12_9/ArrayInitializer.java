package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class ArrayInitializer extends Expression {

  private Expression size;
  private Type type;

  public ArrayInitializer(Type type, Expression size) {
    this.type = type;
    this.size = size;
  }

  public Expression getSize() {
    return size;
  }

  public Type getType() {
    return type;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 3;
  }

}
