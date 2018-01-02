package aufgabe9_6;


public class CodeGeneratorTest {

  private static Program getGgtProgram(int a, int b) {
    Statement ggtSwap;
    ggtSwap = new IfThen(new Comparison(new Variable("b"), Comp.Greater, new Variable("a")),
        new Composite(new Statement[]{new Assignment("temp", new Variable("a")),
            new Assignment("a", new Variable("b")),
            new Assignment("b", new Variable("temp")),}));
    Statement ggtWhile = new While(new Comparison(new Variable("b"), Comp.NotEquals, new Number(0)),
        new Composite(new Statement[]{new Assignment("temp", new Variable("b")),
            new Assignment("b", new Binary(new Variable("a"), Binop.Modulo, new Variable("b"))),
            new Assignment("a", new Variable("temp"))}));
    Function ggt = new Function("ggt", new String[]{"a", "b"},
        new Declaration[]{new Declaration(new String[]{"temp"})},
        new Statement[]{ggtSwap, ggtWhile, new Return(new Variable("a"))});
    Function mainFunctionGgt =

        new Function("main", new String[]{}, new Declaration[]{}, new Statement[]{
            new Return(new Call("ggt", new Expression[]{new Number(a), new Number(b)}))});
    Program ggtProgram = new Program(new Function[]{ggt, mainFunctionGgt});
    return ggtProgram;
  }

  private static Program getFakProgram(int n) {
    Statement fakRecEnd = new IfThen(new Comparison(new Variable("n"), Comp.Equals, new Number(0)),
        new Return(new Number(1)));
    Statement fakRec =
        new Return(new Binary(new Variable("n"), Binop.MultiplicationOperator, new Call("fak",
            new Expression[]{new Binary(new Variable("n"), Binop.Minus, new Number(1))})));
    Function fakFunc = new Function("fak", new String[]{"n"}, new Declaration[]{},
        new Statement[]{fakRecEnd, fakRec});
    Function mainFunctionFak = new Function("main", new String[]{}, new Declaration[]{},
        new Statement[]{new Return(new Call("fak", new Expression[]{new Number(n)}))});
    Program fakProgram = new Program(new Function[]{mainFunctionFak, fakFunc});
    return fakProgram;
  }

  public void testProgram(Program program, int expectedRetVal) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    program.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expectedRetVal, retVal);
  }

  @Test
  public void testGGT() {
    testProgram(getGgtProgram(12, 18), 6);
    testProgram(getGgtProgram(16, 175), 1);
    testProgram(getGgtProgram(144, 160), 16);
    testProgram(getGgtProgram(3780, 3528), 252);
    testProgram(getGgtProgram(3528, 3780), 252);
  }

  @Test
  public void testGGT32Bit() {
    testProgram(getGgtProgram(378000, 3528), 504);
    testProgram(getGgtProgram(3528, 378000), 504);
  }

  @Test
  public void testFak() {
    testProgram(getFakProgram(3), 6);
    testProgram(getFakProgram(10), 3628800);
  }
}
