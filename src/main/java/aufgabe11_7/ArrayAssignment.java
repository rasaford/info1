package aufgabe11_7;

public class ArrayAssignment extends Statement {

  private String name;
  private Expression array;
  private Expression value;

  public ArrayAssignment(String name, Expression array, Expression value) {
    this.name = name;
    this.array = array;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Expression getArray() {
    return array;
  }

  public Expression getValue() {
    return value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
