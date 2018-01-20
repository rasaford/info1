package aufgabe11_7;

public class ArrayAssignment extends Statement {

  private String name;
  private Expression index;
  private Expression value;

  public ArrayAssignment(String name, Expression index, Expression value) {
    this.name = name;
    this.index = index;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Expression getIndex() {
    return index;
  }

  public Expression getValue() {
    return value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
