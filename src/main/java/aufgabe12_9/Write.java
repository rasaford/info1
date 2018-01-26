package aufgabe12_9;

public class Write extends Statement {

  private Expression expression;

  public Write(Expression expression) {
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
