package aufgabe9_6;

public class False extends Condition {

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
