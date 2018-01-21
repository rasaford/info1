package aufgabe11_7;

public class Return extends Statement {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Expression expr;

  public Return(Expression expr) {
    super();
    this.expr = expr;
  }

  public Expression getExpression() {
    return expr;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
