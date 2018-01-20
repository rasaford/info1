package aufgabe11_7;

public enum Bunop {
  Not(3);

  private int order;

  Bunop(int precedence) {
    this.order = precedence;
  }

  public int getPriority() {
    return order;
  }
}
