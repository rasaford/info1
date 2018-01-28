package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Declaration implements Visitable {

  private Type type;
  private String[] names;

  public Declaration(Type type, String[] names) {
    this.type = type;
    this.names = names;
  }

  public Declaration(Type type, String a) {
    this.type = type;
    this.names = new String[]{a};
  }

  public Declaration(Type type, String a, String b) {
    this.type = type;
    this.names = new String[]{a, b};
  }

  public Type getType() {
    return type;
  }

  public String[] getNames() {
    return names;
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
