package aufgabe12_9;

public class ExpressionStatement extends Statement {

private Expression expression;

  public ExpressionStatement(Expression expression) {
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
