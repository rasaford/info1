package aufgabe12_9;


import static aufgabe12_9.SteckOperation.ADD;
import static aufgabe12_9.SteckOperation.ALLOC;
import static aufgabe12_9.SteckOperation.ALLOCH;
import static aufgabe12_9.SteckOperation.AND;
import static aufgabe12_9.SteckOperation.CALL;
import static aufgabe12_9.SteckOperation.DIV;
import static aufgabe12_9.SteckOperation.EQ;
import static aufgabe12_9.SteckOperation.HALT;
import static aufgabe12_9.SteckOperation.IN;
import static aufgabe12_9.SteckOperation.JUMP;
import static aufgabe12_9.SteckOperation.LDH;
import static aufgabe12_9.SteckOperation.LDI;
import static aufgabe12_9.SteckOperation.LDS;
import static aufgabe12_9.SteckOperation.LE;
import static aufgabe12_9.SteckOperation.LT;
import static aufgabe12_9.SteckOperation.MOD;
import static aufgabe12_9.SteckOperation.MUL;
import static aufgabe12_9.SteckOperation.NOP;
import static aufgabe12_9.SteckOperation.NOT;
import static aufgabe12_9.SteckOperation.OR;
import static aufgabe12_9.SteckOperation.OUT;
import static aufgabe12_9.SteckOperation.RETURN;
import static aufgabe12_9.SteckOperation.SHL;
import static aufgabe12_9.SteckOperation.STH;
import static aufgabe12_9.SteckOperation.STS;
import static aufgabe12_9.SteckOperation.SUB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


class FunctionDesc {

  public final int functionIndex;
  public final int argumentCount;

  public FunctionDesc(int functionIndex, int argumentCount) {
    this.functionIndex = functionIndex;
    this.argumentCount = argumentCount;
  }
}


class ClassDesc {

  private final int allocIndex;
  private final String superClass;
  private int constructorIndex;
  private Map<String, Integer> methodPointers = new HashMap<>();
  private Map<String, Integer> heapVars = new HashMap<>();
  private List<String> vTable = new ArrayList<>();
  private Map<String, Integer> argumentCounts = new HashMap<>();


  public ClassDesc(int allocIndex, String superClass) {
    this.allocIndex = allocIndex;
    this.superClass = superClass;
  }

  public int getAllocIndex() {
    return allocIndex;
  }

  public String getSuper() {
    return superClass;
  }

  public void setConstructorIndex(int constructorIndex) {
    this.constructorIndex = constructorIndex;
  }

  public int getConstructorIndex() {
    return constructorIndex;
  }

  public void setvTable(List<String> vTable) {
    this.vTable = vTable;
  }

  public List<String> getvTable() {
    return vTable;
  }

  public Map<String, Integer> getMethodPointers() {
    return methodPointers;
  }

  public void setHeapVars(Map<String, Integer> heapVars) {
    this.heapVars.clear();
    this.heapVars.putAll(heapVars);
  }

  public Map<String, Integer> getHeapVars() {
    return new HashMap<>(heapVars);
  }

