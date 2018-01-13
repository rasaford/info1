package aufgabe10_7;

public class Variable extends Expression {
  private String name;
  
  public String getName() {
    return name;
  }
  
  public Variable(String name) {
    super();
    this.name = name;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
