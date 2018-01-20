package aufgabe11_7;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormatTest {

  @Test
  public void testExpression1() {
    Expression exp = new Binary(
        new Binary(new Number(99), Binop.Plus, new Number(11)),
        Binop.Minus,
        new Binary(new Variable("a"), Binop.Plus, new Number(1))
    );
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 + 11 - (a + 1)", visitor.getResult());
  }

  @Test
  public void testExpression2() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)), Binop.Plus,
        new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 + 11 + a + 1", visitor.getResult());
  }

  @Test
  public void testExpression3() {
    Expression exp = new Binary(
        new Binary(new Number(99), Binop.Plus, new Number(11)),
        Binop.Plus,
        new Binary(new Variable("a"), Binop.MultiplicationOperator, new Number(1))
    );
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 + 11 + a * 1", visitor.getResult());
  }


  @Test
  public void testExpression4() {
    Expression exp = new Unary(Unop.Minus, new Binary(new Number(99), Binop.Plus, new Number(11)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("-(99 + 11)", visitor.getResult());
  }

  @Test
  public void testExpression5() {
    Expression exp = new Unary(Unop.Minus,
        new Binary(new Number(99), Binop.MultiplicationOperator, new Number(11))
    );
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("-99 * 11", visitor.getResult());
  }

  @Test
  public void testExpression6() {
    Expression exp = new ArrayAccess(new ArrayInitializer(new Number(99)), new Number(3));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("(new int[99])[3]", visitor.getResult());
  }

  @Test
  public void testExpression7() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.DivisionOperator, new Number(11)),
        Binop.DivisionOperator,
        new Binary(new Variable("a"), Binop.DivisionOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 / 11 / (a / 1)", visitor.getResult());
  }

  @Test
  public void testExpression8() {
    Expression exp = new Binary(
        new Binary(new Number(99), Binop.DivisionOperator,
            new Call("hugo", new Expression[]{new Number(1), new Variable("b")})),
        Binop.DivisionOperator,
        new Binary(new Variable("a"), Binop.DivisionOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 / hugo(1, b) / (a / 1)", visitor.getResult());
  }

  @Test
  public void testExpression9() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)),
        Binop.MultiplicationOperator, new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("(99 + 11) * (a + 1)", visitor.getResult());
  }

  @Test
  public void testExpression10() {
    Expression exp =
        new Binary(new Binary(new Number(99), Binop.MultiplicationOperator, new Number(11)),
            Binop.MultiplicationOperator, new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 * 11 * (a + 1)", visitor.getResult());
  }

  @Test
  public void testExpression11() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Minus, new Number(11)),
        Binop.MultiplicationOperator,
        new Binary(new Variable("a"), Binop.MultiplicationOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("(99 - 11) * a * 1", visitor.getResult());
  }

  @Test
  public void testCondition1() {
    Condition cond = new BinaryCondition(new True(), Bbinop.And,
        new BinaryCondition(new False(), Bbinop.And, new True()));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("true && false && true", visitor.getResult());
  }

  @Test
  public void testCondition2() {
    Condition cond = new BinaryCondition(new True(), Bbinop.And,
        new BinaryCondition(new False(), Bbinop.Or, new True()));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("true && (false || true)", visitor.getResult());
  }

  @Test
  public void testCondition3() {
    Condition cond =
        new BinaryCondition(new Comparison(new Number(2), Comp.Greater, new Variable("a")),
            Bbinop.Or, new BinaryCondition(new False(), Bbinop.And, new True()));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("2 > a || false && true", visitor.getResult());
  }

  @Test
  public void testCondition4() {
    Condition cond =
        new UnaryCondition(Bunop.Not, new Comparison(new Number(1), Comp.Equals, new Number(2)));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("!(1 == 2)", visitor.getResult());
  }

  @Test
  public void testCondition5() {
    Condition cond = new UnaryCondition(Bunop.Not,
        new BinaryCondition(new Comparison(new Number(2), Comp.Greater, new Variable("a")),
            Bbinop.Or, new BinaryCondition(new False(), Bbinop.And, new True())));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("!(2 > a || false && true)", visitor.getResult());
  }

//  @Test
//  public void testGGT() {
//    int a = 10, b = 20;
//    Statement ggtSwap =
//        new IfThen(new Comparison(new Variable("b"), Comp.Greater, new Variable("a")),
//            new Composite(new Statement[]{new Assignment("temp", new Variable("a")),
//                new Assignment("a", new Variable("b")),
//                new Assignment("b", new Variable("temp")),}));
//    Statement ggtWhile = new While(new Comparison(new Variable("b"), Comp.NotEquals, new Number(0)),
//        new Composite(new Statement[]{new Assignment("temp", new Variable("b")),
//            new Assignment("b", new Binary(new Variable("a"), Binop.Modulo, new Variable("b"))),
//            new Assignment("a", new Variable("temp"))}));
//    Function ggt = new Function("ggt", new String[]{"a", "b"},
//        new Declaration[]{new Declaration(Type.Integer, new String[]{"temp"})},
//        new Statement[]{ggtSwap, ggtWhile, new Return(new Variable("a"))});
//    Function mainFunctionGgt =
//        new Function("main", new String[]{}, new Type[]{}, new Declaration[]{}, new Statement[]{
//            new Return(new Call("ggt", new Expression[]{new Number(a), new Number(b)}))});
//    Program ggtProgram = new Program(new Function[]{ggt, mainFunctionGgt});
//    FormatVisitor f = new FormatVisitor();
//    ggtProgram.accept(f);
//    String res = f.getResult();
//    System.out.println(res);
//    assertEquals("int ggt(int a, int b) {\n"
//        + "  int temp;\n"
//        + "  if (b > a)\n"
//        + "  {\n"
//        + "    temp = a;\n"
//        + "    a = b;\n"
//        + "    b = temp;\n"
//        + "  }\n"
//        + "  while (b != 0)\n"
//        + "  {\n"
//        + "    temp = b;\n"
//        + "    b = a % b;\n"
//        + "    a = temp;\n"
//        + "  }\n"
//        + "  return a;\n"
//        + "}\n"
//        + "\n"
//        + "int main() {\n"
//        + "  return ggt(10, 20);\n"
//        + "}\n", res);
//  }
//
//  @Test
//  public void testFak() {
//    int n = 20;
//    Statement fakRecEnd = new IfThen(new Comparison(new Variable("n"), Comp.Equals, new Number(0)),
//        new Return(new Number(1)));
//    Statement fakRec =
//        new Return(new Binary(new Variable("n"), Binop.MultiplicationOperator, new Call("fak",
//            new Expression[]{new Binary(new Variable("n"), Binop.Minus, new Number(1))})));
//    Function fakFunc = new Function("fak", new String[]{"n"}, new Type[]{}, new Declaration[]{},
//        new Statement[]{fakRecEnd, fakRec});
//    Function mainFunctionFak = new Function("main", new String[]{}, new Type[]{},
//        new Declaration[]{},
//        new Statement[]{new Return(new Call("fak", new Expression[]{new Number(n)}))});
//    Program fakProgram = new Program(new Function[]{mainFunctionFak, fakFunc});
//    FormatVisitor f = new FormatVisitor();
//    f.visit(fakProgram);
//    String res = f.getResult();
//    System.out.println(res);
//    assertEquals("int main() {\n"
//        + "  return fak(20);\n"
//        + "}\n"
//        + "\n"
//        + "int fak(int n) {\n"
//        + "  if (n == 0)\n"
//        + "    return 1;\n"
//        + "  return n * fak(n - 1);\n"
//        + "}\n", res);
//  }
}
