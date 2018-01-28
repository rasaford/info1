package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
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
