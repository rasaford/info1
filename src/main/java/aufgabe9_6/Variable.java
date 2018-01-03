package aufgabe9_6;

public class Variable extends Expression {

  private String name;

  public Variable(String name) {
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

