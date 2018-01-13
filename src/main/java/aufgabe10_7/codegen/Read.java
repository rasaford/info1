package aufgabe10_7.codegen;

public class Read extends Statement {
  private String name;
  
  public String getName() {
    return name;
  }
  
  public Read(String name) {
    super();
    this.name = name;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
