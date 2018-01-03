package aufgabe9_6;

public class While extends Statement {

  private Condition cond;
  private Statement body;

  public While(Condition cond, Statement body) {
    this.cond = cond;
    this.body = body;
  }

  public Condition getCond() {
    return cond;
  }

  public Statement getBody() {
    return body;
  }
}
