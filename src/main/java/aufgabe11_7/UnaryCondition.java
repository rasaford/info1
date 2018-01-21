package aufgabe11_7;

public class UnaryCondition extends Condition {

  private Bunop operator;
  private Condition operand;

  public UnaryCondition(Bunop operator, Condition operand) {
    super();
    this.operator = operator;
    this.operand = operand;
  }

  public Bunop getOperator() {
    return operator;
  }

  public Condition getOperand() {
    return operand;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return operator.getPriority();
  }
}
