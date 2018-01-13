package aufgabe10_7;

public class UnaryCondition extends Condition {
  private Bunop operator;
  
  public Bunop getOperator() {
    return operator;
  }
  
  private Condition operand;
  
  public Condition getOperand() {
    return operand;
  }
  
  public UnaryCondition(Bunop operator, Condition operand) {
    super();
    this.operator = operator;
    this.operand = operand;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
