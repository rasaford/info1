package aufgabe11_7;

public class Call extends Expression {
  private String functionName;
  
  public String getFunctionName() {
    return functionName;
  }
  
  private Expression[] arguments;
  
  public Expression[] getArguments() {
    return arguments;
  }
  
  public Call(String functionName, Expression[] arguments) {
    super();
    this.functionName = functionName;
    this.arguments = arguments;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
