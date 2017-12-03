import static org.junit.Assert.*;
import org.junit.Test;

public class InterpreterTest {

  @Test
  public void testGGT() {
    String textProgram =
"LDI 3528\n"+
"LDI 3780\n"+
"LDI ggt\n"+
"CALL 2\n"+
"HALT\n"+
"\n"+
"ggt:\n"+
"ALLOC 1\n"+
"LDS 0\n"+
"LDS -1\n"+
"JLT loop\n"+
"LDS -1\n"+
"STS 1\n"+
"LDS 0\n"+
"STS -1\n"+
"LDS 1\n"+
"STS 0\n"+
"loop:\n"+
"LDS 0\n"+
"STS 1\n"+
"LDS -1\n"+
"LDS 0\n"+
"MOD\n"+
"STS 0\n"+
"LDS 1\n"+
"STS -1\n"+
"LDS -0\n"+
"LDI 0\n"+
"JNE loop\n"+
"LDS -1\n"+
"RETURN 3\n";
    int[] program = Interpreter.parse(textProgram);
    int retVal = Interpreter.execute(program);
    assertEquals(252, retVal);
  }
  
  @Test
  public void testFak() {
    String textProgram =
"LDI 6\n"+
"LDI fak\n"+
"CALL 1\n"+
"HALT\n"+
"\n"+
"fak:\n"+
"ALLOC 1\n"+
"LDI 1\n"+
"STS 1\n"+
"LDS 0\n"+
"LDI 1\n"+
"JE end\n"+
"LDI 1\n"+
"LDS 0\n"+
"SUB\n"+
"LDI fak\n"+
"CALL 1\n"+
"LDS 0\n"+
"MUL\n"+
"STS 1\n"+
"end:\n"+
"LDS 1\n"+
"RETURN 2";
    int[] program = Interpreter.parse(textProgram);
    int retVal = Interpreter.execute(program);
    assertEquals(720, retVal);
  }

}
