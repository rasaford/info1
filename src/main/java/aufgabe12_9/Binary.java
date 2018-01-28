package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Binary extends Expression {

  private Expression lhs;
  private Binop operator;
  private Expression rhs;

  public Binary(Expression lhs, Binop operator, Expression rhs) {
    super();
    this.lhs = lhs;
    this.operator = operator;
    this.rhs = rhs;
  }

  public Expression getLhs() {
    return lhs;
  }

  public Binop getOperator() {
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
    switch (operator) {
      case DivisionOperator:
      case Modulo:
      case MultiplicationOperator:
        return 1;
      case Plus:
      case Minus:
        return 2;
    }
    throw new RuntimeException("Unreachable");
  }
}
