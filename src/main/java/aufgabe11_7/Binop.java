package aufgabe11_7;

public enum Binop {
  MultiplicationOperator(4),
  DivisionOperator(4),
  Modulo(4),
  Minus(5),
  Plus(5);

  private int order;

  Binop(int precedence) {
    this.order = precedence;
  }

  public int getPriority() {
    return order;
  }
}

