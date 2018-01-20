package aufgabe11_7;

public class ArrayLength extends Expression {

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
