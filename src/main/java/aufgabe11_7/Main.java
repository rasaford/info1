package aufgabe11_7;

public class Main {

  public static void main(String[] args) {
    Statement ggtSwap =
    new IfThen(new Comparison(new Variable("b"), Comp.Greater, new Variable("a")),
    new Composite(new Statement[] {new Assignment("temp", new Variable("a")),
    new Assignment("a", new Variable("b")),
    new Assignment("b", new Variable("temp")),}));
    Statement ggtWhile =
    new While(new Comparison(new Variable("b"), Comp.NotEquals, new Number(0)),
    new Composite(new Statement[] {new Assignment("temp", new Variable("b")),
    new Assignment("b",
    new Binary(new Variable("a"), Binop.Modulo, new Variable("b"))),
    new Assignment("a", new Variable("temp"))}));
    Function ggt =
    new Function("ggt", new String[] {"a", "b"}, new Declaration[] {new Declaration("temp")},
    new Statement[] {ggtSwap, ggtWhile, new Return(new Variable("a"))});
    Function mainFunctionGgt = new Function("main", new String[] {}, new Declaration[] {},
    new Statement[] {new Return(new Call("ggt", new Expression[] { new Number(3780), new Number(3528)}))});
//    new Statement[] {new Return(new Call("ggt", new Expression[] { new Number(378000), new Number(3528)}))});
    Program ggtProgram = new Program(new Function[] {ggt, mainFunctionGgt});

    Statement fakRecEnd =
        new IfThen(new Comparison(new Variable("n"), Comp.Equals, new Number(0)),
            new Return(new Number(1)));
    Statement fakRec = new Return(new Binary(new Variable("n"), Binop.MultiplicationOperator,
        new Call("fak", new Expression[] {new Binary(new Variable("n"), Binop.Minus, new Number(1))})));
    Function fakFunc = new Function("fak", new String[] { "n" }, new Declaration[] {}, new Statement[] { fakRecEnd, fakRec });
     Function mainFunctionFak = new Function("main", new String[] {}, new Declaration[] {},
     new Statement[] { new Return(new Call("fak", new Expression[] { new Number(6)}))});
     Program fakProgram = new Program(new Function[] {mainFunctionFak, fakFunc});

    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    ggtProgram.accept(cgv);
    System.out.println(Interpreter.programToString(cgv.getProgram()));

    System.out.println("Executing...");
    int retVal = Interpreter.execute(cgv.getProgram());
    System.out.println("Return value: " + retVal);
  }

}
