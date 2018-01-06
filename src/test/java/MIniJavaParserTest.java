import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.*;

public class MIniJavaParserTest {

  @Test
  public void taskExampleParse() {
    String program = "int sum, n, i; "
        + "n = read(); "
        + "while (n < 0) {\n"
        + "n = read(); "
        + "}\n"
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
    String[] lexed = MiniJavaParser.lex(program);
    System.out.println(Arrays.toString(lexed));
    int res = MiniJavaParser.parseProgram(lexed);
    assertEquals(lexed.length, res);
  }

  @Test
  public void lexTestBig() {
    String program = "int sum, n, i;\n"
        + "n = read();\n"
        + "while (n < 0) {\n"
        + "n = read();\n"
        + "}\n"
        + "\n"
        + "sum = 0;\n"
        + "i = 0;\n"
        + "while (i < n) {\n"
        + "{\n"
        + "if (i % 3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i;\n"
        + "if (i % 3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i;\n"
        + "} else\n"
        + "sum = 99;\n"
        + "}\n"
        + "i = i + 1;\n"
        + "}\n"
        + "}\n"
        + "\n"
        + "write(sum);";
    String[] lexed = {
        "int", "sum", ",", "n", ",", "i", ";",
        "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{",
        "n", "=", "read", "(", ")", ";",
        "}",
        "sum", "=", "0", ";",
        "i", "=", "0", ";",
        "while", "(", "i", "<", "n", ")", "{",
        "{",
        "if", "(", "i", "%", "3", "==", "0", "||", "i", "%", "7", "==", "0", ")", "{",
        "sum", "=", "sum", "+", "i", ";",
        "if", "(", "i", "%", "3", "==", "0", "||", "i", "%", "7", "==", "0", ")", "{",
        "sum", "=", "sum", "+", "i", ";",
        "}",
        "else",
        "sum", "=", "99", ";",
        "}",
        "i", "=", "i", "+", "1", ";",
        "}",
        "}",
        "write", "(", "sum", ")", ";", ""
    };
    String[] actualLex = MiniJavaParser.lex(program);
    assertArrayEquals(lexed, actualLex);
  }

  @Test
  public void testDeclParseValid1() {
    String program = "int a , sum;";
    String[] lexed = MiniJavaParser.lex(program);
    assertEquals(5, MiniJavaParser.parseProgram(lexed));
  }

  @Test
  public void testDeclParseValid2() {
    String program = "int a;";
    String[] lexed = MiniJavaParser.lex(program);
    assertNotEquals(-1,
        MiniJavaParser.parseProgram(lexed));
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
    String[] lexed = MiniJavaParser.lex(program);
    assertEquals(-1,
        MiniJavaParser.parseProgram(lexed));
  }

  @Test
  public void testDeclParseInvalid2() {
    String program = "int ;";
    String[] lexed = MiniJavaParser.lex(program);
    assertEquals(-1,
        MiniJavaParser.parseProgram(lexed));
  }

  @Test
  public void testLexSmall1() {
    String program = "int sum, n, i;\n"
        + " n = read();\n"
        + " while (n < 0) {\n"
        + "n = read(); \n"
        + "}\n";
    String[] lexed = MiniJavaParser.lex(program);
    System.out.println(Arrays.toString(lexed));
    assertArrayEquals(new String[]{
        "int", "sum", ",", "n", ",", "i", ";",
        "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{",
        "n", "=", "read", "(", ")", ";",
        "}", ""
    }, lexed);
  }

  @Test
  public void testLexSmall2() {
    String program = "intsum,n,i;"
        + "n=read();"
        + "while(n<0){"
        + "n=read();"
        + "}";
    String[] lexed = MiniJavaParser.lex(program);
    System.out.println(Arrays.toString(lexed));
    assertArrayEquals(new String[]{
        "int", "sum", ",", "n", ",", "i", ";",
        "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{",
        "n", "=", "read", "(", ")", ";",
        "}", ""
    }, lexed);
  }

  @Test
  public void lexSmall3() {
    String prog = "if(i%3==0||i%7==0)";
    String[] lexed = MiniJavaParser.lex(prog);
    System.out.println(Arrays.toString(lexed));
    assertArrayEquals(new String[]{
        "if", "(", "i", "%", "3", "==", "0", "||",
        "i", "%", "7", "==", "0", ")", ""}, lexed);
  }

