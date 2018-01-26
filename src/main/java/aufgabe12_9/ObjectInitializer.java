package aufgabe12_9;

public class ObjectInitializer extends Expression {

  private String objectName;
  private Expression[] parameters;

  public ObjectInitializer(String objectName, Expression[] parameters) {
    this.objectName = objectName;
    this.parameters = parameters;
  }

  public String getClassName() {
    return objectName;
  }

  public Expression[] getArguments() {
    return parameters;
  }

  @Override
  public int firstLevelPriority() {
    return 0;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
