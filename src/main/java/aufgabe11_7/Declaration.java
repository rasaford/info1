package aufgabe11_7;

public class Declaration {

  private String[] names;
  private Type type;

  public String[] getNames() {
    return names;
  }

  public Type getType() {
    return type;
  }

  public Declaration(Type type, String[] names) {
    this.names = names;
    this.type = type;
  }

  public Declaration(Type type, String a) {
    this.names = new String[]{a};
    this.type = type;
  }

  public Declaration(Type type, String a, String b) {
    this.names = new String[]{a, b};
    this.type = type;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
