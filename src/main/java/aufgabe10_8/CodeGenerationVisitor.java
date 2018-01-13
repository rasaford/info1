package aufgabe10_7;

import static aufgabe10_7.SteckOperation.*;

import java.util.ArrayList;
import java.util.HashMap;

class PatchLocation {

  public final String functionName;
  public final int argumentCount;
  public final int ldiLocation;

  public PatchLocation(String functionName, int argumentCount, int ldiLocation) {
    this.functionName = functionName;
    this.argumentCount = argumentCount;
    this.ldiLocation = ldiLocation;
  }
}

class FunctionDesc {

  public final int functionIndex;
  public final int argumentCount;

  public FunctionDesc(int functionIndex, int argumentCount) {
    this.functionIndex = functionIndex;
    this.argumentCount = argumentCount;
  }
}

public class CodeGenerationVisitor extends Visitor {

  /*
   * Liste für die generierten Instruktionen. Eine ArrayList verhält
   * sich wie ein Array, welches wachsen kann.
   */
  private ArrayList<Integer> instructions = new ArrayList<>();
  /*
   * HashMap für lokale Variablen. Man beachte, dass es lokale Variablen
   * nur als Funktionsparameter und am Anfang von Funktionen gibt; daher
   * gibt es nur genau einen Scope.
   */
  private HashMap<String, Integer> locals = new HashMap<>();
  /*
   * Frame-Zellen der aktuellen Funktion. Wird für den Rücksprung benötigt.
   */
  private int frameCells = 0;
  /*
   * Funktionen, die bereits assembliert wurden. Wir merken uns alle Funktionen,
   * sodass wir am Ende die Call-Sites patchen können.
   */
  private HashMap<String, FunctionDesc> functions = new HashMap<>();
  /*
   * Call-Sites; Funktionsaufrufe müssen ganz am Ende generiert werden, nämlich
   * dann, wenn die Funktionen schon bekannte Adressen haben.
   */
  private ArrayList<PatchLocation> patchLocations = new ArrayList<>();

  public int[] getProgram() {
    // Umwandlung von Integer[] nach int[]
    Integer[] program = instructions.toArray(new Integer[0]);
    int[] intProgram = new int[program.length];
    for (int i = 0; i < intProgram.length; i++) {
      intProgram[i] = program[i];
    }
    return intProgram;
  }

  private int add(int instruction) {
    int index = instructions.size();
    instructions.add(instruction);
    return index;
  }

  private int addDummy() {
    return add(0);
  }
  
  /*
   * Expression
   */

  @Override
  public void visit(Number number) {
    int value = number.getValue();
    add(LDI.encode(value & 0xffff));
    // Für den Fall, dass der Wert mehr als 16 Bit braucht, benötigen
    // wir mehrere Operationen. Wir legen die oberen 16 Bit auf den Stack,
    // schieben diese an die richtige Stelle, und vereinen zum Schluss die
    // oberen und unteren Bits.
    if (value > 0xffff || value < 0) {
      add(LDI.encode(value >>> 16));
      add(SHL.encode(16));
      add(OR.encode());

    }
  }

  @Override
  public void visit(Variable variable) {
    if (!locals.containsKey(variable.getName())) {
      throw new RuntimeException("Unknown variable '" + variable.getName() + "'");
    }
    int variableLocation = locals.get(variable.getName());
    add(LDS.encode(variableLocation));
  }

  @Override
  public void visit(Unary unary) {
    switch (unary.getOperator()) {
      case Minus:
        unary.getOperand().accept(this);
        add(LDI.encode(0));
        add(SUB.encode());
        break;
    }
  }

  @Override
  public void visit(Binary binary) {
    binary.getRhs().accept(this);
    binary.getLhs().accept(this);
    switch (binary.getOperator()) {
      case Minus:
        add(SUB.encode());
        break;
      case Plus:
        add(ADD.encode());
        break;
      case MultiplicationOperator:
        add(MUL.encode());
        break;
      case DivisionOperator:
        add(DIV.encode());
        break;
      case Modulo:
        add(MOD.encode());
        break;
    }
  }

  @Override
  public void visit(Call call) {
    for (Expression e : call.getArguments()) {
      e.accept(this);
    }
    int patchLocation = addDummy();
    // Wir können die Instruktion zum Laden der Adresse hier noch nicht generieren; die
    // Funktion wird evtl. erst später assembliert.
    patchLocations
        .add(new PatchLocation(call.getFunctionName(), call.getArguments().length, patchLocation));
    add(CALL.encode(call.getArguments().length));
  }
  
  /*
   * Statement
   */

  @Override
  public void visit(Read read) {
    add(IN.encode());
    int location = locals.get(read.getName());
    add(STS.encode(location));
  }

  @Override
  public void visit(Write write) {
    write.getExpression().accept(this);
    add(OUT.encode());
  }

  @Override
  public void visit(Assignment assignment) {
    assignment.getExpression().accept(this);
    int location = locals.get(assignment.getName());
    add(STS.encode(location));
  }

  @Override
  public void visit(Composite composite) {
    for (Statement s : composite.getStatements()) {
      s.accept(this);
    }
  }

