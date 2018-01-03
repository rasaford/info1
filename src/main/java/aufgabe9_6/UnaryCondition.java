package aufgabe9_6;

public class UnaryCondition extends Condition {

  private Bunop operator;
  private Condition operand;

  public UnaryCondition(Bunop operator, Condition operand) {
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
  public void accept(Visitor v) {
    v.visit(this);
  }
}
