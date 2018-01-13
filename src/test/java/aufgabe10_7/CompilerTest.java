package aufgabe10_7;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompilerTest {

  @Test
  public void testGGT() {
    String ggtCode = "int ggt(int a, int b) {\n" +
        "  int temp;\n" +
        "  if(b > a) {\n" +
        "    temp = a;\n" +
        "    a = b;\n" +
        "    b = temp;\n" +
        "  }\n" +
        "  while(a != 0) {\n" +
        "   temp = a;\n" +
        "   a = a % b;\n" +
        "   b = temp;\n" +
        "  }\n" +
        "  return b;\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int a, b, r;\n" +
        "  a = 3528;\n" +
        "  b = 3780;\n" +
        "  r = ggt(a, b);\n" +
        "  return r;\n" +
        "}";
    int[] assembly = Compiler.compile(ggtCode);
    int retVal = Interpreter.execute(assembly);
    assertEquals(252, retVal);
  }

  @Test
  public void testFak() {
    String fakCode = "int fak(int n) {\n" +
        "  if(n == 0) \n" +
        "    return 1;\n" +
        "  return n*fak(n - 1);\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  return fak(6);\n" +
        "}\n";
    int[] assembly = Compiler.compile(fakCode);
    int retVal = Interpreter.execute(assembly);
    assertEquals(720, retVal);
  }

  @Test
  public void testPrim() {
    String primTestCode = "int prim(int n) {\n" +
        "  int divisors, i;\n" +
        "  divisors = 0;\n" +
        "  \n" +
        "  i = 2;\n" +
        "  while (i < n) {\n" +
        "    if (n % i == 0)\n" +
        "      divisors = divisors + 1;\n" +
        "    i = i + 1;\n" +
        "  }\n" +
        "  \n" +
        "  if (divisors == 0 && n >= 2) {\n" +
        "    return 1;\n" +
        "  } else {\n" +
        "    return 0;\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int prims;\n" +
        "  prims = 0;\n" +
        "  prims = prims + prim(997);\n" +
        "  prims = prims + prim(120);\n" +
        "  prims = prims + prim(887);\n" +
        "  prims = prims + prim(21);\n" +
        "  prims = prims + prim(379);\n" +
        "  prims = prims + prim(380);\n" +
        "  prims = prims + prim(757);\n" +
        "  prims = prims + prim(449);\n" +
        "  prims = prims + prim(5251);\n" +
        "  return prims;\n" +
        "}";
    int[] assembly = Compiler.compile(primTestCode);
    int retVal = Interpreter.execute(assembly);
    assertEquals(5, retVal);
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidCall() {
    String invalidCode = "int ggt(int a, int b) {\n" +
        "  return b;\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int a, b, r;\n" +
        "  a = 3528;\n" +
        "  b = 3780;\n" +
        "  r = ggt(a, b, a);\n" +
        "  return r;\n" +
        "}";
    Compiler.compile(invalidCode);
  }

  @Test(expected = RuntimeException.class)
  public void testUndefinedVariable() {
    String invalidCode = "int ggt(int a, int b) {\n" +
        "  return c;\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int a, b, r;\n" +
        "  a = 3528;\n" +
        "  b = 3780;\n" +
        "  r = ggt(a, b);\n" +
        "  return r;\n" +
        "}";
    Compiler.compile(invalidCode);
  }

  @Test(expected = RuntimeException.class)
  public void testDoubleDefinition() {
    String invalidCode = "int ggt(int a, int b) {\n" +
        "  int a;\n" +
        "  return b;\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int a, b, r;\n" +
        "  a = 3528;\n" +
        "  b = 3780;\n" +
        "  r = ggt(a, b);\n" +
        "  return r;\n" +
        "}";
    Compiler.compile(invalidCode);
  }
}
