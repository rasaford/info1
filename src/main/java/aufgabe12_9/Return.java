package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Return extends Statement {

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
