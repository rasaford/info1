package aufgabe9_6;

public class Return extends Statement {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Expression expression;


  public Return(Expression expression) {
    this.expression = expression;
  }


  public Expression getExpression() {
    return expression;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
