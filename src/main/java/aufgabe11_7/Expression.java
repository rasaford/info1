package aufgabe11_7;

public abstract class Expression {

  public abstract void accept(Visitor visitor);

  public int firstLevelPriority() {
    return -1;
  }
}
