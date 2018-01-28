package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class ArrayAssignment extends Statement {

  private String name;
  private Expression index;
  private Expression rhs;

  public ArrayAssignment(String name, Expression index, Expression rhs) {
    super();
    this.name = name;
    this.index = index;
    this.rhs = rhs;
  }

  public String getName() {
    return name;
  }

  public Expression getIndex() {
    return index;
  }

  public Expression getRhs() {
    return rhs;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
