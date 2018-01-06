package aufgabe9_6;

public class Unary extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Unop operator;
  private Expression operand;

  public Unary(Unop operator, Expression operand) {
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
  public void accept(Visitor v) {
   v.visit(this);
  }
}
