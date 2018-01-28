package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class MethodCall extends Expression {

  private String refName;
  private String methodName;
  private Expression[] arguments;

  public MethodCall(String refName, String methodName, Expression[] arguments) {
    this.methodName = methodName;
    this.refName = refName;
    this.arguments = arguments;
  }

  public String getRefName() {
    return refName;
  }

  public Expression[] getArguments() {
    return arguments;
  }

  public String getMethodName() {
    return methodName;
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
