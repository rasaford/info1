package aufgabe9_6;

public class Binary extends Expression {

  private Expression lhs;
  private Binop operator;
  private Expression rhs;

  public Binary(Expression lhs, Binop operator, Expression rhs) {
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
  public void accept(Visitor v) {
    v.visit(this);
  }
}
