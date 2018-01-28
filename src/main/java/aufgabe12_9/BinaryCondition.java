package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class BinaryCondition extends Condition {

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

  public Condition getRhs() {
    return rhs;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    switch (operator) {
      case And:
        return 3;
      case Or:
        return 4;
    }
    throw new RuntimeException("Unreachable");
  }

}
