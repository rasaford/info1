package aufgabe11_7;

public class Declaration {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private String[] names;
  private Type type;

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

  public String[] getNames() {
    return names;
  }

  public Type getType() {
    return type;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
