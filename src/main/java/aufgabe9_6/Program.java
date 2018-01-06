package aufgabe9_6;

public class Program {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode


  private Function[] functions;

  public Program(Function[] functions) {
    this.functions = functions;
  }

  public Function[] getFunctions() {
    return functions;
  }


  public void accept(Visitor v) {
    v.visit(this);
  }
}
