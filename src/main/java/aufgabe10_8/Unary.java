package aufgabe10_8;

public class Unary extends Expression {
  private Unop operator;

  public Unop getOperator() {
    return operator;
  }
  
  private Expression operand;
  
  public Expression getOperand() {
    return operand;
  }
  
  public Unary(Unop operator, Expression operand) {
    super();
    this.operator = operator;
    this.operand = operand;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
