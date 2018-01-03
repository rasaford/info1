package aufgabe9_6;

public class True extends Condition {

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
}
