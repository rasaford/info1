package aufgabe10_8;

public class True extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
