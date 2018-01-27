package aufgabe12_9;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormatTest {

  @Test
  public void testExpression1() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)), Binop.Minus,
        new Binary(new Variable("a"), Binop.Plus, new Number(1)));
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
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)), Binop.Plus,
        new Binary(new Variable("a"), Binop.MultiplicationOperator, new Number(1)));
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
        new Binary(new Number(99), Binop.MultiplicationOperator, new Number(11)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("-(99 * 11)", visitor.getResult());
  }

  @Test
  public void testExpression5P5() {
    Expression exp = new Unary(Unop.Minus, new Unary(Unop.Minus,
        new Binary(new Number(99), Binop.MultiplicationOperator, new Number(11))));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("-(-(99 * 11))", visitor.getResult());
  }

  @Test
  public void testExpression6() {
    Expression exp = new ArrayAccess(new ArrayInitializer(new Int(),new Number(99)), new Number(3));
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
}
