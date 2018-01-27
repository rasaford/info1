package aufgabe12_9;

public class Constructor implements Visitable {

  private String name;
  private SingleDeclaration[] parmeters;
  private Declaration[] declarations;
  private Statement[] statements;

  public Constructor(String name, SingleDeclaration[] parmeters,
      Declaration[] declarations, Statement[] statements) {
    this.name = name;
    this.parmeters = parmeters;
    this.declarations = declarations;
    this.statements = statements;
  }


  public String getName() {
    return name;
  }

  public SingleDeclaration[] getParameters() {
    return parmeters;
  }

  public Declaration[] getDeclarations() {
    return declarations;
  }


  public Statement[] getStatements() {
    return statements;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    FormatVisitor f = new FormatVisitor();
    f.visit(this);
    return f.getResult();
  }
}
