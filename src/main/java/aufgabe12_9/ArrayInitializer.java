package aufgabe12_9;

public class ArrayInitializer extends Expression {

  private Expression length;

  public ArrayInitializer(Expression length) {
    super();
    this.length = length;
  }

  public Expression getLength() {
    return length;
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
