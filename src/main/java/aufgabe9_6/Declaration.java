package aufgabe9_6;

public class Declaration {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private String[] names;

  public String[] getNames() {
    return names;
  }

  public Declaration(String[] names) {
    this.names = names;
  }


  public void accept(Visitor v) {
    v.visit(this);
  }
}
