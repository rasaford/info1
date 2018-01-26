package aufgabe12_9;

public class False extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority() {
    return 0;
  }

}
