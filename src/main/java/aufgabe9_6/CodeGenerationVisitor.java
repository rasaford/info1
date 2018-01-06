package aufgabe9_6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CodeGenerationVisitor extends Visitor {

  private Code code = new Code();
  private List<String> variables = new ArrayList<>();
  private int paramOffset = 0;
  private List<String> functions = new ArrayList<>();

  @Override
  public void visit(Function function) {
    functions.add(function.getName());
    code.appendlnf("%s:", function.getName());
    paramOffset = -function.getParameters().length + 1;
    for (String p : function.getParameters()) {
      variables.add(p);
    }
    for (Declaration d : function.getDeclarations()) {
      d.accept(this);
    }
    for (Statement s : function.getStatements()) {
      s.accept(this);
    }
    variables.clear();
    paramOffset = 0;
  }

  @Override
  public void visit(Return ret) {
    ret.getExpression().accept(this);
    code.appendlnf("RETURN %d", variables.size());
  }

  public int[] getProgram() {
    System.out.println(code.toString());
    int[] machineCode = Interpreter.parse(code.toString());
    // cleanup
    functions.clear();
    code.clear();
    variables.clear();
    paramOffset = 0;
    return machineCode;
  }

  @Override
  public void visit(Unary unary) {
    unary.getOperand().accept(this);
    code.appendlnf("LDI 0");
    code.appendlnf("SUB");
  }

  @Override
  public void visit(Binary binary) {
    binary.getRhs().accept(this);
    binary.getLhs().accept(this);
    switch (binary.getOperator()) {
      case Minus:
        code.appendlnf("SUB");
        break;
      case Plus:
        code.appendlnf("ADD");
        break;
      case MultiplicationOperator:
        code.appendlnf("MUL");
        break;
      case DivisionOperator:
        code.appendlnf("DIV");
        break;
      case Modulo:
        code.appendlnf("MOD");
        break;
    }
  }

  @Override
  public void visit(Number number) {
    int val = number.getValue();
    if (val < Short.MIN_VALUE || val > Short.MAX_VALUE) {
      // pushing 4 bytes onto the stack then merging to avoid the
      // automatic sign extension.
      code.appendlnf("LDI %d", val >> 24);
      code.appendlnf("SHL %d", 24);
      code.appendlnf("LDI %d", (val & 0xFF0000) >> 16);
      code.appendlnf("SHL %d", 16);
      code.appendlnf("LDI %d", (val & 0xFF00) >> 8);
      code.appendlnf("SHL %d", 8);
      code.appendlnf("LDI %d", (val & 0xFF));
      code.appendlnf("OR");
      code.appendlnf("OR");
      code.appendlnf("OR");
    } else {
      code.appendlnf("LDI %d", val);
    }
  }

  @Override
  public void visit(Variable variable) {
    if (!variables.contains(variable.getName())) {
      errorf("local variable %s is not defined", variable.getName());
    }
    int index = variables.indexOf(variable.getName()) + paramOffset;
    code.appendlnf("LDS %d", index);
  }

  @Override
  public void visit(Call call) {
    for (Expression e : call.getArguments()) {
      e.accept(this);
    }
    String name = call.getFunctionName();
    if (!functions.contains(name)) {
      errorf("Undefined function %s", name);
    }
    code.appendlnf("LDI %s", call.getFunctionName());
    code.appendlnf("CALL %d", call.getArguments().length);
  }

  @Override
  public void visit(Assignment assignment) {
    if (!variables.contains(assignment.getName())) {
      errorf("local variable %s is not defined", assignment.getName());
    }
    assignment.getExpression().accept(this);
    int index = variables.indexOf(assignment.getName()) + paramOffset;
    code.appendlnf("STS %d", index);
  }

  @Override
  public void visit(Declaration declaration) {
    code.appendlnf("ALLOC %d", declaration.getNames().length);
    for (String name : declaration.getNames()) {
      if (!variables.contains(name)) {
        variables.add(name);
      } else {
        errorf("the variable %s has already been declared");
      }
    }
  }

  @Override
  public void visit(BinaryCondition binaryCondition) {
    binaryCondition.getRhs().accept(this);
    binaryCondition.getLhs().accept(this);
    switch (binaryCondition.getOperator()) {
      case And:
        code.appendlnf("AND");
        break;
      case Or:
        code.appendlnf("OR");
        break;
    }
  }

  @Override
  public void visit(UnaryCondition unaryCondition) {
    unaryCondition.getOperand().accept(this);
    switch (unaryCondition.getOperator()) {
      case Not:
        code.appendlnf("NOT");
        break;
    }
  }

  @Override
  public void visit(Comparison comparison) {
    comparison.getRhs().accept(this);
    comparison.getLhs().accept(this);
    switch (comparison.getOperator()) {
      case Equals:
        code.appendlnf("EQ");
        break;
      case NotEquals:
        code.appendlnf("EQ");
        code.appendlnf("NOT");
        break;
      case LessEqual:
        code.appendlnf("LE");
        break;
      case Less:
        code.appendlnf("LT");
        break;
      case GreaterEqual:
        code.appendlnf("LT");
        code.appendlnf("NOT");
        break;
      case Greater:
        code.appendlnf("LE");
        code.appendlnf("NOT");
        break;
    }
  }

  @Override
  public void visit(False f) {
    code.appendlnf("LDI 0");
  }

  @Override
  public void visit(True t) {
    code.appendlnf("LDI -1");
  }

  @Override
  public void visit(Composite composite) {
    for (Statement s : composite.getStatements()) {
      s.accept(this);
    }
  }

  @Override
  public void visit(Read read) {
    if (!variables.contains(read.getName())) {
      errorf("local variable %s is not defined", read.getName());
    }
    int index = variables.indexOf(read.getName()) + paramOffset;
    code.appendlnf("IN");
    code.appendlnf("STS %d", index);
  }

  @Override
  public void visit(While w) {
    int condLine = code.getLineCount();
    w.getCond().accept(this);
    Code body = renderBlock(visitor -> w.getBody().accept(this));
    code.appendlnf("NOT");
    code.appendlnf("JUMP %d", code.getLineCount() + body.getLineCount() + 3);
    code.merge(body);
    code.appendlnf("LDI -1");
    code.appendlnf("JUMP %d", condLine);
  }

  @Override
  public void visit(IfThen ifThen) {
    ifThen.getCond().accept(this);
    Code then = renderBlock(visitor ->
        ifThen.getThenBranch().accept(visitor));
    code.appendlnf("NOT");
    code.appendlnf("JUMP %d", code.getLineCount() + then.getLineCount() + 1);
    code.merge(then);
  }

  @Override
  public void visit(IfThenElse ifThenElse) {
    ifThenElse.getCond().accept(this);
    Code then = renderBlock(visitor ->
        ifThenElse.getThenBranch().accept(visitor));
    Code el = renderBlock(visitor ->
        ifThenElse.getElseBranch().accept(visitor));
    code.appendlnf("NOT");
    // jump to else
    code.appendlnf("JUMP %d", code.getLineCount() + then.getLineCount() + 3);
    code.merge(then);
    // skip else
    code.appendlnf("LDI -1");
    code.appendlnf("JUMP %d", code.getLineCount() + el.getLineCount() + 1);
    code.merge(el);
  }

  @Override
  public void visit(Write write) {
    write.getExpression().accept(this);
    code.appendlnf("OUT");
  }

  @Override
  public void visit(Program p) {
    for (Function f : p.getFunctions()) {
      functions.add(f.getName());
    }
    if (!functions.contains("main")) {
      errorf("The program does not contain a main function");
    }
    Call main = new Call("main", new Expression[0]);
    main.accept(this);
    code.appendlnf("HALT");

    for (Function f : p.getFunctions()) {
      f.accept(this);
    }
  }

  public void errorf(String format, Object... args) {
    throw new RuntimeException(String.format(format, args));
  }

  private Code renderBlock(Consumer<Visitor> consumer) {
    Code prev = code;
    code = new Code();
    consumer.accept(this);
    Code block = code;
    code = prev;
    return block;
  }

  class Code {

    private StringBuilder sb = new StringBuilder();
    private int lines = 0;

    public Code merge(Code other) {
      sb.append(other.toString());
      lines += other.getLineCount();
      return this;
    }

    public Code appendlnf(String format, Object... args) {
      sb.append(String.format(format, args));
      sb.append("\n");
      lines++;
      return this;
    }

    public int getLineCount() {
      return lines;
    }

    @Override
    public String toString() {
      return sb.toString();
    }

    public void clear() {
      sb = new StringBuilder();
      lines = 0;
    }
  }
}
