package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Call extends Expression {

  private String functionName;
  private Expression[] arguments;

  public Call(String functionName, Expression[] arguments) {
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

  @Override
  public int firstLevelPriority() {
    return 0;
  }

}
