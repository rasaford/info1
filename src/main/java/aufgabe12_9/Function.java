package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Function implements Visitable {

  private Type returnType;
  private String name;
  private SingleDeclaration[] parameters;
  private Declaration[] declarations;
  private Statement[] statements;

  public Function(Type returnType, String name, SingleDeclaration[] parameters,
      Declaration[] declarations, Statement[] statements) {
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.declarations = declarations;
    this.statements = statements;
  }

  public Type getReturnType() {
    return returnType;
  }

  public String getName() {
    return name;
  }

  public SingleDeclaration[] getParameters() {
    return parameters;
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

  @Override
  public String toString() {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
