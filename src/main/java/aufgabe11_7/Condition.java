package aufgabe11_7;

public abstract class Condition {

  public abstract void accept(Visitor visitor);

  public int firstLevelPriority() {
    return -1;
  }
}
