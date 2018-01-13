package aufgabe10_8;

public class Return extends Statement {
  private Expression expr;
  
  public Expression getExpression() {
    return expr;
  }
  
  public Return(Expression expr) {
    super();
    this.expr = expr;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
