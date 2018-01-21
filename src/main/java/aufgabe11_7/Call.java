package aufgabe11_7;

public class Call extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private String functionName;
  private Expression[] arguments;

  public Call(String functionName, Expression[] arguments) {
    super();
    this.functionName = functionName;
    this.arguments = arguments;
  }

  public String getFunctionName() {
    return functionName;
  }

  public Expression[] getArguments() {
    return arguments;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
