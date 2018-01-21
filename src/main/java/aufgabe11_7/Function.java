package aufgabe11_7;

public class Function {

  private String name;
  private String[] parameters;
  private Type[] parameterTypes;
  private Declaration[] declarations;
  private Statement[] statements;

  public Function(String name,
      String[] parameters,
      Type[] parameterTypes,
      Declaration[] declarations,
      Statement[] statements) {
    super();
    this.name = name;
    this.parameters = parameters;
    this.parameterTypes = parameterTypes;
    this.declarations = declarations;
    this.statements = statements;
  }

  public String getName() {
    return name;
  }

  public String[] getParameters() {
    return parameters;
  }

  public Type[] getParameterTypes() {
    return parameterTypes;
  }

  public Declaration[] getDeclarations() {
    return declarations;
  }

  public Statement[] getStatements() {
    return statements;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
