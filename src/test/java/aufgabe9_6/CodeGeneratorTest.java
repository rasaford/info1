package aufgabe9_6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
    testProgram(getFakProgram(3), 6);
    testProgram(getFakProgram(10), 3628800);
  }

  public void testProgram(Program program, int expected) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    program.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expected, retVal);
  }

  @Test
  public void testExpression() {
    Program p = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Unary(Unop.Minus, new Number(10))
            )})
    });
    testProgram(p, -10);
    Program p2 = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Binary(new Number(13), Binop.Plus, new Number(10))
            )})
    });
    testProgram(p2, 23);
    Program p3 = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Binary(new Number(13), Binop.MultiplicationOperator, new Number(10))
            )})
    });
    testProgram(p3, 130);
    Program p4 = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Binary(
                    new Binary(new Number(1), Binop.Plus, new Number(2)),
                    Binop.Plus,
                    new Binary(new Number(1), Binop.Plus, new Number(2)))
            )})
    });
    testProgram(p4, 6);
  }

  @Test
  public void testCondition() {
    Program p1 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new BinaryCondition(new True(), Bbinop.And, new False()),
                new Return(new Number(-1)),
                new Return(new Number(0))
            )})
    });
    testProgram(p1, 0);
    Program p2 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new BinaryCondition(new True(), Bbinop.Or, new False()),
                new Return(new Number(-1)),
                new Return(new Number(0))
            )})
    });
    testProgram(p2, -1);
    Program p3 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new UnaryCondition(Bunop.Not, new False()),
                new Return(new Number(-1)),
                new Return(new Number(0))
            )})
    });
    testProgram(p3, -1);
    Program p4 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new Comparison(new Number(5), Comp.Less, new Number(10)),
                new Return(new Number(-1)),
                new Return(new Number(0))
            )})
    });
    testProgram(p4, -1);
    Program p5 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new Comparison(new Number(5), Comp.Greater, new Number(10)),
                new Return(new Number(-1)),
                new Return(new Number(0))
            )})
    });
    testProgram(p5, 0);
  }

  @Test
  public void testIfThen() {
    Function f = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{
            new IfThen(
                new True(),
                new Assignment("testProgram", new Number(12))
            ),
            new Return(new Variable("testProgram"))
        });
    testProgram(new Program(new Function[]{f}), 12);
  }

  @Test
  public void testIfThenElse() {
    Function f2 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{
            new IfThenElse(
                new True(),
                new Assignment("testProgram", new Number(12)),
                new Assignment("testProgram", new Number(20))
            ),
            new Return(new Variable("testProgram"))
        });
    Function f3 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{
            new IfThenElse(
                new False(),
                new Assignment("testProgram", new Number(12)),
                new Assignment("testProgram", new Number(20))
            ),
            new Return(new Variable("testProgram"))
        });
    testProgram(new Program(new Function[]{f2}), 12);
    testProgram(new Program(new Function[]{f3}), 20);

  }

  @Test
  public void testVariables() {
    Function f = new Function("main", new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{new Assignment("testProgram", new Number(123)),
            new Return(new Variable("testProgram"))});
    Program p = new Program(new Function[]{f});
    testProgram(p, 123);
  }

  @Test
  public void testWhile() {
    Function f = new Function("main", new String[0],
        new Declaration[]{new Declaration(new String[]{"a", "b"})},
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Binary(new Variable("b"), Binop.Plus, new Number(5))),
                    new Assignment("a", new Binary(new Variable("a"), Binop.Minus, new Number(1)))
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }

  @Test(expected = RuntimeException.class)
  public void testVariableNotDeclared() {
    Function f = new Function("main", new String[0],
        new Declaration[0],
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Binary(new Variable("b"), Binop.Plus, new Number(5))),
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }

  @Test(expected = RuntimeException.class)
  public void testFunctionNotDeclared() {
    Function f = new Function("main", new String[0],
        new Declaration[0],
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Call("undefined", new Expression[0])),
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }

  @Test(expected = RuntimeException.class)
  public void testNoMainFunction() {
    Function f = new Function("testProgram", new String[0],
        new Declaration[0],
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Number(10)),
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }
}
