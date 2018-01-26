package aufgabe9_6;

import static org.junit.Assert.*;

import org.junit.Test;

public class InterpreterTest {

  @Test
  public void testToString() {
    String prog = "LDI 2\n"
        + "LDI 10\n"
        + "MUL\n"
        + "HALT";
    int[] machineCode = Interpreter.parse(prog);
    assertEquals("0: LDI 2\n"
        + "1: LDI 10\n"
        + "2: MUL\n"
        + "3: HALT\n", Interpreter.programToString(machineCode));
  }

  @Test
  public void testGGT() {
    String textProgram = "LDI 3528\n" +
        "LDI 3780\n" +
        "LDI ggt\n" +
        "CALL 2\n" +
        "HALT\n" +
        "\n" +
        "ggt:\n" +
        "ALLOC 1\n" +
        "LDS 0\n" +
        "LDS -1\n" +
        "JLT loop\n" +
        "LDS -1\n" +
        "STS 1\n" +
        "LDS 0\n" +
        "STS -1\n" +
        "LDS 1\n" +
        "STS 0\n" +
        "loop:\n" +
        "LDS 0\n" +
        "STS 1\n" +
        "LDS -1\n" +
        "LDS 0\n" +
        "MOD\n" +
        "STS 0\n" +
        "LDS 1\n" +
        "STS -1\n" +
        "LDS -0\n" +
        "LDI 0\n" +
        "JNE loop\n" +
        "LDS -1\n" +
        "RETURN 3\n";
    int[] program = Interpreter.parse(textProgram);
    int retVal = Interpreter.execute(program);
    assertEquals(252, retVal);
  }

  @Test
  public void testFak10() {
    String textProgram = "LDI 10\n" +
        "LDI fak\n" +
        "CALL 1\n" +
        "HALT\n" +
        "\n" +
        // 1 arg
        "fak:\n" +
        "ALLOC 1\n" +
        "LDI 1\n" +
        "STS 1\n" +
        // x = 1
        "LDS 0\n" +
        "LDI 1\n" +
        // abbruch ?
        "JE done\n" +
        // rekursiver Aufruf mit n - 1
        "LDI 1\n" +
        "LDS 0\n" +
        "SUB\n" +
        "LDI fak\n" +
        "CALL 1\n" +
        // n * fak(n -1)
        "LDS 0\n" +
        "MUL\n" +
        "STS 1\n" +
        "done:\n" +
        "LDS 1\n" +
        // arg freigeben
        "RETURN 2";
    int[] program = Interpreter.parse(textProgram);
    int retVal = Interpreter.execute(program);
    assertEquals(3628800, retVal);
  }

  @Test
  public void testFak6() {
    String textProgram = "LDI 6\n" +
        "LDI fak\n" +
        "CALL 1\n" +
        "HALT\n" +
        "\n" +
        // 1 arg
        "fak:\n" +
        "ALLOC 1\n" +
        "LDI 1\n" +
        "STS 1\n" +
        // x = 1
        "LDS 0\n" +
        "LDI 1\n" +
        // abbruch ?
        "JE done\n" +
        // rekursiver Aufruf mit n - 1
        "LDI 1\n" +
        "LDS 0\n" +
        "SUB\n" +
        "LDI fak\n" +
        "CALL 1\n" +
        // n * fak(n -1)
        "LDS 0\n" +
        "MUL\n" +
        "STS 1\n" +
        "done:\n" +
        "LDS 1\n" +
        // arg freigeben
        "RETURN 2";
    int[] program = Interpreter.parse(textProgram);
    int retVal = Interpreter.execute(program);
    assertEquals(720, retVal);
  }

  @Test
  public void testFak3() {
    String textProgram = "LDI 3\n" +
        "LDI fak\n" +
        "CALL 1\n" +
        "HALT\n" +
        "\n" +
        // 1 arg
        "fak:\n" +
        "ALLOC 1\n" +
        "LDI 1\n" +
        "STS 1\n" +
        // x = 1
        "LDS 0\n" +
        "LDI 1\n" +
        // abbruch ?
        "JE done\n" +
        // rekursiver Aufruf mit n - 1
        "LDI 1\n" +
        "LDS 0\n" +
        "SUB\n" +
        "LDI fak\n" +
        "CALL 1\n" +
        // n * fak(n -1)
        "LDS 0\n" +
        "MUL\n" +
        "STS 1\n" +
        "done:\n" +
        "LDS 1\n" +
        // arg freigeben
        "RETURN 2";
    int[] program = Interpreter.parse(textProgram);
    int retVal = Interpreter.execute(program);
    assertEquals(6, retVal);
  }

  @Test
  public void smallExample() {
    String prog = "ALLOC 1\n"
        + "LDI 0\n"
        + "STS 1\n"
        + "from:\n"
        + "LDS 1\n"
        + "LDI 1\n"
        + "ADD\n"
        + "STS 1\n"
        + "LDI 10\n"
        + "LDS 1\n"
        + "JNE from\n"
        + "HALT";
    int[] code = Interpreter.parse(prog);
    int ret = Interpreter.execute(code);
    assertEquals(10, ret);
  }

  @Test
  public void testADD() {
    String prog = "LDI 44\n"
        + "LDI 11\n"
        + "ADD\n"
        + "HALT";
    int[] pr = Interpreter.parse(prog);
    assertEquals(55, Interpreter.execute(pr));
  }

  @Test
  public void testNegativeADD() {
    String prog = "LDI -10\n"
        + "LDI 10\n"
        + "ADD\n"
        + "HALT\n";
    int[] pr = Interpreter.parse(prog);
    assertEquals(0, Interpreter.execute(pr));
  }

  @Test
  public void testSUB() {
    String prog = "LDI 11\n"
        + "LDI 22\n"
        + "SUB\n"
        + "HALT\n";
    int[] pr = Interpreter.parse(prog);
    assertEquals(11, Interpreter.execute(pr));
  }

  @Test
  public void testMUL() {
    String prog = "LDI 2\n"
        + "LDI 100\n"
        + "MUL\n"
        + "HALT";
    int[] pr = Interpreter.parse(prog);
    assertEquals(200, Interpreter.execute(pr));
  }

  @Test
  public void testMOD() {
    String prog = "LDI 5\n"
        + "LDI 15\n"
        + "MOD\n"
        + "HALT";
    int[] pr = Interpreter.parse(prog);
    assertEquals(0, Interpreter.execute(pr));
  }
}
