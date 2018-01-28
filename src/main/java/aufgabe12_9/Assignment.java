package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Assignment extends Statement {

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
