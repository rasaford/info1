package aufgabe10_8;

public class IfThenElse extends Statement {
  private Condition cond;

  public Condition getCond() {
    return cond;
  }

  private Statement thenBranch;

  public Statement getThenBranch() {
    return thenBranch;
  }

  private Statement elseBranch;

  public Statement getElseBranch() {
    return elseBranch;
  }

  public IfThenElse(Condition cond, Statement thenBranch, Statement elseBranch) {
    super();
    this.cond = cond;
    this.thenBranch = thenBranch;
    this.elseBranch = elseBranch;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