  @Test
  public void testParseNumber() {
    String[] lexed = MiniJavaParser.lex("123455");
    assertEquals(1, MiniJavaParser.parseNumber(lexed, 0));
  }

  @Test
  public void testParseNumber2() {
    String[] lexed = MiniJavaParser.lex("a123123");
    assertEquals(-1, MiniJavaParser.parseNumber(lexed, 0));
  }

  @Test
  public void testParseNumber3() {
    String[] lexed = MiniJavaParser.lex("1");
    assertEquals(1, MiniJavaParser.parseNumber(lexed, 0));
  }

  @Test
  public void testParseNumber4() {
    String[] lexed = MiniJavaParser.lex("");
    assertEquals(-1, MiniJavaParser.parseNumber(lexed, 0));
  }

  @Test
  public void parseName1() {
    String[] lexed = MiniJavaParser.lex("a123");
    assertEquals(1, MiniJavaParser.parseName(lexed, 0));
  }

  @Test
  public void parseName2() {
    String[] lexed = MiniJavaParser.lex("a");
    assertEquals(1, MiniJavaParser.parseName(lexed, 0));
  }

  @Test
  public void parseName3() {
    String[] lexed = MiniJavaParser.lex("132");
    assertEquals(-1, MiniJavaParser.parseName(lexed, 0));
  }

  @Test
  public void parseName4() {
    String[] lexed = MiniJavaParser.lex("");
    assertEquals(-1, MiniJavaParser.parseName(lexed, 0));
  }


  @Test
  public void parseType1() {
    String[] lexed = MiniJavaParser.lex("int");
    assertEquals(1, MiniJavaParser.parseType(lexed, 0));
  }

  @Test
  public void parseType2() {
    String[] lexed = MiniJavaParser.lex("it");
    assertEquals(-1, MiniJavaParser.parseType(lexed, 0));
  }

  @Test
  public void parseType3() {
    String[] lexed = MiniJavaParser.lex("");
    assertEquals(-1, MiniJavaParser.parseType(lexed, 0));
  }

  @Test
  public void parseDecl() {
    String[] lexed = MiniJavaParser.lex("int n;");
    assertEquals(3, MiniJavaParser.parseDecl(lexed, 0));
  }

  @Test
  public void parseDecl2() {
    String[] lexed = MiniJavaParser.lex("int n, i;");
    assertEquals(5, MiniJavaParser.parseDecl(lexed, 0));
  }

  @Test
  public void parseDecl3() {
    String[] lexed = MiniJavaParser.lex("int n, i");
    assertEquals(-1, MiniJavaParser.parseDecl(lexed, 0));
  }

  @Test
  public void parseDecl4() {
    String[] lexed = MiniJavaParser.lex("int n i;");
    assertEquals(3, MiniJavaParser.parseDecl(lexed, 0));
  }

  @Test
  public void parseDecl5() {
    String[] lexed = MiniJavaParser.lex("int");
    assertEquals(-1, MiniJavaParser.parseDecl(lexed, 0));
  }

  @Test
  public void parseUnop1() {
    String[] lexed = MiniJavaParser.lex("-");
    assertEquals(1, MiniJavaParser.parseUnop(lexed, 0));
  }

  @Test
  public void parseUnop2() {
    String[] lexed = new String[]{"--"};
    assertEquals(-1, MiniJavaParser.parseUnop(lexed, 0));
  }

  @Test
  public void parseUnop3() {
    String[] lexed = MiniJavaParser.lex("");
    assertEquals(-1, MiniJavaParser.parseUnop(lexed, 0));
  }

  @Test
  public void parseBinop1() {
    String[] lexed = MiniJavaParser.lex("-");
    assertEquals(1, MiniJavaParser.parseBinop(lexed, 0));
  }

  @Test
  public void parseBinop2() {
    String[] lexed = MiniJavaParser.lex("+");
    assertEquals(1, MiniJavaParser.parseBinop(lexed, 0));
  }

  @Test
  public void parseBinop3() {
    String[] lexed = MiniJavaParser.lex("*");
    assertEquals(1, MiniJavaParser.parseBinop(lexed, 0));
  }

  @Test
  public void parseBinop4() {
    String[] lexed = MiniJavaParser.lex("/");
    assertEquals(1, MiniJavaParser.parseBinop(lexed, 0));
  }

