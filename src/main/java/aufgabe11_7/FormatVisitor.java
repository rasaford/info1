package aufgabe11_7;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FormatVisitor extends Visitor {

  private StringBuilder sb;
  private int prevPriority;
  private int indentation;
  private boolean commutative;
  private boolean newLine;

  public FormatVisitor() {
    this.sb = new StringBuilder();
    this.prevPriority = Integer.MAX_VALUE;
    this.commutative = true;
    this.indentation = 0;
    this.newLine = false;
  }

  public String getResult() {
    return sb.toString();
  }

  private StringBuilder appendf(String format, Object... args) {
    if (newLine) {
      for (int i = 0; i < indentation; i++) {
        sb.append("  ");
      }
    }
    newLine = format.endsWith("\n");
    return sb.append(String.format(format, args));
  }


  private StringBuilder render(Consumer<FormatVisitor> consumer) {
    StringBuilder oldSB = sb;
    sb = new StringBuilder();
    consumer.accept(this);
    StringBuilder block = sb;
    sb = oldSB;
    return block;
  }

  private StringBuilder parenthesize(Consumer<FormatVisitor> consumer) {
    return sb.append(
        render(consumer)
            .insert(0, "(")
            .append(")")
    );
  }

  private StringBuilder render(String... args) {
    for (int i = 0; i < args.length; i++) {
      String name = args[i];
      if (i == args.length - 1) {
        appendf(name);
      } else {
        appendf("%s, ", name);
      }
    }
    return sb;
  }

  private StringBuilder render(Expression... args) {
    String[] strings = Arrays.stream(args)
        .map(expr -> render(v -> expr.accept(this)).toString())
        .toArray(String[]::new);
    return render(strings);
  }

  @Override
  public void visit(Number number) {
    appendf(Integer.toString(number.getValue()));
  }

  @Override
  public void visit(Variable nameExpression) {
    appendf(nameExpression.getName());
  }

  @Override
  public void visit(Unary unary) {
    switch (unary.getOperator()) {
      case Minus:
        appendf("-");
        break;
    }
    int priority = unary.getOperator().getPriority();
    int oldPriority = prevPriority;
    if (priority > prevPriority) {
      prevPriority = priority;
      parenthesize(v -> unary.getOperand().accept(this));
      prevPriority = oldPriority;
    } else {
      prevPriority = priority;
      unary.getOperand().accept(this);
      prevPriority = oldPriority;
    }
  }

  @Override
  public void visit(Binary binary) {
    final int priority = binary.getOperator().getPriority();
    final int oldPriority = prevPriority;
    // fun times with global variables :)
    BiConsumer<String, Boolean> block = (operator, commutative) -> {
      this.prevPriority = priority;
      binary.getLhs().accept(this);
      appendf(operator);

      boolean oldcommutative = this.commutative;
      this.commutative = commutative;
      binary.getRhs().accept(this);
      this.commutative = oldcommutative;
      this.prevPriority = oldPriority;
    };
    switch (binary.getOperator()) {
      case Minus:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" - ", false));
        } else {
          block.accept(" - ", false);
        }
        break;
      case Plus:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" + ", true));
        } else {
          block.accept(" + ", true);
        }
        break;
      case MultiplicationOperator:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" * ", true));
        } else {
          block.accept(" * ", true);
        }
        break;
      case DivisionOperator:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" / ", false));
        } else {
          block.accept(" / ", false);
        }
        break;
      case Modulo:
        if (priority > prevPriority || !commutative) {
          // %% to escape the character in the format string.
          parenthesize(v -> block.accept(" %% ", false));
        } else {
          block.accept(" %% ", false);
        }
        break;
    }
  }

  @Override
  public void visit(Function function) {
    appendf("%s %s(", "int", function.getName());
    render(Arrays.stream(function.getParameters())
        // prepending the type
        .map(p -> "int " + p)
        .toArray(String[]::new));
    appendf(") {\n");
    indentation++;
    for (Declaration decl : function.getDeclarations()) {
      decl.accept(this);
    }
    for (Statement stmt : function.getStatements()) {
      stmt.accept(this);
    }
    indentation--;
    appendf("}\n");
  }

  @Override
  public void visit(Declaration declaration) {
    appendf("int ");
    render(declaration.getNames());
    appendf(";\n");
  }

  @Override
  public void visit(Read read) {
    appendf("%s = read();", read.getName());
  }

  @Override
  public void visit(Assignment assignment) {
    appendf("%s = ", assignment.getName());
    assignment.getExpression().accept(this);
    appendf(";\n");
  }

  @Override
  public void visit(Composite composite) {
    appendf("{\n");
    indentation++;
    for (Statement s : composite.getStatements()) {
      s.accept(this);
    }
    indentation--;
    appendf("}\n");
  }

  @Override
  public void visit(Comparison comparison) {
    final int priority = comparison.getOpeator().getPriority();
    final int oldPriority = prevPriority;
    // fun times with global variables :)
    BiConsumer<String, Boolean> block = (operator, commutative) -> {
      this.prevPriority = priority;
      comparison.getLhs().accept(this);
      appendf(operator);

      boolean oldcommutative = this.commutative;
      this.commutative = commutative;
      comparison.getRhs().accept(this);
      this.commutative = oldcommutative;
      this.prevPriority = oldPriority;
    };
    switch (comparison.getOpeator()) {
      case Equals:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" == ", true));
        } else {
          block.accept(" == ", true);
        }
        break;
      case NotEquals:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" != ", true));
        } else {
          block.accept(" != ", true);
        }
        break;
      case LessEqual:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" <= ", false));
        } else {
          block.accept(" <= ", false);
        }
        break;
      case Less:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" < ", false));
        } else {
          block.accept(" < ", false);
        }
        break;
      case GreaterEqual:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" >= ", false));
        } else {
          block.accept(" >= ", false);
        }
        break;
      case Greater:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" > ", false));
        } else {
          block.accept(" > ", false);
        }
        break;
    }
  }

  @Override
  public void visit(While while_) {
    appendf("while (");
    while_.getCond().accept(this);
    appendf(")\n");
    while_.getBody().accept(this);
  }

  @Override
  public void visit(True true_) {
    appendf("true");
  }

  @Override
  public void visit(False false_) {
    appendf("false");
  }

  @Override
  public void visit(UnaryCondition unaryCondition) {
    switch (unaryCondition.getOperator()) {
      case Not:
        appendf("!");
        break;
    }
    int priority = unaryCondition.getOperator().getPriority();
    int oldPriority = prevPriority;
    prevPriority = priority;
    unaryCondition.getOperand().accept(this);
    prevPriority = oldPriority;
  }

  @Override
  public void visit(BinaryCondition binaryCondition) {
    final int priority = binaryCondition.getOperator().getPriority();
    // fun times with global variables :)
    Consumer<String> block = (operator) -> {
      int oldPriority = prevPriority;
      this.prevPriority = priority;
      binaryCondition.getLhs().accept(this);
      appendf(operator);
      binaryCondition.getRhs().accept(this);
      this.prevPriority = oldPriority;
    };
    switch (binaryCondition.getOperator()) {
      case And:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" && "));
        } else {
          block.accept(" && ");
        }
        break;
      case Or:
        if (priority > prevPriority || !commutative) {
          parenthesize(v -> block.accept(" || "));
        } else {
          block.accept(" || ");
        }
        break;
    }
  }

  @Override
  public void visit(IfThenElse ifThenElse) {
    appendf("if (");
    ifThenElse.getCond().accept(this);
    appendf(")\n");
    if (ifThenElse.getThenBranch() instanceof Composite) {
      ifThenElse.getThenBranch().accept(this);
    } else {
      indentation++;
      ifThenElse.getThenBranch().accept(this);
      indentation--;
    }
    appendf("else\n");
    if (ifThenElse.getElseBranch() instanceof Composite) {
      ifThenElse.getElseBranch().accept(this);
    } else {
      indentation++;
      ifThenElse.getElseBranch().accept(this);
      indentation--;
    }
  }

  @Override
  public void visit(Program program) {
    for (int i = 0; i < program.getFunctions().length; i++) {
      program.getFunctions()[i].accept(this);
      if (i != program.getFunctions().length - 1) {
        appendf("\n");
      }
    }
  }

  @Override
  public void visit(IfThen ifThen) {
    appendf("if (");
    ifThen.getCond().accept(this);
    appendf(")\n");
    if (ifThen.getThenBranch() instanceof Composite) {
      ifThen.getThenBranch().accept(this);
    } else {
      indentation++;
      ifThen.getThenBranch().accept(this);
      indentation--;
    }
  }

  @Override
  public void visit(Call call) {
    appendf("%s(", call.getFunctionName());
    // priorities get reset within function calls.
    int oldPriority = prevPriority;
    prevPriority  = Integer.MAX_VALUE;
    render(call.getArguments());
    prevPriority = oldPriority;
    appendf(")");
  }

  @Override
  public void visit(Return return_) {
    appendf("return ");
    return_.getExpression().accept(this);
    appendf(";\n");
  }

  @Override
  public void visit(Write write) {
    appendf("write(");
    write.getExpression().accept(this);
    appendf(");");
  }

  @Override
  public void visit(EmptyStatement emptyStatement) {
  }

  @Override
  public void visit(ArrayInitializer initializer) {

  }

  @Override
  public void visit(ArrayAccess arrayAccess) {

  }

  @Override
  public void visit(ArrayLength length) {

  }

  @Override
  public void visit(ArrayAssignment assignment) {

  }
}
