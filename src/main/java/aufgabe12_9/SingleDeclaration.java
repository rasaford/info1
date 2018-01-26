package aufgabe12_9;

public class SingleDeclaration implements Visitable {

  private Type type;
  private String name;

  public SingleDeclaration(Type type, String a) {
    this.type = type;
    this.name = a;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
