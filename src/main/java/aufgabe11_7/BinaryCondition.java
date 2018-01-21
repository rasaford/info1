package aufgabe11_7;

public class BinaryCondition extends Condition {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Condition lhs;
  private Bbinop operator;
  private Condition rhs;

  public BinaryCondition(Condition lhs, Bbinop operator, Condition rhs) {
    super();
    this.lhs = lhs;
    this.operator = operator;
    this.rhs = rhs;
  }

  public Condition getLhs() {
    return lhs;
  }

  public Bbinop getOperator() {
    return operator;
  }

  @Override
  public int firstLevelPriority() {
    return operator.getPriority();
  }

  public Condition getRhs() {
    return rhs;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
