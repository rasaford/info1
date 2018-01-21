package aufgabe11_7;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompilerTest {

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
    System.out.println(Interpreter.programToString(assembly));
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
    System.out.println(Interpreter.programToString(assembly));
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
  public void testQuicksort() {
    String program = "int printArr(int[] numbers) {\n"
        + "  int i;\n"
        + "  i = 0;\n"
        + "  while(i < length(numbers)) {\n"
        + "    write(numbers[i]);\n"
        + "    i = i + 1;\n"
        + "  }\n"
        + "  return 0;\n"
        + "}\n"
        + "\n"
        + "int main() {\n"
        + "    int trash;\n"
        + "    int[] array;\n"
        + "    array = new int[7];\n"
        + "    array[0] = 234;\n"
        + "    array[1] = 123;\n"
        + "    array[2] = 2;\n"
        + "    array[3] = 27564;\n"
        + "    array[4] = 333;\n"
        + "    array[5] = 12 * 4 / 234 % 2 + 23432;\n"
        + "    array[6] = length(array);\n"
        + "\n"
//        + "\ttrash = printArr(array);\n"
        + "\ttrash = quicksort(array, 0, length(array) - 1);\n"
//        + "\ttrash = printArr(array);\n"
        + "\treturn 0;\n"
        + "}\n"
        + "\n"
        + "int quicksort(int[] numbers, int left, int right) {\n"
        + "  int pivot, trash;\n"
        + "  if (right - left < 1) {\n"
        + "    return 0;\n"
        + "  }\n"
        + "  pivot = partition(numbers, left, right);\n"
        + "  trash = quicksort(numbers, left, pivot-1);\n"
        + "  trash = quicksort(numbers, pivot+1, right);\n"
        + "  return 0;\n"
        + "}\n"
        + "\n"
        + "int swap(int[] numbers, int i, int j) {\n"
        + "  int tmp;\n"
        + "  tmp = numbers[i];\n"
        + "  numbers[i] = numbers[j];\n"
        + "  numbers[j] = tmp;\n"
        + "  return 0;\n"
        + "}\n"
        + "\n"
        + "int partition(int[] numbers, int left, int right) {\n"
        + "  int pivot, count, i, trash;\n"
        + "  pivot = numbers[right];\n"
        + "  count = left;\n"
        + "  i = left;\n"
        + "  while(i < right) {\n"
        + "    if (numbers[i] < pivot) {\n"
        + "      trash = swap(numbers, i, count);\n"
        + "      count = count + 1;\n"
        + "    }\n"
        + "    i = i + 1;\n"
        + "  }\n"
        + "  trash = swap(numbers, right, count);\n"
        + "  return count;\n"
        + "}";
    int[] assembly = Compiler.compile(program);
    System.out.println(Interpreter.programToString(assembly));
    long start = System.nanoTime();
    int res = Interpreter.execute(assembly);
    System.out.println("Execution time: " + (System.nanoTime() - start) + "ns");
    assertEquals(0, res);
  }

  @Test
  public void testQuicksortLarge() {
    String program = "int printArr(int[] numbers) {\n"
        + "  int i;\n"
        + "  i = 0;\n"
        + "  while(i < length(numbers)) {\n"
        + "    write(numbers[i]);\n"
        + "    i = i + 1;\n"
        + "  }\n"
        + "  return 0;\n"
        + "}\n"
        + "\n"
        + "int main() {\n"
        + "    int trash, i;\n"
        + "    int[] array;\n"
        + "    array = new int[100];\n"
        + "    while (i < length(array)) {\n"
        + "       array[i] = length(array) - i;\n"
        + "       i = i + 1;\n"
        + "     }"
        + "\n"
//        + "\ttrash = printArr(array);\n"
        + "\ttrash = quicksort(array, 0, length(array) - 1);\n"
//        + "\ttrash = printArr(array);\n"
        + "\treturn 0;\n"
        + "}\n"
        + "\n"
        + "int quicksort(int[] numbers, int left, int right) {\n"
        + "  int pivot, trash;\n"
        + "  if (right - left < 1) {\n"
        + "    return 0;\n"
        + "  }\n"
        + "  pivot = partition(numbers, left, right);\n"
        + "  trash = quicksort(numbers, left, pivot-1);\n"
        + "  trash = quicksort(numbers, pivot+1, right);\n"
        + "  return 0;\n"
        + "}\n"
        + "\n"
        + "int swap(int[] numbers, int i, int j) {\n"
        + "  int tmp;\n"
        + "  tmp = numbers[i];\n"
        + "  numbers[i] = numbers[j];\n"
        + "  numbers[j] = tmp;\n"
        + "  return 0;\n"
        + "}\n"
        + "\n"
        + "int partition(int[] numbers, int left, int right) {\n"
        + "  int pivot, count, i, trash;\n"
        + "  pivot = numbers[right];\n"
        + "  count = left;\n"
        + "  i = left;\n"
        + "  while(i < right) {\n"
        + "    if (numbers[i] < pivot) {\n"
        + "      trash = swap(numbers, i, count);\n"
        + "      count = count + 1;\n"
        + "    }\n"
        + "    i = i + 1;\n"
        + "  }\n"
        + "  trash = swap(numbers, right, count);\n"
        + "  return count;\n"
        + "}";
    int[] assembly = Compiler.compile(program);
    System.out.println(Interpreter.programToString(assembly));
    long start = System.currentTimeMillis();
    int res = Interpreter.execute(assembly);
    System.out.println("Execution time: " + (System.currentTimeMillis() - start) + "ms");
    assertEquals(0, res);
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
}
