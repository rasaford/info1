package aufgabe10_7;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//import aufgabe10_7.compiler.Compiler;
//import aufgabe10_7.interpreter.Interpreter;

public class TailCallTest {
    // Stack-Größe auf 32 anpassen!
//
//  private static final String fakEndRecCode = "int fak(int n, int acc) {\n" +
//      "  if(n == 0)\n" +
//      "    return acc;\n" +
//      "  return fak(n - 1, acc*n);\n" +
//      "}\n" +
//      "\n" +
//      "int main() {\n" +
//      "  return fak(12, 1);\n" +
//      "}\n";
//
//  private static final String fakCode = "int fak(int n) {\n" +
//      "  if(n == 0)\n" +
//      "    return 1;\n" +
//      "  return n*fak(n - 1);\n" +
//      "}\n" +
//      "\n" +
//      "int main() {\n" +
//      "  return fak(12);\n" +
//      "}\n";
//
//  @Test
//  public void testFakEndRecOptimized() {
//    int[] assembly = Compiler.compile(fakEndRecCode);
//    TailCallOptimization.optimize(assembly);
////    System.out.println("------");
////    System.out.println(Interpreter.programToString(assembly));
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void testFakEndRecNoOptimization() {
//    int[] assembly = Compiler.compile(fakEndRecCode);
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void testFakOptimized() {
//    int[] assembly = Compiler.compile(fakCode);
//    TailCallOptimization.optimize(assembly);
////    System.out.println("------");
////    System.out.println(Interpreter.programToString(assembly));
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void testFakNoOptimization() {
//    int[] assembly = Compiler.compile(fakCode);
//    int retVal = Interpreter.execute(assembly);
//    assertEquals(479001600, retVal);
//  }
}
