package aufgabe9_6;

public class Read extends Statement{

  private String name;

  public Read(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
