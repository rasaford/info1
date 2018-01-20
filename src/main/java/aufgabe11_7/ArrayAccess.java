package aufgabe11_7;

public class ArrayAccess extends Expression {

  private Expression index;
  private Expression array;

  public ArrayAccess(Expression array, Expression index) {
    this.index = index;
    this.array = array;
  }

  public Expression getIndex() {
    return index;
  }

  public Expression getArray() {
    return array;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