  public Map<String, Integer> getArgumentCounts() {
    return argumentCounts;
  }
}

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class CodeGenerationVisitor extends Visitor {

  /*
   * Liste für die generierten Instruktionen. Eine ArrayList verhält sich wie ein Array, welches
   * wachsen kann.
   */
  private ArrayList<Integer> instructions = new ArrayList<>();
  /*
   * HashMap für lokale Variablen. Man beachte, dass es lokale Variablen nur als Funktionsparameter
   * und am Anfang von Funktionen gibt; daher gibt es nur genau einen Scope.
   */
  private HashMap<String, Integer> locals = new HashMap<>();
  /*
   * Frame-Zellen der aktuellen Funktion. Wird für den Rücksprung benötigt.
   */
  private int frameCells = 0;
  /*
   * Funktionen, die bereits assembliert wurden. Wir merken uns alle Funktionen, sodass wir am Ende
   * die Call-Sites patchen können.
   */
  private HashMap<String, FunctionDesc> functions = new HashMap<>();
  /*
   * Call-Sites; Funktionsaufrufe müssen ganz am Ende generiert werden, nämlich dann, wenn die
   * Funktionen schon bekannte Adressen haben.
   */
  private ArrayList<PatchLocation> patchLocations = new ArrayList<>();

  private Map<String, ClassDesc> classDesc = new HashMap<>();
  private Map<String, Class> classes = new HashMap<>();

  private String className;
  // name -> object index
  private Map<String, Integer> heapVars = new HashMap<>();
  // reference name -> Type
  private Map<String, Type> types = new HashMap<>();


  public int[] getProgram() {
    // Umwandlung von Integer[] nach int[]
    int[] intProgram = new int[instructions.size()];
    for (int i = 0; i < intProgram.length; i++) {
      intProgram[i] = instructions.get(i);
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
    if (heapVars.containsKey(variable.getName())) {
      int index = heapVars.get(variable.getName());
      add(LDI.encode(index));
      add(LDS.encode(locals.get("$obj")));
      add(LDH.encode());
      return;
    }
    if (locals.containsKey(variable.getName())) {
      int variableLocation = locals.get(variable.getName());
      add(LDS.encode(variableLocation));
      return;
    }
    if (variable.getName().equals("this")) {
      add(LDS.encode(locals.get("$obj")));
      return;
    }
    throw new RuntimeException("Unknown variable '" + variable.getName() + "'");
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
    // Padding for the tail call optimization
    for (int i = 0; i < call.getArguments().length; i++) {
      add(NOP.encode());
    }
    add(CALL.encode(call.getArguments().length));
  }

  @Override
  public void visit(ArrayAccess arrayAccess) {
    arrayAccess.getIndex().accept(this);
    // Wir addieren 1 auf den Index, da an Offset 0 die Länge des Arrays gespeichert ist.
    add(LDI.encode(1));
    add(ADD.encode());
    arrayAccess.getArray().accept(this);
    add(LDH.encode());
  }

  @Override
  public void visit(ArrayInitializer arrayInitializer) {
    arrayInitializer.getSize().accept(this);
    // Kopie der Länge in t0 speichern
    add(STS.encode(locals.get("$t0")));
    add(LDS.encode(locals.get("$t0")));
    // Länge für Heap-Allokation um 1 erhöhen (Platz für Länge)
    add(LDI.encode(1));
    add(ADD.encode());
    // Heap-Allokation durchführen
    add(ALLOCH.encode());
    // Kopie der Heap-Referenz in t1 speichern
    add(STS.encode(locals.get("$t1")));

    // Array Initialisieren (heißt das length-Feld an Offset 0 setzen)
    // Länge laden
    add(LDS.encode(locals.get("$t0")));
    // Offset 0 laden
    add(LDI.encode(0));
    // Heap-Referenz laden
    add(LDS.encode(locals.get("$t1")));
    // Länge an Offset 0 von Array schreiben
    add(STH.encode());

    // Heap-Referenz laden (als Ergebnis auf dem Stack)
    add(LDS.encode(locals.get("$t1")));
  }

  @Override
  public void visit(ArrayLength arrayLength) {
    // Die Länge des Arrays ist im Heap an Offset 0 gespeichert
    add(LDI.encode(0));
    arrayLength.getArray().accept(this);
    add(LDH.encode());
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
    if (heapVars.containsKey(assignment.getName())) {
      int index = heapVars.get(assignment.getName());
      add(LDI.encode(index));
      add(LDS.encode(locals.get("$obj")));
      add(STH.encode());
      return;
    }
    Integer location = locals.get(assignment.getName());
    if (location == null) {
      throw new RuntimeException("Unknown variable " + assignment.getName());
    }
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

  @Override
  public void visit(ArrayAssignment arrayAssignment) {
    arrayAssignment.getRhs().accept(this);
    arrayAssignment.getIndex().accept(this);
    // Wir addieren 1 auf den Index, da an Offset 0 die Länge des Arrays gespeichert ist.
    add(LDI.encode(1));
    add(ADD.encode());

    String name = arrayAssignment.getName();
    if (heapVars.containsKey(name)) {
      add(LDI.encode(heapVars.get(name)));
      add(LDS.encode(locals.get("$obj")));
      add(LDH.encode());
    } else if (locals.containsKey(name)) {
      add(LDS.encode(locals.get(name)));
    } else {
      throw new RuntimeException("unknown variable " + name);
    }
    add(STH.encode());
  }


  /*
   * Condition
   */

  @Override
  public void visit(True true_) {
    add(LDI.encode(0));
    add(NOT.encode());
  }

  @Override
  public void visit(False false_) {
    add(LDI.encode(0));
  }

  @Override
  public void visit(Comparison comparison) {
    comparison.getRhs().accept(this);
    comparison.getLhs().accept(this);
    switch (comparison.getOperator()) {
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
  private void visitDeclarations(Type type, String[] names) {
    int offset = locals.size() + 1;
    for (int i = 0; i < names.length; i++) {
      if (locals.containsKey(names[i])) {
        throw new RuntimeException("Variable '" + names[i] + "' is already defined");
      }
      locals.put(names[i], offset + i);
    }

    for (String name : names) {
      types.put(name, type);
    }
    // Beachte: Mehrere ALLOCs am Funktionsanfang
    // hintereinander sind ok
    add(ALLOC.encode(names.length));
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
    locals.clear();
    types.clear();
    // Wir müssen Platz für Generator-private Variablen schaffen (vgl. Code
    // zur Initialisierung von Arrays)
    locals.put("$t0", locals.size() + 1);
    locals.put("$t1", locals.size() + 1);
    locals.put("$trash", locals.size() + 1);
    int declarations = 3;
    add(ALLOC.encode(declarations));

    for (Declaration d : function.getDeclarations()) {
      declarations += d.getNames().length;
      d.accept(this);
    }
    SingleDeclaration[] parameters = function.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (locals.containsKey(parameters[i].getName()) ||
          heapVars.containsKey(parameters[i].getName())) {
        throw new RuntimeException("Variable '" + parameters[i].getName() + "' is already defined");
      }
      locals.put(parameters[i].getName(), -parameters.length + i + 1);
      types.put(parameters[i].getName(), parameters[i].getType());
    }
    frameCells = parameters.length + declarations;
    for (Statement s : function.getStatements()) {
      s.accept(this);
    }
    types.clear();
    locals.clear();
  }

  public void methodCall(Function function) {
    locals.clear();
    types.clear();
    // Wir müssen Platz für Generator-private Variablen schaffen (vgl. Code
    // zur Initialisierung von Arrays)
    locals.put("$t0", locals.size() + 1);
    locals.put("$t1", locals.size() + 1);
    locals.put("$trash", locals.size() + 1);
    int declarations = 3;
    add(ALLOC.encode(declarations));
    for (Declaration d : function.getDeclarations()) {
      declarations += d.getNames().length;
      d.accept(this);
    }
    locals.put("$obj", 0);
    SingleDeclaration[] parameters = function.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (locals.containsKey(parameters[i].getName()) ||
          heapVars.containsKey(parameters[i].getName())) {
        throw new RuntimeException("Variable '" + parameters[i].getName() + "' is already defined");
      }
      locals.put(parameters[i].getName(), -parameters.length + i);
      types.put(parameters[i].getName(), parameters[i].getType());
    }
    // +1 for $obj arg
    frameCells = parameters.length + declarations + 1;
    for (Statement s : function.getStatements()) {
      s.accept(this);
    }
    types.clear();
    locals.clear();
  }

  @Override
  public void visit(Program program) {
    // Das Programm beginnt mit einem Sprung zur Hauptfunktion
    int ldiMainAddress = addDummy();
    add(CALL.encode(0));
    add(HALT.encode());

    for (Class clazz : program.getClasses()) {
      classes.put(clazz.getName(), clazz);
    }
    for (Class clazz : program.getClasses()) {
      clazz.accept(this);
    }

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
      f.accept(this);
    }
    if (!hasMain) {
      throw new RuntimeException("Main function is missing.");
    }
    // Nachdem alle Funktionen assembliert wurden, müssen wir die Instruktionen patchen, die
    // Funktionsadressen laden.
    for (PatchLocation pl : patchLocations) {
      pl.patch();
    }
  }

  @Override
  public void visit(Class clazz) {
    locals.clear();
    if (classDesc.containsKey(clazz.getName())) {
      return;
    }
    String sName = clazz.getSuperClass();
    if (sName != null && !classDesc.containsKey(sName)) {
      if (!classes.containsKey(sName)) {
        throw new RuntimeException(
            String.format("Super class %s has not been defined", sName));
      }
      classes.get(sName).accept(this);
    }
    className = clazz.getName();
    // generate $alloc_Class
    int allocStart = instructions.size();
    ClassDesc c = new ClassDesc(allocStart, clazz.getSuperClass());
    classDesc.put(clazz.getName(), c);
    // allocation specific variables
    locals.put("$vt", locals.size() + 1);
    locals.put("$obj", locals.size() + 1);
    locals.put("$trash", locals.size() + 1);
    int localVariables = 3;
    add(ALLOC.encode(localVariables));
    List<String> vTable = allocatevTable(clazz);
    Map<String, Integer> fields = allocateFields(clazz);
    // return the obj ref
    add(LDS.encode(locals.get("$obj")));
    add(RETURN.encode(localVariables));
    locals.clear();
    ClassDesc cDesc = classDesc.get(clazz.getName());
    cDesc.setvTable(vTable);
    cDesc.setHeapVars(fields);

    generateMethods(clazz);
    generateConstructor(clazz);
  }

  private void generateMethods(Class clazz) {
    ClassDesc cDesc = classDesc.get(clazz.getName());
    heapVars = cDesc.getHeapVars();
    Map<String, Integer> methodPointers = cDesc.getMethodPointers();
    for (Function method : clazz.getFunctions()) {
      cDesc.getArgumentCounts().put(method.getName(), method.getParameters().length);
      int methodStart = instructions.size();
      methodPointers.put(method.getName(), methodStart);
      methodCall(method);
    }
    heapVars.clear();
  }

  private void generateConstructor(Class clazz) {
    int constructorStart = instructions.size();
    classDesc.get(clazz.getName()).setConstructorIndex(constructorStart);
    heapVars = classDesc.get(clazz.getName()).getHeapVars();
    clazz.getConstructor().accept(this);
    className = null;
    heapVars.clear();
    locals.clear();
  }

  private List<String> allocatevTable(Class initializer) {
    LinkedList<Function> methods = new LinkedList<>();
    collectMethods(initializer.getName(), methods);
    List<String> vTable = new ArrayList<>();

    for (Function method : methods) {
      vTable.add(method.getName());
    }
    // generate vTable
    add(LDI.encode(methods.size()));
    add(ALLOCH.encode());
    // save vTable in $vt
    add(STS.encode(locals.get("$vt")));
    return vTable;
  }

  private Map<String, Integer> allocateFields(Class initializer) {
    LinkedList<SingleDeclaration> fields = new LinkedList<>();
    collectFields(initializer.getName(), fields);

    Map<String, Integer> heapVars = new HashMap<>();
    for (int i = 0; i < fields.size(); i++) {
      heapVars.put(fields.get(i).getName(), i + 1);
    }

    // +1 for vTable ref
    add(LDI.encode(fields.size() + 1));
    add(ALLOCH.encode());
    add(STS.encode(locals.get("$obj")));
    // save vTable ref at Index 0
    add(LDS.encode(locals.get("$vt")));
    add(LDI.encode(0));
    add(LDS.encode(locals.get("$obj")));
    add(STH.encode());
    return heapVars;
  }

  private void collectMethods(String className, LinkedList<Function> methods) {
    if (className == null || !classes.containsKey(className)) {
      return;
    }
    Class clazz = classes.get(className);
    collectMethods(clazz.getSuperClass(), methods);
    for (Function f : clazz.getFunctions()) {
      int index = -1;
      for (int i = 0; i < methods.size(); i++) {
        if (methods.get(i).getName().equals(f.getName())) {
          index = i;
          break;
        }
      }
      if (index == -1) {
        methods.add(f);
      } else {
        methods.set(index, f);
      }
    }
  }

  private void collectFields(String className, LinkedList<SingleDeclaration> fields) {
    if (className == null || !classes.containsKey(className)) {
      return;
    }
    Class clazz = classes.get(className);
    collectFields(clazz.getSuperClass(), fields);
    for (SingleDeclaration s : clazz.getFields()) {
      int index = -1;
      for (int i = 0; i < fields.size(); i++) {
        if (fields.get(i).getName().equals(s.getName())) {
          index = i;
          break;
        }
      }
      if (index == -1) {
        fields.add(s);
      } else {
        fields.set(index, s);
      }
    }
  }

  @Override
  public void visit(Constructor constructor) {
    String name = constructor.getName();
    className = name;
    if (!classDesc.containsKey(name)) {
      throw new RuntimeException(
          String.format("invalid constructor %s", name));
    }
    ClassDesc cDesc = classDesc.get(name);
    cDesc.setConstructorIndex(instructions.size());
    cDesc.getArgumentCounts().put(name, constructor.getParameters().length);
    locals.clear();
    types.clear();

    // allocation specific variables
    locals.put("$vt", locals.size() + 1);
    locals.put("$t0", locals.size() + 1);
    locals.put("$t1", locals.size() + 1);
    locals.put("$trash", locals.size() + 1);
    int localVariables = 4;
    add(ALLOC.encode(localVariables));

    SingleDeclaration[] param = constructor.getParameters();
    for (int i = 0; i < param.length; i++) {
      locals.put(param[i].getName(), -param.length + i);
      types.put(param[i].getName(), param[i].getType());
    }
    // $obj is always the last argument when calling the constructor
    locals.put("$obj", 0);
    localVariables += param.length + 1;
    // save vtable pointer to $vt
    add(LDI.encode(0));
    add(LDS.encode(locals.get("$obj")));
    add(LDH.encode());
    add(STS.encode(locals.get("$vt")));

    int stmtIndex = 0;
    if (cDesc.getSuper() != null) {
      Statement superCall = constructor.getStatements()[0];
      stmtIndex++;
      if (!(superCall instanceof ExpressionStatement)) {
        throw new RuntimeException("Inherited Constructors have to call the"
            + "superclass on the first line");
      }
      superCall.accept(this);
      // +1 for extra obj ref on the stack
      localVariables++;
    }
    // update vtable
    List<String> vTable = cDesc.getvTable();
    Map<String, Integer> methodPointers = cDesc.getMethodPointers();
    for (int i = 0; i < vTable.size(); i++) {
      String method = vTable.get(i);
      if (Arrays.stream(classes.get(name).getFunctions())
          .noneMatch(f -> f.getName().equals(method))) {
        continue;
      }
      int methodPointer = methodPointers.get(method);
      add(LDI.encode(methodPointer));
      add(LDI.encode(i));
      add(LDS.encode(locals.get("$vt")));
      add(STH.encode());
    }
    for (Declaration declaration : constructor.getDeclarations()) {
      for (String s : declaration.getNames()) {
        if (locals.containsKey(s) || heapVars.containsKey(s)) {
          throw new RuntimeException("variable " + s + " already declared");
        }
      }
      declaration.accept(this);
    }
    for (int i = stmtIndex; i < constructor.getStatements().length; i++) {
      constructor.getStatements()[i].accept(this);
    }
    add(LDS.encode(locals.get("$obj")));
    add(RETURN.encode(localVariables));

    locals.clear();
    types.clear();
  }

  private void superConstructorCall(String className, Expression[] arguments) {
    if (!classes.containsKey(className)) {
      throw new RuntimeException(
          String.format("super class constructor %s is undefined", className));
    }
    ClassDesc superCDesc = classDesc.get(className);
    ClassDesc thisC = classDesc.get(this.className);
    superCDesc.getMethodPointers().forEach((k, v) ->
        thisC.getMethodPointers().putIfAbsent(k, v)
    );
    superCDesc.getArgumentCounts().forEach((k, v) ->
        thisC.getArgumentCounts().putIfAbsent(k, v)
    );
    for (Expression argument : arguments) {
      argument.accept(this);
    }
    add(LDS.encode(locals.get("$obj")));
    int patch = addDummy();
    patchLocations.add(new ConstructorPatch(className, arguments.length, patch));
    add(CALL.encode(arguments.length + 1));
  }

  @Override
  public void visit(MethodCall methodCall) {
    String ref = methodCall.getRefName();
    String method = methodCall.getMethodName();
    String clazz;
    // super calls have to be statically bound
    if (ref.equals("super")) {
      // if it's actually a super Constructor call
      if (classes.containsKey(method)) {
        superConstructorCall(method, methodCall.getArguments());
        return;
      }
      clazz = classDesc.get(className).getSuper();
      if (!locals.containsKey("$obj")) {
        throw new RuntimeException("the keyword this can only be used within a class");
      }
      for (Expression argument : methodCall.getArguments()) {
        argument.accept(this);
      }
      add(LDS.encode(locals.get("$obj")));
      int patch = addDummy();
      patchLocations.add(new StaticCall(clazz, method, methodCall.getArguments().length, patch));
    } else {
      int obj;
      if (ref.equals("this")) {
        if (!locals.containsKey("$obj")) {
          throw new RuntimeException("the keyword this can only be used within a class");
        }
        obj = locals.get("$obj");
      } else {
        if (!locals.containsKey(ref) || !types.containsKey(ref)) {
          throw new RuntimeException("Unknown variable " + ref);
        }
        obj = locals.get(ref);
        className = types.get(ref).codeString();
      }
      for (Expression argument : methodCall.getArguments()) {
        argument.accept(this);
      }
      add(LDS.encode(obj));
      // lookup the method in the vtable
      int patch = addDummy();
      patchLocations
          .add(new DynamicCall(className, method, methodCall.getArguments().length, patch));
      add(LDI.encode(0));
      add(LDS.encode(obj));
      add(LDH.encode());
      add(LDH.encode());
    }
    // + 1 for obj ref
    add(CALL.encode(methodCall.getArguments().length + 1));
  }

  @Override
  public void visit(ObjectInitializer initializer) {
    String name = initializer.getClassName();
    if (!classDesc.containsKey(name)) {
      throw new RuntimeException(
          String.format("class %s has not been defined", name));
    }
    ClassDesc clazz = classDesc.get(name);

    for (Expression expression : initializer.getArguments()) {
      expression.accept(this);
    }
    // call to $alloc_Class
    add(LDI.encode(clazz.getAllocIndex()));
    add(CALL.encode(0));
    // Constructor expects:
    // arg0, ..., argN, obj ref
    add(LDI.encode(clazz.getConstructorIndex()));
    add(CALL.encode(initializer.getArguments().length + 1));
  }

  @Override
  public void visit(ExpressionStatement expressionStatement) {
    // all <expr> increase the stack size by one
    expressionStatement.getExpression().accept(this);
    // therefore the return value need to be removed from the stack
    if (expressionStatement.getExpression() instanceof MethodCall) {
      MethodCall m = (MethodCall) expressionStatement.getExpression();
      if (!m.getRefName().equals("super")) {
        add(STS.encode(locals.get("$trash")));
      }
    }
  }


  class PatchLocation {

    public final String functionName;
    public final int argumentCount;
    public final int ldiLocation;

    public PatchLocation(String functionName, int argumentCount, int ldiLocation) {
      this.functionName = functionName;
      this.argumentCount = argumentCount;
      this.ldiLocation = ldiLocation;
    }

    public void patch() {
      FunctionDesc fDesc = functions.get(functionName);
      if (fDesc == null) {
        throw new RuntimeException("Unknown function " + functionName);
      }
      if (fDesc.argumentCount != argumentCount) {
        throw new RuntimeException("Invalid number of function arguments");
      }
      instructions.set(ldiLocation, LDI.encode(fDesc.functionIndex));
    }
  }

  class StaticCall extends PatchLocation {

    public String className;

    public StaticCall(String className, String functionName, int argumentCount, int ldiLocation) {
      super(functionName, argumentCount, ldiLocation);
      this.className = className;
    }

    @Override
    public void patch() {
      ClassDesc cDesc = classDesc.get(className);
      if (cDesc == null || !cDesc.getMethodPointers().containsKey(functionName)) {
        throw new RuntimeException(String.format("Unknown Method %s.%s",
            className, functionName));
      }
      if (!cDesc.getArgumentCounts().containsKey(functionName) ||
          cDesc.getArgumentCounts().get(functionName) != argumentCount) {
        throw new RuntimeException("Invalid number of method arguments for " + functionName);
      }
      int methodIndex = cDesc.getMethodPointers().get(functionName);
      instructions.set(ldiLocation, LDI.encode(methodIndex));
    }
  }

  class DynamicCall extends PatchLocation {

    public String className;

    public DynamicCall(String className, String functionName, int argumentCount, int ldiLocation) {
      super(functionName, argumentCount, ldiLocation);
      this.className = className;
    }

    @Override
    public void patch() {
      ClassDesc cDesc = classDesc.get(className);
      List<String> vTable = cDesc.getvTable();
      int methodIndex = -1;
      for (int i = 0; i < vTable.size(); i++) {
        if (vTable.get(i).equals(functionName)) {
          methodIndex = i;
          break;
        }
      }
      if (methodIndex == -1) {
        throw new RuntimeException(String.format("Unknown Method %s.%s",
            className, functionName));
      }
      if (!cDesc.getArgumentCounts().containsKey(functionName) ||
          cDesc.getArgumentCounts().get(functionName) != argumentCount) {
        throw new RuntimeException("Invalid number of method arguments for " + functionName);
      }
      instructions.set(ldiLocation, LDI.encode(methodIndex));
    }
  }

  class ConstructorPatch extends PatchLocation {

    public String className;

    public ConstructorPatch(String className, int argumentCount, int ldiLocation) {
      super(null, argumentCount, ldiLocation);
      this.className = className;
    }

    @Override
    public void patch() {
      ClassDesc cDesc = classDesc.get(className);
      if (cDesc == null) {
        throw new RuntimeException(String.format("Unknown Class %s", className));
      }
      if (!cDesc.getArgumentCounts().containsKey(className) ||
          cDesc.getArgumentCounts().get(className) != argumentCount) {
        throw new RuntimeException("Invalid number of Constructor arguments");
      }
      int constructorIndex = cDesc.getConstructorIndex();
      instructions.set(ldiLocation, LDI.encode(constructorIndex));
    }
  }
}
