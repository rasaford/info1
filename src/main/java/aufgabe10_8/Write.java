package aufgabe10_8;

public class Write extends Statement {
  private Expression expression;
  
  public Expression getExpression() {
    return expression;
  }
  
  public Write(Expression expression) {
    super();
    this.expression = expression;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
