package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
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
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 0;
  }
}
