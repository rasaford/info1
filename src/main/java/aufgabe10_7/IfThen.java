package aufgabe10_7;

public class IfThen extends Statement {
  private Condition cond;
  
  public Condition getCond() {
    return cond;
  }
  
  private Statement thenBranch;
  
  public Statement getThenBranch() {
    return thenBranch;
  }
  
  public IfThen(Condition cond, Statement thenBranch) {
    super();
    this.cond = cond;
    this.thenBranch = thenBranch;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
