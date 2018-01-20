package aufgabe11_7;

public class Function {

  private String name;

  public String getName() {
    return name;
  }

  private String[] parameters;
  private Type[] parameterTypes;

  public String[] getParameters() {
    return parameters;
  }

  public Type[] getParameterTypes() {
    return parameterTypes;
  }

  private Declaration[] declarations;

  public Declaration[] getDeclarations() {
    return declarations;
  }

  private Statement[] statements;

  public Statement[] getStatements() {
    return statements;
  }

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

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
