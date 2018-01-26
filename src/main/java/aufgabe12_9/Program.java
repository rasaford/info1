package aufgabe12_9;


public class Program implements Visitable {

  private Function[] functions;
  private Class[] classes;

  public Program(Function[] functions, Class[] classes) {
    this.functions = functions;
    this.classes = classes;
  }

  public Function[] getFunctions() {
    return functions;
  }

  public Class[] getClasses() {
    return classes;
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
