package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class IfThenElse extends Statement {

  private Condition cond;
  private Statement thenBranch;
  private Statement elseBranch;

  public IfThenElse(Condition cond, Statement thenBranch, Statement elseBranch) {
    super();
    this.cond = cond;
    this.thenBranch = thenBranch;
    this.elseBranch = elseBranch;
  }

  public Condition getCond() {
    return cond;
  }

  public Statement getThenBranch() {
    return thenBranch;
  }

  public Statement getElseBranch() {
    return elseBranch;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