  @Override
  public void visit(IfThenElse ifThenElse) {
    ifThenElse.getCond().accept(this);
    int jumpToElseAddress = addDummy();
    ifThenElse.getElseBranch().accept(this);
    new True().accept(this);
    int jumpToEndAddress = addDummy();
    // Wir setzen den Sprung zum Else-Zweig
    instructions.set(jumpToElseAddress, JUMP.encode(instructions.size()));
    ifThenElse.getThenBranch().accept(this);
    // Wir setzen den Sprung zum Ende am Ende des Else-Zweiges
    instructions.set(jumpToEndAddress, JUMP.encode(instructions.size()));
  }

  @Override
  public void visit(IfThen ifThen) {
    ifThen.getCond().accept(this);
    add(NOT.encode());
    int jumpToEndAddress = addDummy();
    ifThen.getThenBranch().accept(this);
    instructions.set(jumpToEndAddress, JUMP.encode(instructions.size()));
  }

  @Override
  public void visit(While while_) {
    int whileBeginAddress = instructions.size();
    while_.getCond().accept(this);
    add(NOT.encode());
    int jumpToEndAddress = addDummy();
    while_.getBody().accept(this);
    new True().accept(this);
    add(JUMP.encode(whileBeginAddress));
    int whileEndAddress = instructions.size();
    // Wir setzen den Sprung ans Ende der Schleife
    instructions.set(jumpToEndAddress, JUMP.encode(whileEndAddress));
  }

  @Override
  public void visit(Return return_) {
    return_.getExpression().accept(this);
    add(RETURN.encode(frameCells));
  }

  @Override
  public void visit(EmptyStatement emptyStatement) {
  }
  
  /*
   * Condition
   */

  @Override
  public void visit(True true_) {
    add(LDI.encode(0));
    add(NOT.encode(0));
  }

  @Override
  public void visit(False false_) {
    add(LDI.encode(0));
  }

  @Override
  public void visit(Comparison comparison) {
    comparison.getRhs().accept(this);
    comparison.getLhs().accept(this);
    switch (comparison.getOpeator()) {
      case Equals:
        add(EQ.encode());
        break;
      case NotEquals:
        add(EQ.encode());
        add(NOT.encode());
        break;
      case Greater:
        add(LE.encode());
        add(NOT.encode());
        break;
      case GreaterEqual:
        add(LT.encode());
        add(NOT.encode());
        break;
      case Less:
        add(LT.encode());
        break;
      case LessEqual:
        add(LE.encode());
        break;
    }
  }

  @Override
  public void visit(UnaryCondition unaryCondition) {
    unaryCondition.getOperand().accept(this);
    switch (unaryCondition.getOperator()) {
      case Not:
        add(NOT.encode());
        break;
    }
  }

  @Override
  public void visit(BinaryCondition binaryCondition) {
    binaryCondition.getRhs().accept(this);
    binaryCondition.getLhs().accept(this);
    switch (binaryCondition.getOperator()) {
      case And:
        add(AND.encode());
        break;
      case Or:
        add(OR.encode());
        break;
    }
  }
  
  /*
   * Rest
   */

  @Override
  public void visit(Declaration declaration) {
    int offset = locals.size() + 1;
    String[] names = declaration.getNames();
    for (int i = 0; i < names.length; i++) {
      if (locals.containsKey(names[i])) {
        throw new RuntimeException("Variable '" + names[i] + "' is already defined");
      }
      locals.put(names[i], offset + i);
    }
    // Beachte: Mehrere ALLOCs am Funktionsanfang
    // hintereinander sind ok
    add(ALLOC.encode(names.length));
  }

  @Override
  public void visit(Function function) {
    int declarations = 0;
    for (Declaration d : function.getDeclarations()) {
      declarations += d.getNames().length;
      d.accept(this);
    }
    String[] parameters = function.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (locals.containsKey(parameters[i])) {
        throw new RuntimeException("Variable '" + parameters[i] + "' is already defined");
      }
      locals.put(parameters[i], -parameters.length + i + 1);
    }
    frameCells = parameters.length + declarations;
    for (Statement s : function.getStatements()) {
      s.accept(this);
    }
  }

  @Override
  public void visit(Program program) {
    // Das Programm beginnt mit einem Sprung zur Hauptfunktion
    int ldiMainAddress = addDummy();
    add(CALL.encode(0));
    add(HALT.encode());
    boolean hasMain = false;
    for (Function f : program.getFunctions()) {
      int functionStartIndex = instructions.size();
      if (f.getName().equals("main")) {
        if (f.getParameters().length > 0) {
          throw new RuntimeException("Main function must not have parameters");
        }
        instructions.set(ldiMainAddress, LDI.encode(functionStartIndex));
        hasMain = true;
      }
      functions.put(f.getName(), new FunctionDesc(functionStartIndex, f.getParameters().length));
      locals.clear();
      f.accept(this);
    }
    if (!hasMain) {
      throw new RuntimeException("Main function is missing.");
    }
    // Nachdem alle Funktionen assembliert wurden, müssen wir die Instruktionen patchen, die
    // Funktionsadressen laden.
    for (PatchLocation pl : patchLocations) {
      FunctionDesc fDesc = functions.get(pl.functionName);
      if (fDesc.argumentCount != pl.argumentCount) {
        throw new RuntimeException("Invalid number of function arguments");
      }
      instructions.set(pl.ldiLocation, LDI.encode(fDesc.functionIndex));
    }
  }
}
