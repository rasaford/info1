package aufgabe12_9;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompilerTest {

  @Test
  public void testObjectArray() {
    String code =
        "class Foo {\n" +
            "  int a;\n" +
            "\n" +
            "  Foo(int x) {\n" +
            "    a = x;\n" +
            "  }\n" +
            "\n" +
            "  int getA() {\n" +
            "    return a;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo[] foos;\n" +
            "  Foo foo;\n" +
            "  int sum, i;\n" +
            "  foos = new Foo[5];\n" +
            "  i = 0;\n" +
            "  while(i < length(foos)) {\n" +
            "    foos[i] = new Foo(2*i + 1);\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  i = length(foos);\n" +
            "  sum = 0;\n" +
            "  while(i > 0) {\n" +
            "    foo = foos[i - 1];\n" +
            "    sum = sum + foo.getA();\n" +
            "    i = i - 1;\n" +
            "  }\n" +
            "  return sum;\n" +
            "}";
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(25, retVal);
  }

  @Test
  public void testVisitor() {
    String code =
        "class Grundflaeche {\n" +
            "  Grundflaeche() {\n" +
            "  }\n" +
            "\n" +
            "  int accept(Visitor visitor) {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Visitor {\n" +
            "  Visitor() {\n" +
            "  }  \n" +
            "\n" +
            "  int visitQ(Quadrat quadrat) {\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "  int visitR(Rechteck rechteck) {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class FlaechenVisitor extends Visitor {\n" +
            "  int flaeche;\n" +
            "\n" +
            "  FlaechenVisitor() {\n" +
            "    super.Visitor();\n" +
            "  }  \n" +
            "  \n" +
            "  int getFlaeche() {\n" +
            "    return flaeche;\n" +
            "  }\n" +
            "\n" +
            "  int visitQ(Quadrat quadrat) {\n" +
            "    flaeche = quadrat.getLaenge() * quadrat.getLaenge();\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "  int visitR(Rechteck rechteck) {\n" +
            "    flaeche = rechteck.getBreite() * rechteck.getLaenge();\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "}\n" +
            "\n" +
            "class Rechteck extends Grundflaeche {\n" +
            "  int breite;\n" +
            "\n" +
            "  int laenge;\n" +
            "  \n" +
            "  Rechteck(int breiteP, int laengeP) {\n" +
            "    super.Grundflaeche();\n" +
            "    breite = breiteP;\n" +
            "    laenge = laengeP;\n" +
            "  }\n" +
            "\n" +
            "  int getBreite() {\n" +
            "    return breite;\n" +
            "  }\n" +
            "\n" +
            "  int getLaenge() {\n" +
            "    return laenge;\n" +
            "  }\n" +
            "  \n" +
            "  int accept(Visitor visitor) {\n" +
            "    visitor.visitR(this);\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Quadrat extends Grundflaeche {\n" +
            "  int laenge;\n" +
            "\n" +
            "  Quadrat(int laengeP) {\n" +
            "    super.Grundflaeche();\n" +
            "    laenge = laengeP;\n" +
            "  }\n" +
            "\n" +
            "  int getLaenge() {\n" +
            "    return laenge;\n" +
            "  }\n" +
            "  \n" +
            "  int accept(Visitor visitor) {\n" +
            "    visitor.visitQ(this);\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int flaeche(Grundflaeche g) {\n" +
            "  FlaechenVisitor visitor;\n" +
            "  visitor = new FlaechenVisitor();\n" +
            "  g.accept(visitor);\n" +
            "  return visitor.getFlaeche();\n" +
            "}\n" +
            "  \n" +
            "int main() {\n" +
            "  Grundflaeche g2, g3, g6;\n" +
            "  int sum;\n" +
            "  g2 = new Rechteck(2, 3);\n" +
            "  g3 = new Rechteck(4, 4);\n" +
            "  g6 = new Quadrat(8);\n" +
            "  sum = flaeche(g2);\n" +
            "  sum = sum + flaeche(g3);\n" +
            "  sum = sum + flaeche(g6);\n" +
            "  return sum;\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(86, retVal);
  }

  @Test
  public void testHeap() {
    String code =
        "class Heap {\n" +
            "\n" +
            "  int[] heap;\n" +
            "  int[] sorted;\n" +
            "\n" +
            "  Heap(int size) {\n" +
            "    heap = new int[size];\n" +
            "  }\n" +
            "\n" +
            "  int[] getHeap() {\n" +
            "    return heap;\n" +
            "  }\n" +
            "\n" +
            "  int[] getSorted() {\n" +
            "    return sorted;\n" +
            "  }\n" +
            "\n" +
            "  int sort() {\n" +
            "    int i;\n" +
            "    sorted = new int[length(heap)];\n" +
            "    this.buildHeap();\n" +
            "    i = 0;\n" +
            "    while (i < length(heap)) {\n" +
            "      sorted[i] = heap[0];\n" +
            "      heap[0] = heap[length(heap) - (1 + i)];\n" +
            "      heap[length(heap) - (1 + i)] = -1;\n" +
            "      this.down(0, length(heap) - (2 + i));\n" +
            "      i = i + 1;\n" +
            "    }\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "\n" +
            "  int swap(int first, int second) {\n" +
            "    int temp;\n" +
            "    temp = heap[first];\n" +
            "    heap[first] = heap[second];\n" +
            "    heap[second] = temp;\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "  int daughter(int index) {\n" +
            "    return 2 * index + 1;\n" +
            "  }\n" +
            "\n" +
            "  int son(int index) {\n" +
            "    return 2 * index + 2;\n" +
            "  }\n" +
            "\n" +
            "  int buildHeap() {\n" +
            "    int index;\n" +
            "    index = length(heap) / 2;\n" +
            "    while(index >= 0) {\n" +
            "      this.down(index, length(heap) - 1);\n" +
            "      index = index - 1;\n" +
            "    }\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "  int down(int index, int stop) {\n" +
            "    int mindex;\n" +
            "    if (index > stop) {\n" +
            "      return 0;\n" +
            "    }\n" +
            "    mindex = this.minIndex(index, this.daughter(index), this.son(index), stop);\n" +
            "    if (mindex != index)\n" +
            "      if (heap[mindex] < heap[index]) {\n" +
            "        this.swap(index, mindex);\n" +
            "        this.down(mindex, stop);\n" +
            "      }" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "  int minIndex(int i1, int i2, int i3, int stop) {\n" +
            "    if(i2 > stop) {\n" +
            "      if (i3 > stop)\n" +
            "        return i1;\n" +
            "      else\n" +
            "        return i3;\n" +
            "    }\n" +
            "    if (heap[i1] <= heap[i2]) {\n" +
            "      if (i3 > stop)\n" +
            "        return i1;\n" +
            "      if (heap[i1] <= heap[i3])\n" +
            "        return i1;\n" +
            "      else\n" +
            "        return i3;\n" +
            "    } else {\n" +
            "      if(i3 > stop)\n" +
            "        return i2;\n" +
            "      if (heap[i2] <= heap[i3])\n" +
            "        return i2;\n" +
            "      else\n" +
            "        return i3;\n" +
            "    }\n" +
            "  }" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Heap heap;\n" +
            "  int[] arr;\n" +
            "  int i, sum;\n" +
            "  heap = new Heap(10);\n" +
            "  arr = heap.getHeap();\n" +
            "  arr[0] = 4;\n" +
            "  arr[1] = 1;\n" +
            "  arr[2] = 9;\n" +
            "  arr[3] = 7;\n" +
            "  arr[4] = 8;\n" +
            "  arr[5] = -10;\n" +
            "  arr[6] = 50;\n" +
            "  arr[7] = 2;\n" +
            "  arr[8] = 15;\n" +
            "  arr[9] = -3;\n" +
            "  heap.sort();\n" +
            "  arr = heap.getSorted();\n" +
            "  if(arr[0] != -10) return 99;\n" +
            "  if(arr[1] != -3) return 99;\n" +
            "  if(arr[2] != 1) return 99;\n" +
            "  if(arr[3] != 2) return 99;\n" +
            "  if(arr[4] != 4) return 99;\n" +
            "  if(arr[5] != 7) return 99;\n" +
            "  if(arr[6] != 8) return 99;\n" +
            "  if(arr[7] != 9) return 99;\n" +
            "  if(arr[8] != 15) return 99;\n" +
            "  if(arr[9] != 50) return 99;\n" +
            "  return 42;\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(42, retVal);
  }


  @Test
  public void testSuper() {
    String code =
        "class Foo {\n" +
            "  Foo() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 20;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Bar extends Foo {\n" +
            "  Bar() {\n" +
            "    super.Foo();\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return super.x();\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Bar b;\n" +
            "  b = new Bar();\n" +
            "  return b.x();\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(20, retVal);
  }

  @Test
  public void testInheritance2() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo extends Bar {\n" +
            "  Foo() {\n" +
            "    super.Bar();\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 99;\n" +
            "  }\n" +
            "\n" +
            "  int y() {\n" +
            "    return this.x() + 2;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo1, foo2;\n" +
            "  Bar bar1, bar2;\n" +
            "  foo1 = new Foo();\n" +
            "  foo2 = foo1;\n" +
            "  bar1 = new Bar();\n" +
            "  bar2 = foo1;\n" +
            "  return foo1.x() + foo2.x() + bar1.x() + bar2.x();\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(319, retVal);
  }

  @Test
  public void testInheritance1() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo extends Bar {\n" +
            "  Foo() {\n" +
            "    super.Bar();\n" +
            "  }\n" +
            "\n" +
            "  int y() {\n" +
            "    return this.x() + 2;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.y() + foo.x();\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(46, retVal);
  }

  @Test
  public void testClassBasic1() {
    String code =
        "class Foo {\n" +
            "  int a;\n" +
            "\n" +
            "  Foo() {\n" +
            "    a = 100;\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return a;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.x();\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(100, retVal);
  }

  @Test
  public void testClassBasic2() {
    String code =
        "class Foo {\n" +
            "  int a;\n" +
            "\n" +
            "  Foo() {\n" +
            "    a = 100;\n" +
            "  }\n" +
            "\n" +
            "  int y() {\n" +
            "    return a + a;\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 1 + this.y() + this.y();\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.x();\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(401, retVal);
  }

  @Test
  public void testClass3() {
    String code =
        "class Bar {\n" +
            "  int a;\n" +
            "\n" +
            "  Bar() {\n" +
            "    a = 7;\n" +
            "  }\n" +
            "\n" +
            "  int test(Foo f) {\n" +
            "    return f.x() + a;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo {\n" +
            "  int a;\n" +
            "\n" +
            "  Foo(int k) {\n" +
            "    a = k;\n" +
            "  }\n" +
            "\n" +
            "  int y() {\n" +
            "    return a + a;\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 1 + this.y() + this.y();\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  Bar bar;\n" +
            "  foo = new Foo(100);\n" +
            "  bar = new Bar();\n" +
            "  return bar.test(foo);\n" +
            "}";
//    Parser p = new Parser(code);
//    Program ast = p.parse();
//    System.out.println(ast);
    int[] assembly = Compiler.compile(code);
    System.out.println("######################");
    System.out.println(Interpreter.programToString(assembly));
    int retVal = Interpreter.execute(assembly);
    assertEquals(408, retVal);
  }

  @Test
  public void testArraysBasic() {
    String code =
        "int main() {\n" +
            "  int[] arr;\n" +
            "  int i, n, sum;\n" +
            "  n = 5;\n" +
            "  arr = new int[n];\n" +
            "  i = 0;\n" +
            "  while(i < n) {\n" +
            "    arr[i] = 2*i;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  sum = 0;\n" +
            "  i = 0;\n" +
            "  while(i < n) {\n" +
            "    sum = sum + (arr[i]);\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return sum;\n" +
            "}";
    int[] assembly = Compiler.compile(code);
    int retVal = Interpreter.execute(assembly);
    assertEquals(20, retVal);
  }

  @Test
  public void testArraysFunction() {
    String code =
        "int sum(int[] arr, int n) {\n" +
            "  int i, sum;\n" +
            "  sum = 0;\n" +
            "  i = 0;\n" +
            "  while(i < n) {\n" +
            "    sum = sum + (arr[i]);\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return sum;\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int[] arr;\n" +
            "  int i, n, x, sum;\n" +
            "  n = 5;\n" +
            "  arr = new int[n];\n" +
            "  i = 0;\n" +
            "  while(i < n) {\n" +
            "    arr[i] = 2*i;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return sum(arr, n);\n" +
            "}";
    int[] assembly = Compiler.compile(code);
    int retVal = Interpreter.execute(assembly);
    assertEquals(20, retVal);
  }

  @Test
  public void testArraysFunctionReturnsArray() {
    String code =
        "int[] generateArray(int n) {\n" +
            "  int[] arr;\n" +
            "  int i;\n" +
            "  arr = new int[n];\n" +
            "  i = 0;\n" +
            "  while(i < n) {\n" +
            "    arr[i] = 2*i;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return arr;\n" +
            "}\n" +
            "\n" +
            "int sum(int[] arr, int n) {\n" +
            "  int i, sum;\n" +
            "  sum = 0;\n" +
            "  i = 0;\n" +
            "  while(i < n) {\n" +
            "    sum = sum + (arr[i]);\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return sum;\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int[] arr;\n" +
            "  int n;\n" +
            "  n = 5;\n" +
            "  arr = generateArray(n);\n" +
            "  return sum(arr, n);\n" +
            "}";
    int[] assembly = Compiler.compile(code);
    int retVal = Interpreter.execute(assembly);
    assertEquals(20, retVal);
  }

  @Test
  public void testArrayLength() {
    String code =
        "int[] generateArray() {\n" +
            "  int[] arr;\n" +
            "  int i;\n" +
            "  arr = new int[6];\n" +
            "  i = 0;\n" +
            "  while(i < length(arr)) {\n" +
            "    arr[i] = 2*i;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return arr;\n" +
            "}\n" +
            "\n" +
            "int sum(int[] arr) {\n" +
            "  int i, sum;\n" +
            "  sum = 0;\n" +
            "  i = 0;\n" +
            "  while(i < length(arr)) {\n" +
            "    sum = sum + (arr[i]);\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  return sum;\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int[] arr;\n" +
            "  arr = generateArray();\n" +
            "  return sum(arr);\n" +
            "}";
    int[] assembly = Compiler.compile(code);
    int retVal = Interpreter.execute(assembly);
    assertEquals(30, retVal);
  }

  @Test
  public void testPalindrome() {
    String code =
        "int isPalindrome(int n) {\n" +
            "  int[] digits;\n" +
            "  int numberOfDigits, t, i, notMatching, digit;\n" +
            "\n" +
            "  numberOfDigits = 0;\n" +
            "  t = n;\n" +
            "  while (t != 0) {\n" +
            "    numberOfDigits = numberOfDigits + 1;\n" +
            "    t = t / 10;\n" +
            "  }\n" +
            "\n" +
            "  digits = new int[numberOfDigits];\n" +
            "\n" +
            "  i = 0;\n" +
            "  while (n != 0) {\n" +
            "    digit = n % 10;\n" +
            "    digits[i] = digit;\n" +
            "    n = n / 10;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "\n" +
            "  notMatching = 0;\n" +
            "  i = 0;\n" +
            "  while (i < numberOfDigits / 2) {\n" +
            "    if (digits[i] != digits[(numberOfDigits - i) - 1])\n" +
            "      notMatching = notMatching + 1;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "\n" +
            "  if (notMatching == 0)\n" +
            "    return 1;\n" +
            "  else\n" +
            "    return 0;\n" +
            "}\n" +
            " \n" +
            "int main() {\n" +
            "  int n;\n" +
            "  n = 0;\n" +
            "  n = n + isPalindrome(4224);\n" +
            "  n = n + isPalindrome(10);\n" +
            "  n = n + isPalindrome(99);\n" +
            "  n = n + isPalindrome(123321);\n" +
            "  n = n + isPalindrome(19910);\n" +
            "  n = n + isPalindrome(0990);\n" +
            "  n = n + isPalindrome(111111);\n" +
            "  n = n + isPalindrome(1112111);\n" +
            "  return n;" +
            "}";
    int[] assembly = Compiler.compile(code);
    int retVal = Interpreter.execute(assembly);
    assertEquals(5, retVal);
  }

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
    String code = "int fak(int n) {\n" +
        "  if(n == 0)\n" +
        "    return 1;\n" +
        "  return n*fak(n - 1);\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  return fak(6);\n" +
        "}\n";
    int[] assembly = Compiler.compile(code);
    int retVal = Interpreter.execute(assembly);
    assertEquals(720, retVal);
  }

  @Test
  public void testPrim() {
    String code = "int prim(int n) {\n" +
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
    int[] assembly = Compiler.compile(code);
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


  /**
   * own tests
   */
  @Test(expected = RuntimeException.class)
  public void testThisOutsideClass() {
    String invalidCode = "int ggt(int a, int b) {\n" +
        "  return b;\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int a, b, r;\n" +
        "  a = 3528;\n" +
        "  b = 3780;\n" +
        "  r = this.ggt(a, b);\n" +
        "  return r;\n" +
        "}";
    Compiler.compile(invalidCode);

  }

  @Test(expected = RuntimeException.class)
  public void testSuperOutsideClass() {
    String invalidCode = "int ggt(int a, int b) {\n" +
        "  int a;\n" +
        "  return b;\n" +
        "}\n" +
        "\n" +
        "int main() {\n" +
        "  int a, b, r;\n" +
        "  a = 3528;\n" +
        "  b = 3780;\n" +
        "  r = super.ggt(a, b);\n" +
        "  return r;\n" +
        "}";
    Compiler.compile(invalidCode);
  }

  @Test(expected = RuntimeException.class)
  public void noCallToSuperConstructor() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo extends Bar {\n" +
            "  Foo() {\n" +
            "  }\n" +
            "\n" +
            "  int y() {\n" +
            "    return this.x() + 2;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.y() + foo.x();\n" +
            "}";
    Compiler.compile(code);
  }

  @Test(expected = RuntimeException.class)
  public void noCallToSuperConstructor2() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo extends Bar {\n" +
            "  Foo() {\n" +
            "  int a;\n" +
            "  super.Bar()\n" +
            "  }\n" +
            "\n" +
            "  int y() {\n" +
            "    return this.x() + 2;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.y() + foo.x();\n" +
            "}";
    Compiler.compile(code);
  }

  @Test(expected = RuntimeException.class)
  public void classWithoutConstructor() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo extends Bar {\n" +
            "\n" +
            "  int y() {\n" +
            "    return this.x() + 2;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.y() + foo.x();\n" +
            "}";
    Compiler.compile(code);
  }

  @Test
  public void dynamicMethodBinding() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Foo extends Bar {\n" +
            "\n"
            + "Foo() {\n"
            + "   super.Bar();\n"
            + "}\n"
            + "\n"
            + "int x() {\n"
            + "   return 42;\n"
            + "}\n"
            + "\n" +
            "  int y() {\n" +
            "    return this.x() + 2;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  Foo foo;\n" +
            "  foo = new Foo();\n" +
            "  return foo.y() + foo.x();\n" +
            "}";
    int[] assembler = Compiler.compile(code);
    int ret = Interpreter.execute(assembler);
    assertEquals(66, ret);
  }


  @Test(expected = RuntimeException.class)
  public void doubleClassDefinition() {
    String code =
        "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 22;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "class Bar {\n" +
            "  Bar() {\n" +
            "  }\n" +
            "\n" +
            "  int x() {\n" +
            "    return 42;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n"
            + "    Bar bar;\n"
            + "    bar = new Bar();\n" +
            "}";
    int[] assembler = Compiler.compile(code);
    int ret = Interpreter.execute(assembler);
    assertEquals(66, ret);
  }
}
