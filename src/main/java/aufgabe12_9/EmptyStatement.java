package aufgabe12_9;

public class EmptyStatement extends Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
