package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Read extends Statement {

  private String name;

  public Read(String name) {
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
