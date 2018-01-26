package aufgabe12_9;

public abstract class Statement implements Visitable {

  @Override
  public String toString() {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