  @Test
  public void parseBinop5() {
    String[] lexed = MiniJavaParser.lex("");
    assertEquals(-1, MiniJavaParser.parseBinop(lexed, 0));
  }

  @Test
  public void parseComp1() {
    String[] lexed = MiniJavaParser.lex("==");
    assertEquals(1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseComp2() {
    String[] lexed = MiniJavaParser.lex("!=");
    assertEquals(1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseComp3() {
    String[] lexed = MiniJavaParser.lex("<=");
    assertEquals(1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseComp4() {
    String[] lexed = MiniJavaParser.lex("<");
    assertEquals(1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseComp5() {
    String[] lexed = MiniJavaParser.lex(">=");
    assertEquals(1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseComp6() {
    String[] lexed = MiniJavaParser.lex(">");
    assertEquals(1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseComp7() {
    String[] lexed = MiniJavaParser.lex("");
    assertEquals(-1, MiniJavaParser.parseComp(lexed, 0));
  }

  @Test
  public void parseExpression1() {
    String[] lexed = MiniJavaParser.lex("1234");
    assertEquals(1, MiniJavaParser.parseExpression(lexed, 0));
  }

  @Test
  public void parseExpression2() {
    String[] lexed = MiniJavaParser.lex("testProgram");
    assertEquals(1, MiniJavaParser.parseExpression(lexed, 0));
  }

  @Test
  public void parseExpression3() {
    String[] lexed = MiniJavaParser.lex("(testProgram)");
    assertEquals(3, MiniJavaParser.parseExpression(lexed, 0));
  }

  @Test
  public void parseExpression4() {
    String[] lexed = MiniJavaParser.lex("((123)testProgram)");
    assertEquals(-1, MiniJavaParser.parseExpression(lexed, 0));
  }

  @Test
  public void parseExpression5() {
    String[] lexed = MiniJavaParser.lex("-123");
    assertEquals(2, MiniJavaParser.parseExpression(lexed, 0));
  }

  @Test
  public void parseExpression6() {
    String[] lexed = MiniJavaParser.lex("123*testProgram");
    assertEquals(3, MiniJavaParser.parseExpression(lexed, 0));
  }

  @Test
  public void parseStatement1() {
    String[] lexed = MiniJavaParser.lex("{}");
    assertEquals(2, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement2() {
    String[] lexed = MiniJavaParser.lex(";");
    assertEquals(1, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement3() {
    String[] lexed = MiniJavaParser.lex("testProgram = 123;");
    assertEquals(4, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement4() {
    String[] lexed = MiniJavaParser.lex("testProgram = read();");
    assertEquals(6, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement5() {
    String[] lexed = MiniJavaParser.lex("write(12345);");
    assertEquals(6, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement6() {
    String[] lexed = MiniJavaParser.lex("if (true) "
        + "{ testProgram = 2; }");
    assertEquals(10, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement7() {
    String[] lexed = MiniJavaParser.lex("if (true) "
        + "{ testProgram = 2; }"
        + "else"
        + "{ testProgram = 3; }");
    assertEquals(17, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseStatement8() {
    String[] lexed = MiniJavaParser.lex("while (true) "
        + "{ testProgram = 3; }");
    assertEquals(10, MiniJavaParser.parseStatement(lexed, 0));
  }

  @Test
  public void parseCondition1() {
    String[] lexed = MiniJavaParser.lex("true");
    assertEquals(1, MiniJavaParser.parseCondition(lexed, 0));
  }

  @Test
  public void parseCondition2() {
    String[] lexed = MiniJavaParser.lex("false");
    assertEquals(1, MiniJavaParser.parseCondition(lexed, 0));
  }

  @Test
  public void parseCondition3() {
    String[] lexed = MiniJavaParser.lex("(true)");
    assertEquals(3, MiniJavaParser.parseCondition(lexed, 0));
  }

  @Test
  public void parseCondition4() {
    String[] lexed = MiniJavaParser.lex("123 == 123");
    assertEquals(3, MiniJavaParser.parseCondition(lexed, 0));
  }

  @Test
  public void parseCondition5() {
    String[] lexed = MiniJavaParser.lex("!(true)");
    assertEquals(3, MiniJavaParser.parseCondition(lexed, 0));
  }

  @Test
  public void parseCondition6() {
    String[] lexed = MiniJavaParser.lex("true && true");
    assertEquals(3, MiniJavaParser.parseCondition(lexed, 0));
  }
}
