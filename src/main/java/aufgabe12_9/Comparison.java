package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Comparison extends Condition {

  private Expression lhs;
  private Comp operator;
  private Expression rhs;

  public Comparison(Expression lhs, Comp operator, Expression rhs) {
    super();
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
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 2;
  }
}
