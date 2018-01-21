package aufgabe11_7;

public class Assignment extends Statement {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private String name;
  private Expression expression;

  public Assignment(String name, Expression expression) {
    super();
    this.name = name;
    this.expression = expression;
  }

  public String getName() {
    return name;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
