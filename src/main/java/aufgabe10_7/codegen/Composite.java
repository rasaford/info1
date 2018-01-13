package aufgabe10_7.codegen;

public class Composite extends Statement {
  private Statement[] statements;
  
  public Statement[] getStatements() {
    return statements;
  }
  
  public Composite(Statement[] statements) {
    super();
    this.statements = statements;
  }

  public Composite(Statement s) {
    this.statements = new Statement[] { s };
  }
  
  public Composite(Statement s1, Statement s2) {
    this.statements = new Statement[] { s1, s2 };
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
