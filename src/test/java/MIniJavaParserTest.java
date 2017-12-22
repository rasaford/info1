import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.*;

public class MIniJavaParserTest {

  @Test
  public void taskExampleParse() {
    String proram = "int sum, n, i; n = read(); while (n < 0) {\n"
        + "n = read(); }\n"
        + "sum = 0;\n"
        + "i = 0;\n"
        + "while (i < n) {\n"
        + "{\n"
        + "if (i % 3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i;\n"
        + "if (i % 3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i; } else\n"
        + "sum = 99; }\n"
        + "i = i + 1; }\n"
        + "}\n"
        + "write(sum);";
    String[] lexed = MiniJavaParser.lex(proram);
    assertNotEquals(-1,
        MiniJavaParser.parseProgram(lexed));
  }

  @Test
  public void testDeclParseValid1() {
    String program = "int a , sum;";
    String[] lexted = MiniJavaParser.lex(program);
    assertNotEquals(-1,
        MiniJavaParser.parseProgram(lexted));
  }

  @Test
  public void testDeclParseValid2() {
    String program = "int a;";
    String[] lexted = MiniJavaParser.lex(program);
    assertNotEquals(-1,
        MiniJavaParser.parseProgram(lexted));
  }

  @Test
  public void testDeclParseInvalid0() {
    String program = "int a, sum";
    String[] lexed = MiniJavaParser.lex(program);
    assertEquals(-1,
        MiniJavaParser.parseProgram(lexed));
  }

  @Test
  public void testDeclParseInvalid1() {
    String program = "it a , sum;";
    String[] lexted = MiniJavaParser.lex(program);
    assertEquals(-1,
        MiniJavaParser.parseProgram(lexted));
  }

  @Test
  public void testDeclParseInvalid2() {
    String program = "int ;";
    String[] lexted = MiniJavaParser.lex(program);
    assertEquals(-1,
        MiniJavaParser.parseProgram(lexted));
  }

  @Test
  public void testLexSmall1() {
    String program = "int sum, n, i;\n"
        + " n = read();\n"
        + " while (n < 0) {\n"
        + "n = read(); \n"
        + "}\n";
    String[] lex = MiniJavaParser.lex(program);
    assertArrayEquals(new String[]{
        "int", "sum", ",", "n", ",", "i", ";",
        "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{",
        "n", "=", "read", "(", ")", ";",
        "}"
    }, lex);
  }

  @Test
  public void testLexSmall2() {
    String program = "intsum,n,i;"
        + "n=read();"
        + "while(n<0){"
        + "n=read();"
        + "}";
    String[] lex = MiniJavaParser.lex(program);
    System.out.println(Arrays.toString(lex));
    assertArrayEquals(new String[]{
        "int", "sum", ",", "n", ",", "i", ";",
        "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{",
        "n", "=", "read", "(", ")", ";",
        "}"
    }, lex);
  }
}
