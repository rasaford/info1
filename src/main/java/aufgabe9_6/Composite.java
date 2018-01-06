package aufgabe9_6;

public class Composite extends Statement {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private Statement[] statements;

  public Composite(Statement[] statements) {
    this.statements = statements;
  }

  public Statement[] getStatements() {
    return statements;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
