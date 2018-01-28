package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Composite extends Statement {

  private Statement[] statements;

  public Composite(Statement[] statements) {
    super();
    this.statements = statements;
  }

  public Composite(Statement s) {
    this.statements = new Statement[]{s};
  }

  public Composite(Statement s1, Statement s2) {
    this.statements = new Statement[]{s1, s2};
  }

  public Statement[] getStatements() {
    return statements;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
