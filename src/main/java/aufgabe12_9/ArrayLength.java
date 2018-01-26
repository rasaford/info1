package aufgabe12_9;

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
