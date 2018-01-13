package aufgabe10_8;

public abstract class Visitor {
  public abstract void visit(Number number);

  public abstract void visit(Variable nameExpression);

  public abstract void visit(Unary unary);

  public abstract void visit(Binary binary);

  public abstract void visit(Function function);

  public abstract void visit(Declaration declaration);

  public abstract void visit(Read read);

  public abstract void visit(Assignment assignment);

  public abstract void visit(Composite composite);

  public abstract void visit(Comparison comparison);

  public abstract void visit(While while_);

  public abstract void visit(True true_);

  public abstract void visit(False false_);

  public abstract void visit(UnaryCondition unaryCondition);

  public abstract void visit(BinaryCondition binaryCondition);

  public abstract void visit(IfThenElse ifThenElse);

  public abstract void visit(Program program);

  public abstract void visit(IfThen ifThen);

  public abstract void visit(Call call);

  public abstract void visit(Return return_);

  public abstract void visit(Write write);

  public abstract void visit(EmptyStatement emptyStatement);
}
