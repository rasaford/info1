package aufgabe11_7;

public class While extends Statement {
  private Condition cond;

  public Condition getCond() {
    return cond;
  }

  private Statement body;

  public Statement getBody() {
    return body;
  }
  
  public While(Condition cond, Statement body) {
    super();
    this.cond = cond;
    this.body = body;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
