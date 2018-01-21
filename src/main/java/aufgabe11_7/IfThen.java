package aufgabe11_7;

public class IfThen extends Statement {

  private Condition cond;
  private Statement thenBranch;

  public IfThen(Condition cond, Statement thenBranch) {
    super();
    this.cond = cond;
    this.thenBranch = thenBranch;
  }

  public Condition getCond() {
    return cond;
  }

  public Statement getThenBranch() {
    return thenBranch;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
