package aufgabe12_9;


// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class FormatVisitor extends Visitor {

  private StringBuilder result = new StringBuilder();

  private int level = 0;

  public String getResult() {
    return result.toString();
  }

  private void indent() {
    if (result.length() > 0) {
      result.append('\n');
    }
    for (int i = 0; i < level; i++) {
      result.append("  ");
    }
  }

  private void indented(String text) {
    indent();
    result.append(text);
  }

  private void append(Visitable node) {
    node.accept(this);
  }

  private void recurse(Visitable node) {
    level++;
    node.accept(this);
    level--;
  }

  /*
   * Expression
   */

  @Override
  public void visit(Number number) {
    result.append(number.getValue());
  }

  @Override
  public void visit(Variable variable) {
    result.append(variable.getName());
  }

  @Override
  public void visit(Unary unary) {
    result.append('-');
    int myPriority = unary.firstLevelPriority();
    int otherPriority = unary.getOperand().firstLevelPriority();
    if (myPriority <= otherPriority) {
      result.append('(');
    }
    append(unary.getOperand());
    if (myPriority <= otherPriority) {
      result.append(')');
    }
  }

  @Override
  public void visit(Binary binary) {
    boolean bracketSameRight = false;
    switch (binary.getOperator()) {
      case DivisionOperator:
      case Modulo:
      case Minus:
        bracketSameRight = true;
        break;
      case MultiplicationOperator:
      case Plus:
        bracketSameRight = false;
        break;
    }
    int myPriority = binary.firstLevelPriority();

    int leftPriority = binary.getLhs().firstLevelPriority();
    if (myPriority < leftPriority) {
      result.append('(');
    }
    append(binary.getLhs());
    if (myPriority < leftPriority) {
      result.append(')');
    }
    result.append(" " + binary.getOperator().codeString() + " ");
    int rightPriority = binary.getRhs().firstLevelPriority();
    // Manche Operatoren (z.B. -) erfordern Klammern auf der rechten
    // Seite auch für den Fall, dass die Priorität gleich ist
    if ((!bracketSameRight && myPriority < rightPriority)
        || (bracketSameRight && myPriority <= rightPriority)) {
      result.append('(');
    }
    append(binary.getRhs());
    if ((!bracketSameRight && myPriority < rightPriority)
        || (bracketSameRight && myPriority <= rightPriority)) {
      result.append(')');
    }
  }

  @Override
  public void visit(Call call) {
    result.append(call.getFunctionName() + "(");
    for (int i = 0; i < call.getArguments().length; i++) {
      if (i > 0) {
        result.append(", ");
      }
      append(call.getArguments()[i]);
    }
    result.append(')');
  }

  @Override
  public void visit(ArrayAccess arrayAccess) {
    int myPriority = arrayAccess.firstLevelPriority();
    int otherPriority = arrayAccess.getArray().firstLevelPriority();
    if (myPriority < otherPriority) {
      result.append('(');
    }
    append(arrayAccess.getArray());
    if (myPriority < otherPriority) {
      result.append(')');
    }
    result.append('[');
    append(arrayAccess.getIndex());
    result.append(']');
  }

  @Override
  public void visit(ArrayInitializer arrayInitializer) {
    result.append("new ");
    result.append(arrayInitializer.getType());
    result.append("[");
    append(arrayInitializer.getSize());
    result.append(']');
  }

  @Override
  public void visit(ArrayLength arrayLength) {
    result.append("length(");
    append(arrayLength.getArray());
    result.append(')');
  }

  /*
   * Statement
   */

  @Override
  public void visit(Read read) {
    indented(read.getName() + " = read();");
  }

  @Override
  public void visit(Write write) {
    indented("write(");
    append(write.getExpression());
    result.append(");");
  }

  @Override
  public void visit(Assignment assignment) {
    indented(assignment.getName() + " = ");
    append(assignment.getExpression());
    result.append(";");
  }

  @Override
  public void visit(Composite composite) {
    indented("{");
    for (int i = 0; i < composite.getStatements().length; i++) {
      recurse(composite.getStatements()[i]);
    }
    indent();
    result.append('}');
  }

  @Override
  public void visit(IfThenElse ifThenElse) {
    indented("if (");
    ifThenElse.getCond().accept(this);
    result.append(")");
    recurse(ifThenElse.getThenBranch());
    indented("else");
    recurse(ifThenElse.getElseBranch());
  }

  @Override
  public void visit(IfThen ifThen) {
    indented("if (");
    ifThen.getCond().accept(this);
    result.append(")");
    recurse(ifThen.getThenBranch());
  }

  @Override
  public void visit(While while_) {
    indented("while(");
//    result.append("while(");
    while_.getCond().accept(this);
    result.append(") ");
    recurse(while_.getBody());
  }

  @Override
  public void visit(Return return_) {
    indented("return ");
    return_.getExpression().accept(this);
    result.append(";");
  }

  @Override
  public void visit(EmptyStatement emptyStatement) {
  }

  @Override
  public void visit(ArrayAssignment arrayAssignment) {
    indented(arrayAssignment.getName() + "[");
    arrayAssignment.getIndex().accept(this);
    result.append("] = ");
    append(arrayAssignment.getRhs());
    result.append(";");

  }

  /*
   * Condition
   */

  @Override
  public void visit(True true_) {
    result.append("true");
  }

  @Override
  public void visit(False false_) {
    result.append("false");
  }

  @Override
  public void visit(Comparison comparison) {
    append(comparison.getLhs());
    result.append(" " + comparison.getOperator().codeString() + " ");
    append(comparison.getRhs());
  }

  @Override
  public void visit(UnaryCondition unaryCondition) {
    result.append('!');
    int myPriority = unaryCondition.firstLevelPriority();
    int otherPriority = unaryCondition.getOperand().firstLevelPriority();
    if (myPriority < otherPriority) {
      result.append('(');
    }
    append(unaryCondition.getOperand());
    if (myPriority < otherPriority) {
      result.append(')');
    }
  }

  @Override
  public void visit(BinaryCondition binaryCondition) {
    int myPriority = binaryCondition.firstLevelPriority();

    int leftPriority = binaryCondition.getLhs().firstLevelPriority();
    if (myPriority < leftPriority) {
      result.append('(');
    }
    append(binaryCondition.getLhs());
    if (myPriority < leftPriority) {
      result.append(')');
    }

    switch (binaryCondition.getOperator()) {
      case And:
        result.append(" && ");
        break;
      case Or:
        result.append(" || ");
        break;
    }

    int rightPriority = binaryCondition.getRhs().firstLevelPriority();
    if (myPriority < rightPriority) {
      result.append('(');
    }
    append(binaryCondition.getRhs());
    if (myPriority < rightPriority) {
      result.append(')');
    }
  }

  /*
   * Rest
   */

  private void visitDeclarations(Type type, String[] names) {
    indented(type.codeString());
    result.append(' ');
    for (int i = 0; i < names.length; i++) {
      if (i > 0) {
        result.append(", ");
      }
      result.append(names[i]);
    }
    result.append(';');
  }

  @Override
  public void visit(SingleDeclaration singleDeclaration) {
    visitDeclarations(singleDeclaration.getType(), new String[]{singleDeclaration.getName()});
  }

  @Override
  public void visit(Declaration declaration) {
    visitDeclarations(declaration.getType(), declaration.getNames());
  }

  @Override
  public void visit(Function function) {
    indented(function.getReturnType().codeString());
    result.append(' ');
    result.append(function.getName());
    result.append('(');
    for (int i = 0; i < function.getParameters().length; i++) {
      SingleDeclaration sd = function.getParameters()[i];
      if (i > 0) {
        result.append(", ");
      }
      result.append(sd.getType().codeString());
      result.append(' ');
      result.append(sd.getName());
    }
    result.append(")");
    indent();
    result.append("{");
    for (Declaration decl : function.getDeclarations()) {
      recurse(decl);
    }
    for (Statement stmt : function.getStatements()) {
      recurse(stmt);
    }
    indented("}");
  }

  @Override
  public void visit(Program program) {
    for (int i = 0; i < program.getClasses().length; i++) {
      if (i > 0) {
        result.append("\n");
      }
      program.getClasses()[i].accept(this);
    }

    for (int i = 0; i < program.getFunctions().length; i++) {
      if (i > 0) {
        result.append("\n");
      }
      program.getFunctions()[i].accept(this);
    }
  }


  @Override
  public void visit(Class classObj) {
    result.append("class ");
    result.append(classObj.getName());
    result.append(" ");
    if (classObj.getSuperClass() != null) {
      result.append(String.format("extends %s ", classObj.getSuperClass()));
    }
    result.append("{");
    for (SingleDeclaration s : classObj.getFields()) {
      recurse(s);
      result.append("\n");
    }
    recurse(classObj.getConstructor());
    for (Function function : classObj.getFunctions()) {
      recurse(function);
    }
    indented("}\n");
  }

  @Override
  public void visit(Constructor constructor) {
    indent();
    result.append(constructor.getName());
    result.append("(");
    for (int i = 0; i < constructor.getParameters().length; i++) {
      SingleDeclaration sd = constructor.getParameters()[i];
      if (i > 0) {
        result.append(", ");
      }
      result.append(sd.getType().codeString());
      result.append(' ');
      result.append(sd.getName());
    }
    result.append(")");
    if (constructor.getDeclarations().length != 0 ||
        constructor.getStatements().length != 0) {
      indent();
    }
    result.append("{");
    for (Declaration declaration : constructor.getDeclarations()) {
      recurse(declaration);
    }
    for (Statement statement : constructor.getStatements()) {
      recurse(statement);
    }
    indented("}\n");
  }

  @Override
  public void visit(MethodCall methodCall) {
    result.append(methodCall.getRefName());
    result.append(".");
    result.append(methodCall.getMethodName());
    result.append("(");
    for (int i = 0; i < methodCall.getArguments().length; i++) {
      Expression ex = methodCall.getArguments()[i];
      if (i > 0) {
        result.append(", ");
      }
      result.append(ex);
    }
    result.append(")");
  }

  @Override
  public void visit(ObjectInitializer objectInitializer) {
    result.append("new ");
    result.append(objectInitializer.getClassName());
    result.append("(");
    for (int i = 0; i < objectInitializer.getArguments().length; i++) {
      Expression ex = objectInitializer.getArguments()[i];
      if (i > 0) {
        result.append(", ");
      }
      result.append(ex);
    }
    result.append(")");
  }

  @Override
  public void visit(ExpressionStatement expressionStatement) {
    expressionStatement.getExpression().accept(this);
    result.append(";");
  }
}
