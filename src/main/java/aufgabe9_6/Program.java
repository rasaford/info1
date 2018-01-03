package aufgabe9_6;

public class Program {


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
