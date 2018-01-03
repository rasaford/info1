package aufgabe9_6;

import org.junit.Test;


import static org.junit.Assert.*;

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


  @Test
  public void testGGT() {
    test(getGgtProgram(12, 18), 6);
    test(getGgtProgram(16, 175), 1);
    test(getGgtProgram(144, 160), 16);
    test(getGgtProgram(3780, 3528), 252);
    test(getGgtProgram(3528, 3780), 252);
  }

  @Test
  public void testGGT32Bit() {
    test(getGgtProgram(378000, 3528), 504);
    test(getGgtProgram(3528, 378000), 504);
  }

  @Test
  public void testFak() {
    test(getFakProgram(3), 6);
    test(getFakProgram(10), 3628800);
  }

  public void test(Program e, int expected) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    e.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expected, retVal);
  }

  public void test(Expression e, int expected) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    Program p = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(e)}),
    });
    p.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expected, retVal);

  }

  public void test(Condition condition, int expected) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    Program p = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                condition,
                new Return(new Number(-1)),
                new Return(new Number(0))
            )})
    });
    p.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expected, retVal);
  }

  public void test(Statement e, int expected) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    e.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expected, retVal);
  }


  @Test
  public void testExpression() {
    Expression e1 = new Unary(Unop.Minus, new Number(10));
    test(e1, -10);
    Expression e2 = new Binary(new Number(13), Binop.Plus, new Number(10));
    test(e2, 23);
    Expression e3 = new Binary(new Number(13), Binop.MultiplicationOperator, new Number(10));
    test(e3, 130);
    Expression e4 = new Binary(
        new Binary(new Number(1), Binop.Plus, new Number(2)),
        Binop.Plus,
        new Binary(new Number(1), Binop.Plus, new Number(2)));
    test(e4, 6);
  }

  @Test
  public void testCondition() {
    Condition c1 = new BinaryCondition(new True(), Bbinop.And, new False());
    test(c1, 0);
    Condition c2 = new BinaryCondition(new True(), Bbinop.Or, new False());
    test(c2, -1);
    Condition c3 = new UnaryCondition(Bunop.Not, new False());
    test(c3, -1);
    Condition c4 = new Comparison(new Number(5), Comp.Less, new Number(10));
    test(c4, -1);
    Condition c5 = new Comparison(new Number(5), Comp.Greater, new Number(10));
    test(c5, 0);
  }

  @Test
  public void testIfThen() {
    Function f = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"test"})},
        new Statement[]{
            new IfThen(
                new True(),
                new Assignment("test", new Number(12))
            ),
            new Return(new Variable("test"))
        });
    Function f2 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"test"})},
        new Statement[]{
            new IfThenElse(
                new True(),
                new Assignment("test", new Number(12)),
                new Assignment("test", new Number(20))
            ),
            new Return(new Variable("test"))
        });
    Function f3 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"test"})},
        new Statement[]{
            new IfThenElse(
                new False(),
                new Assignment("test", new Number(12)),
                new Assignment("test", new Number(20))
            ),
            new Return(new Variable("test"))
        });
    test(new Program(new Function[]{f}), 12);
  }

  @Test
  public void testIfThenElse() {
    Function f2 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"test"})},
        new Statement[]{
            new IfThenElse(
                new True(),
                new Assignment("test", new Number(12)),
                new Assignment("test", new Number(20))
            ),
            new Return(new Variable("test"))
        });
    Function f3 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"test"})},
        new Statement[]{
            new IfThenElse(
                new False(),
                new Assignment("test", new Number(12)),
                new Assignment("test", new Number(20))
            ),
            new Return(new Variable("test"))
        });
    test(new Program(new Function[]{f2}), 12);
    test(new Program(new Function[]{f3}), 20);

  }

  @Test
  public void testVariables() {
    Function f = new Function("main", new String[0],
        new Declaration[]{new Declaration(new String[]{"test"})},
        new Statement[]{new Assignment("test", new Number(123)),
            new Return(new Variable("test"))});
    Program p = new Program(new Function[]{f});
    test(p, 123);
  }
}
