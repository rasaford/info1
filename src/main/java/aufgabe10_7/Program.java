package aufgabe10_7;


public class Program {
  private Function[] functions;
  
  public Function[] getFunctions() {
    return functions;
  }
  
  
  public Program(Function[] functions) {
    super();
    this.functions = functions;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
