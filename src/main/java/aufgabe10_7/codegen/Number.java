package aufgabe10_7.codegen;

public class Number extends Expression {
  private int value;
  
  public int getValue() {
    return value;
  }
  
  public Number(int value) {
    super();
    this.value = value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
