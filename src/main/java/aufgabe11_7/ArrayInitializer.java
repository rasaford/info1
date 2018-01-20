package aufgabe11_7;

public class ArrayInitializer extends Expression {

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
