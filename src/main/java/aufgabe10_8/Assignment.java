package aufgabe10_8;

public class Assignment extends Statement {
  private String name;
  
  public String getName() {
    return name;
  }
  
  private Expression expression;
  
  public Expression getExpression() {
    return expression;
  }
  
  public Assignment(String name, Expression expression) {
    super();
    this.name = name;
    this.expression = expression;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
