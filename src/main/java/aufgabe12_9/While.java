package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
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
