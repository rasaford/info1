package aufgabe10_7;

public class EmptyStatement extends Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}