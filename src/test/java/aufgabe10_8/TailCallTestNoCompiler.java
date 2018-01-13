package aufgabe10_8;

//import static org.junit.Assert.assertEquals;
//import org.junit.Test;
//import codegen.Binary;
//import codegen.Binop;
//import codegen.Call;
//import codegen.CodeGenerationVisitor;
//import codegen.Comp;
//import codegen.Comparison;
//import codegen.Declaration;
//import codegen.Expression;
//import codegen.Function;
//import codegen.IfThen;
//import codegen.Number;
//import codegen.Program;
//import codegen.Return;
//import codegen.Statement;
//import codegen.Variable;
//import interpreter.Interpreter;

public class TailCallTestNoCompiler {
  // Stack-Größe auf 32 anpassen!

//  private static Program getTailRecFakProgram(int n) {
//    Statement fakRecEnd = new IfThen(new Comparison(new Variable("n"), Comp.Equals, new Number(0)),
//        new Return(new Variable("acc")));
//    Statement fakRec = new Return(
//        new Call("fak", new Expression[] {new Binary(new Variable("n"), Binop.Minus, new Number(1)),
//            new Binary(new Variable("acc"), Binop.MultiplicationOperator, new Variable("n"))}));
//    Function fakFunc = new Function("fak", new String[] {"n", "acc"}, new Declaration[] {},
//        new Statement[] {fakRecEnd, fakRec});
//    Function mainFunctionFak = new Function("main", new String[] {}, new Declaration[] {},
//        new Statement[] {new Return(new Call("fak", new Expression[] {new Number(n), new Number(1)}))});
//    Program fakProgram = new Program(new Function[] {mainFunctionFak, fakFunc});
//    return fakProgram;
//  }
//
//  private static Program getNotTailRecFakProgram(int n) {
//    Statement fakRecEnd = new IfThen(new Comparison(new Variable("n"), Comp.Equals, new Number(0)),
//        new Return(new Number(1)));
//    Statement fakRec =
//        new Return(new Binary(new Variable("n"), Binop.MultiplicationOperator, new Call("fak",
//            new Expression[] {new Binary(new Variable("n"), Binop.Minus, new Number(1))})));
//    Function fakFunc = new Function("fak", new String[] {"n"}, new Declaration[] {},
//        new Statement[] {fakRecEnd, fakRec});
//    Function mainFunctionFak = new Function("main", new String[] {}, new Declaration[] {},
//        new Statement[] {new Return(new Call("fak", new Expression[] {new Number(n)}))});
//    Program fakProgram = new Program(new Function[] {mainFunctionFak, fakFunc});
//    return fakProgram;
//  }
//
//
//  @Test
//  public void testFakEndRecOptimized() {
//    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
//    getTailRecFakProgram(12).accept(cgv);
//    int[] assembly = cgv.getProgram();
//    TailCallOptimization.optimize(assembly);
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void testFakEndRecNoOptimization() {
//    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
//    getTailRecFakProgram(12).accept(cgv);
//    int[] assembly = cgv.getProgram();
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void testFakOptimized() {
//    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
//    getNotTailRecFakProgram(12).accept(cgv);
//    int[] assembly = cgv.getProgram();
//    TailCallOptimization.optimize(assembly);
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void testFakNoOptimization() {
//    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
//    getNotTailRecFakProgram(12).accept(cgv);
//    int[] assembly = cgv.getProgram();
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
}
