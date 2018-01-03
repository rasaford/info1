package aufgabe9_6;

public class IfThenElse extends Statement {

  private Condition cond;
  private Statement thenBranch;
  private Statement elseBranch;

  public IfThenElse(Condition cond, Statement thenBranch, Statement elseBranch) {
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
  public void accept(Visitor v) {
    v.visit(this);
  }
}
