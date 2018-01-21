package aufgabe11_7;

public class While extends Statement {

  private Condition cond;
  private Statement body;

  public While(Condition cond, Statement body) {
    super();
    this.cond = cond;
    this.body = body;
  }

  public Condition getCond() {
    return cond;
  }

  public Statement getBody() {
    return body;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
