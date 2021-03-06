package aufgabe11_7;

public class Variable extends Expression {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private String name;

  public Variable(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
