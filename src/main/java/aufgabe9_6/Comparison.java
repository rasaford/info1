package aufgabe9_6;

public class Comparison extends Condition {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Expression lhs;
  private Comp operator;
  private Expression rhs;

  public Comparison(Expression lhs, Comp operator, Expression rhs) {
    this.lhs = lhs;
    this.operator = operator;
    this.rhs = rhs;
  }

  public Expression getLhs() {
    return lhs;
  }

  public Comp getOperator() {
    return operator;
  }

  public Expression getRhs() {
    return rhs;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
