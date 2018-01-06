package aufgabe9_6;

public class Function {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private String name;
  private String[] parameters;
  private Declaration[] declarations;
  private Statement[] statements;

  public Function(String name, String[] parameters, Declaration[] declarations,
      Statement[] statements) {
    this.name = name;
    this.parameters = parameters;
    this.declarations = declarations;
    this.statements = statements;
  }

  public String getName() {
    return name;
  }

  public String[] getParameters() {
    return parameters;
  }

  public Declaration[] getDeclarations() {
    return declarations;
  }

  public Statement[] getStatements() {
    return statements;
  }

  public void accept(Visitor v ) {
    v.visit(this);
  }
}
