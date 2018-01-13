package aufgabe10_8;

public class False extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
