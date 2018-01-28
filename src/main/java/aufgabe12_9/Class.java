package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Class implements Visitable {

  private String name;
  private SingleDeclaration[] fields;
  private Constructor constructor;
  private Function[] functions;
  private String superClass;

  public Class(String name, String superClass, SingleDeclaration[] fields, Constructor constructor,
      Function[] functions) {
    this.name = name;
    this.fields = fields;
    this.superClass = superClass;
    this.constructor = constructor;
    this.functions = functions;
  }


  public String getSuperClass() {
    return superClass;
  }

  public String getName() {
    return name;
  }

  public SingleDeclaration[] getFields() {
    return fields;
  }

  public Constructor getConstructor() {
    return constructor;
  }

  public Function[] getFunctions() {
    return functions;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    FormatVisitor f = new FormatVisitor();
    f.visit(this);
    return f.getResult();
  }
}
