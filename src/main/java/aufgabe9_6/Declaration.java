package aufgabe9_6;

public class Declaration {

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
