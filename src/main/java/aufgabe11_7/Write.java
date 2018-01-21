package aufgabe11_7;

public class Write extends Statement {

  private Expression expression;

  public Write(Expression expression) {
    super();
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
