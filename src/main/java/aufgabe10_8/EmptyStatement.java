package aufgabe10_8;

public class EmptyStatement extends Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
