package aufgabe12_9;

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
