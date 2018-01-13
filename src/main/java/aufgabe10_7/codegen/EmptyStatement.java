package aufgabe10_7.codegen;

public class EmptyStatement extends Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
