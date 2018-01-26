package aufgabe12_9;

public class True extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 0;
  }

}
