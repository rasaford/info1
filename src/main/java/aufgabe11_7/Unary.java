package aufgabe11_7;

public class Unary extends Expression {

  private Unop operator;
  private Expression operand;

  public Unary(Unop operator, Expression operand) {
    super();
    this.operator = operator;
    this.operand = operand;
  }

  public Unop getOperator() {
    return operator;
  }

  public Expression getOperand() {
    return operand;
  }

  @Override
  public int firstLevelPriority() {
    return operator.getPriority();
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
