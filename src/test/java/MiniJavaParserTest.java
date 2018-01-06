import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MiniJavaParserTest {

  @Before
  public void before() {
    MiniJavaParser.init();
  }

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
  public void parseDecl1() {
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


  @Test
  public void lex() {
    String testProgram = "int sum, n, i;\n"
        + "n = read();\n"
        + "while (n < 0) {\n"
        + "n = read();\n"
        + "}";
    String[] expectedTokens = {
        "int", "sum", ",", "n", ",", "i", ";", "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{", "n", "=", "read", "(", ")", ";", "}", ""
    };
    assertEquals(Arrays.toString(expectedTokens), Arrays.toString(MiniJavaParser.lex(testProgram)));
    // same program with more spaces, tabs and newlines
    String testProgramModified = "int                  sum,     \nn, i;\n"
        + "n = read   ();\n"
        + "while     \n\n\n    \t(n<0) {\n"
        + "n = \tread(   );\n"
        + "}\n";
    assertEquals(Arrays.toString(expectedTokens),
        Arrays.toString(MiniJavaParser.lex(testProgramModified)));
    String[] otherTests = {
        "32<=54",
        "name=something",
        "12>23",
        "((asd<=as)&&true)",
        "234==23",
        "232 == 23",
        "23 >= name",
        "if(x!=0)"
    };
    String[][] otherTestExpected = {
        {"32", "<=", "54", ""},
        {"name", "=", "something", ""},
        {"12", ">", "23", ""},
        {"(", "(", "asd", "<=", "as", ")", "&&", "true", ")", ""},
        {"234", "==", "23", ""},
        {"232", "==", "23", ""},
        {"23", ">=", "name", ""},
        {"if", "(", "x", "!=", "0", ")", ""}
    };
    for (int i = 0; i < otherTests.length; i++) {
      assertEquals(Arrays.toString(otherTestExpected[i]),
          Arrays.toString(MiniJavaParser.lex(otherTests[i])));
    }

    // tests found on piazza, thanks for that one! @2256

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

    String[] expectedLex = {
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
    assertArrayEquals(expectedLex, actualLex);
  }

  @Test
  public void parseNumber() {
    String[][] validNumbers = {
        {"1234"},
        {"1"},
        {"32190120"}
    };
    for (String[] number : validNumbers) {
      assertEquals(1, MiniJavaParser.parseNumber(number, 0));
    }
    String[][] invalidNumbers = {
        {"1234a"},
        {"a31"},
        {""},
        {"."},
        {"(234)"},
        {"234)"},
        {"5514d45"},
        {"notevenasinglenumber"},
        {"a"}
    };
    for (String[] number : invalidNumbers) {
      assertEquals(-1, MiniJavaParser.parseNumber(number, 0));
    }
  }

  @Test
  public void parseName() {
    String[][] validNames = {
        {"name"},
        {"s"},
        {"s5"},
        {"sf4jk"},
        {"thisIsAVeryLongName4Variables"},
        {"Ye4h"},
        {"readable"},
        {"unwrite"},
    };
    for (String[] name : validNames) {
      assertEquals(1, MiniJavaParser.parseName(name, 0));
    }
    String[][] invalidNames = {
        {"if"},
        {"else"},
        {";"},
        {"123"},
        {"12asdf"},
        {"write"},
        {"read"},
        {"while"},
    };
    for (String[] name : invalidNames) {
      assertEquals(-1, MiniJavaParser.parseName(name, 0));
    }
  }

  @Test
  public void parseType() {
    String[] valid = {"blub", "int"};
    assertEquals(2, MiniJavaParser.parseType(valid, 1));
    String[] inValid = {"blub", "inta", "aint", "INT", "234", ";", ""};
    for (int i = 0; i < inValid.length; i++) {
      assertEquals(-1, MiniJavaParser.parseType(inValid, i));
    }

  }

  @Test
  public void parseDecl() {
    String[] input = {"int", "sum", ",", "n", ",", "i",
        ";"}; // https://piazza.com/class/j7hxt8rsfjy1gk?cid=1604
    assertEquals(7, MiniJavaParser.parseDecl(input, 0));
    input[4] = "."; // replace dividing comma by invalid dot
    assertEquals(-1, MiniJavaParser.parseDecl(input, 0));
    String[] inputWithoutComma = {"int", "sum", "n", "i",
        ";"}; // https://piazza.com/class/j7hxt8rsfjy1gk?cid=1604
    assertEquals(-1, MiniJavaParser.parseDecl(inputWithoutComma, 0));
    String[] inputWithoutTypeDef = {"sum", ",", "n", ",", "i",
        ";"}; // https://piazza.com/class/j7hxt8rsfjy1gk?cid=1604
    assertEquals(-1, MiniJavaParser.parseDecl(inputWithoutTypeDef, 0));
    String[] inputWithoutSemicolon = {"int", "sum", ",", "n", ",",
        "i"}; // https://piazza.com/class/j7hxt8rsfjy1gk?cid=1604
    assertEquals(-1, MiniJavaParser.parseDecl(inputWithoutSemicolon, 0));
  }

  @Test
  public void parseUnop() {
    String[] valid = {"blub", "-"};
    assertEquals(2, MiniJavaParser.parseUnop(valid, 1));
    String[] inValid = {"-+", "+", "-1", "dfgh", "-23", ";", ""};
    for (int i = 0; i < inValid.length; i++) {
      assertEquals(-1, MiniJavaParser.parseUnop(inValid, i));
    }
  }

  @Test
  public void parseBinop() {
    String[] valid = {"+", "-", "*", "/", "%"};
    for (int i = 0; i < valid.length; i++) {
      assertEquals(i + 1, MiniJavaParser.parseBinop(valid, i));
    }
    String[] inValid = {"-+", "|", "-1", "a+b", "-23", "2*", "test", "2*2", ""};
    for (int i = 0; i < inValid.length; i++) {
      assertEquals(-1, MiniJavaParser.parseBinop(inValid, i));
    }
  }

  @Test
  public void parseComp() {
    // <comp> ::= == | != | <= | < | >= | >
    String[] valid = {"==", "!=", "<=", "<", ">=", ">"};
    for (int i = 0; i < valid.length; i++) {
      assertEquals(i + 1, MiniJavaParser.parseComp(valid, i));
    }
    String[] inValid = {"-+", "|", "||", "=", "", "2*", "test", "2==2", "a>", "=>"};
    for (int i = 0; i < inValid.length; i++) {
      assertEquals(-1, MiniJavaParser.parseComp(inValid, i));
    }
  }

  @Test
  public void parseExpression() {
    // <expr> ::= <number> | <name> | ( <expr> ) | <unop> <expr> | <expr> <binop> <expr>
    // TODO: extend test cases
    String[][] valid = {{"2345", ""},
//        {"dsfg23", ""},
//        {"(", "235", ")", ""},
//        {"-", "234", ""},
//        {"321", "+", "33", ""},
//        {"321", "+", "(", "name", ")", ""},
//        MiniJavaParser.lex("(b-c)"),
        MiniJavaParser.lex("(a+(b-c)+23)"),
        MiniJavaParser.lex("(-a+(b-c)+(23))"),
    };
    for (String[] aValid : valid) {
      assertEquals(aValid.length - 1, MiniJavaParser.parseExpression(aValid, 0));
    }
    String[][] invalid = {
        {"23a45", ""},
        {"+", "234", ""},
        {"321", "%", "1ame", ""}
    };
    for (String[] anInvalid : invalid) {
      assertEquals(-1, MiniJavaParser.parseExpression(anInvalid, 0));
    }
  }

  @Test
  public void parseBbinop() {
    // || or &&
    String[] valid = {"&&", "||"};
    for (int i = 0; i < valid.length; i++) {
      assertEquals(i + 1, MiniJavaParser.parseBbinop(valid, i));
    }
    String[] inValid = {"-+", "|", "&", "=", "", "2||", "test", "2==2", "a>", "=>"};
    for (int i = 0; i < inValid.length; i++) {
      assertEquals(-1, MiniJavaParser.parseBbinop(inValid, i));
    }
  }

  @Test
  public void parseBunop() {
    // !
    String[] valid = {"!"};
    for (int i = 0; i < valid.length; i++) {
      assertEquals(i + 1, MiniJavaParser.parseBunop(valid, i));
    }
    String[] inValid = {"-+", "|", "&", "!=", "", "2||", "test", "2==2", "a>", "=>"};
    for (int i = 0; i < inValid.length; i++) {
      assertEquals(-1, MiniJavaParser.parseBunop(inValid, i));
    }
  }

  @Test
  public void parseCondition() {
    // true | false | ( <cond> ) | <expr> <comp> <expr> | <bunop> <cond> | <cond> <bbinop> <cond>
    String[][] valid = {
        MiniJavaParser.lex("b==c"),
        {"true"},
        {"false"},
        {"!", "(", "b", "==", "c", ")"},
        {"(", "!", "(", "b", ">=", "4", ")", ")"},
        {"a", ">", "b"},
        {"a", ">", "b", "&&", "true"},
    };
    for (String[] aValid : valid) {
      assertEquals(aValid.length, MiniJavaParser.parseCondition(aValid, 0));
    }
    String[][] invalid = {{"23a45"},
        {">", "true"},
        {"321", "=>", "1ame"}, // unknown comp
        {"(", "a", "==", "b"}, // missing closing bracket
        {"(", "false", ">", "true", ")"}, // cond comp cond
    };
    for (String[] anInvalid : invalid) {
      assertEquals(-1, MiniJavaParser.parseCondition(anInvalid, 0));
    }
  }

  @Test
  public void parseStatement() {
    // <stmt> ::= ; | { <stmt>* } | <name> = <expr>; | <name> = read(); | write(<expr>);
    // | if (<cond>) <stmt> | if (<cond>) <stmt> else <stmt> || while (<cond>) <stmt>
    String[][] validStatements = {
        MiniJavaParser.lex(";"),
        MiniJavaParser.lex("{;}"),
        MiniJavaParser.lex("name = 123;"),
        MiniJavaParser.lex("name = read();"),
        MiniJavaParser.lex("write(12+3);"),
        MiniJavaParser.lex("if (a==b) ;"),
        MiniJavaParser.lex("if(a==b) ; else ;"),
        MiniJavaParser.lex("while (a==b) ;"),
    };
    for (String[] validStatement : validStatements) {
      assertEquals(validStatement.length, MiniJavaParser.parseStatement(validStatement, 0));
    }
  }

  @Test
  public void parseProgram() {
    String[] goodPrograms =
        {"int sum, n, i;\n" + "n = read();\n" + "while (n < 0) {\n" + "n = read();\n" + "}",
            "int sum, n, i;\n" + "n = read();\n" + "while (n < 0) {\n" + "n = read();\n" + "}\n"
                + "sum = 0;\n" + "i = 0;\n" + "while (i < n) {\n" + "{\n"
                + "if (i % 3 == 0 || i % 7 == 0) {\n" + "sum = sum + i;\n"
                + "if (i % 3 == 0 || i % 7 == 0) {\n" + "sum = sum + i;\n" + "} else\n"
                + "sum = 99;\n" + "}\n" + "i = i + 1;\n" + "}\n" + "}\n" + "write(sum);",
            "int prod, x, n;\n" + "x = read();\n" + "if (0 < x) {\n" + "prod = 1;\n" + "n = 0;\n"
                + "while (prod <= x) {\n" + "n = n + 1;\n" + "prod = prod * (-n);\n" + "}\n"
                + "write(prod);\n" + "} else {\n" + "write(n);\n" + "}",
            "int a, b, c;\n" + "a = 42;\n" + "while (a > 10) {\n" + "b = 2*a;\n" + "c = b / 2;\n"
                + "if (c < a) {\n" + "b=b-a;\n" + "}\n" + "a = a - 4;\n" + "}",
            "int a, b;" + "a = 0;\n" + "while (a == 0) {\n" + "a = read();\n" + "}\n"
                + "b = read();\n" + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n"
                + "b = b - a;\n" + "}\n" + "write(a);\n" + "}",
            "int a, b;\n" + "a = 0;\n" + "while (a == 0) {\n" + "a = read();\n" + "}\n"
                + "b = read();\n" + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n"
                + "b = b - a;\n" + "}\n" + "write(a);\n" + "}",
            "int x;\n"
                + "x = read();\n"
                + "if (x == 0)\n"
                + "write(0);\n"
                + "else if (x < 0)\n"
                + "write(-1);\n"
                + "else\n"
                + "write(1);",
            "", // empty program https://piazza.com/class/j7hxt8rsfjy1gk?cid=2312
            ";",
            "int x, y, result;\n" // *** ab hier aus der vorlesung ***
                + "\n"
                + "x = read();\n"
                + "\n"
                + "y = read();\n"
                + "\n"
                + "result = x + y;\n"
                + "\n"
                + "write(result);", // slides page 20
            "int x, y, result;\n"
                + "\n"
                + "x = read();\n"
                + "\n"
                + "y = read();\n"
                + "\n"
                + "if (x > y)\n"
                + "\n"
                + "result = x - y;\n"
                + "\n"
                + "else\n"
                + "\n"
                + "result = y - x;\n"
                + "\n"
                + "write(result);", // slides page 21
            "int x, y;\n"
                + "\n"
                + "x = read();\n"
                + "\n"
                + "if (x != 0) {\n"
                + "\n"
                + "y = read();\n"
                + "\n"
                + "if (x > y)\n"
                + "\n"
                + "write(x);\n"
                + "\n"
                + "else\n"
                + "\n"
                + "write(y);\n"
                + "\n"
                + "} else\n"
                + "\n"
                + "write(0);",
            "int x, y;\n" // ggt programm aus vorlesung
                + "\n"
                + "x = read(); y = read();\n"
                + "\n"
                + "while (x != y) {\n"
                + "\n"
                + "if (x < y)\n"
                + "\n"
                + "y = y - x;\n"
                + "\n"
                + "else\n"
                + "\n"
                + "x = x - y;\n"
                + "\n"
                + "}\n"
                + "\n"
                + "write(x);",
        };
    for (String goodProgram : goodPrograms) {
      String[] token = MiniJavaParser.lex(goodProgram);
      assertEquals(token.length, MiniJavaParser.parseProgram(token));
    }
    String[] badPrograms =
        {"int a, b;\n" + "a = 0;\n" + "while (a == 0) {\n" + "a = read();\n" + "}\n"
            + "b = read();\n"
            + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n" + "b = b - a;\n" + "}\n"
            + "write(a);\n" + "", // misssing } at end
            "int sum, n, i;\n" + "n = read()\n" + "while (n < 0) {\n" + "n = read();\n" + "}",
            // missing semicolon after read()
            "int sum, n, i;\n" + "n = read();\n" + "while (n < 0) {\n" + "n = read();\n" + "}\n"
                + "sum = 0;\n" + "i = 0;\n" + "wile (i < n) {\n" + "{\n"
                + "if (i % 3 == 0 || i % 7 == 0) {\n" + "sum = sum + i;\n"
                + "if (i % 3 == 0 | | i % 7 == 0) {\n" + "sum = sum + i;\n" + "} else\n"
                + "sum = 99;\n" // | | instead of ||
                + "}\n" + "i = i + 1;\n" + "}\n" + "}\n" + "write(sum);",
            "int prod, x, n;\n" + "x = read();\n" + "if (0 < x) {\n" + "prod = 1;\n" + "n = 0;\n"
                + "do while (prod <= x) {\n" + "n = n + 1;\n" + "prod = prod * (-n);\n" + "}\n"
                // do while instead of while
                + "write(prod);\n" + "} else {\n" + "write(n);\n" + "}",
            "int a, b c;\n" + "a = 42;\n" + "while (a > 10) {\n" + "b = 2*a;\n" + "c = b / 2;\n"
                + "if (c < a) {\n" + "b=b-a;\n" + "}\n" + "a = a - 4;\n" + "}",
            // don't know any more
            "int a, b;" + "a = 0;\n" + "while (b == 0 && (a == 0) {\n" + "a = read();\n" + "}\n"
                // missing )
                + "b = read();\n" + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n"
                + "b = b - a;\n" + "}\n" + "write(a);\n" + "}",
            "int a, b;\n" + "a = 0;\n" + "while (a == 0) {\n" + "a = read();\n" + "}\n"
                + "b = read();\n"
                + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n" + "b = b - a;\n" + "\n"
                + "write(a);\n" + "}", // missing }?
            "Int sum, n, i;\n" + "n = read();\n" + "while (n < 0) {\n" + "n = read();\n" + "}",
            // Int
            "int sum, n, i;\n" + "n = read();\n" + "while (n < 0) {\n" + "n = read();\n" + "}\n"
                + "1sum = 0;\n" + "i = 0;\n" + "while (i < n) {\n" + "{\n"
                + "if (i % 3 == 0 || i % 7 == 0) {\n" + "sum = sum + i;\n"
                + "if (i % 3 == 0 || i % 7 == 0) {\n" + "sum = sum + i;\n" + "} else\n"
                + "sum = 99;\n"
                + "}\n" + "i = i + 1;\n" + "}\n" + "}\n" + "write(sum);",
            "int p?rod, x, n;\n" + "x = read();\n" + "if (0 < x) {\n" + "prod = 1;\n" + "n = 0;\n"
                // invalid character in name pr?od
                + "while (prod <= x) {\n" + "n = n + 1;\n" + "prod = prod * (-n);\n" + "}\n"
                + "write(prod);\n" + "} else {\n" + "write(n);\n" + "}",
            "int a, b, c;\n" + "a = 42;\n" + "while (a > 10) {\n" + "b = 2*a;\n" + "c = b / 2;\n"
                // b == b-a
                + "if (c < a) {\n" + "b==b-a;\n" + "}\n" + "a = a - 4;\n" + "}",
            "int a, b;" + "a = 0;\n" + "while (a == 0) {\n" + "a = read();\n" + "}\n"
                + "b <= read();\n" // b <= read()
                + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n" + "b = b - a;\n" + "}\n"
                + "write(a);\n" + "}",
            "int a, int;\n" + "a = 0;\n" + "while (a == 0) {\n" + "a = read();\n" + "}\n"
                // variable named int
                + "b = read();\n" + "if (b > a) {\n" + "while (b > a) {\n" + "write(b);\n"
                + "b = b - a;\n" + "}\n" + "write(a);\n" + "}",
            "    int number, lastNumber; \n" // meier game, invalid because of strings
                + "    int firstDraw, secondDraw; \n"
                + "    int playerNumber; \n"
                + "    int output;\n"
                + "\n"
                + "    lastNumber = playerNumber = 0;\n"
                + "\n"
                + "    number = 1;\n"
                + "    while (number > lastNumber) {\n"
                + "      lastNumber = number;\n"
                + "\n"
                + "      \n"
                + "      firstDraw = dice();\n"
                + "      secondDraw = dice();\n"
                + "\n"
                + "     \n"
                + "      if (firstDraw > secondDraw) {\n"
                + "        number = firstDraw * 10 + secondDraw;\n"
                + "      } else if (secondDraw > firstDraw) {\n"
                + "        number = firstDraw + secondDraw * 10;\n"
                + "      } else {\n"
                + "\n"
                + "        number = firstDraw * 100 + secondDraw * 10;\n"
                + "      }\n"
                + "\n"
                + "\n"
                + "\n"
                + "      if (number == 21) {\n"
                + "\n"
                + "        number = number * 1000;\n"
                + "      }\n"
                + "\n"
                + "      output = number;\n"
                + "      while (output > 100) {\n"
                + "        output = output / 10;\n"
                + "      }\n"
                + "      if (playerNumber == 0) {\n"
                + "        write(\"Du wuerfelst \" + output);\n"
                + "      } else {\n"
                + "        write(\"Computer wuerfelt \" + output);\n"
                + "      }\n"
                + "\n"
                + "      playerNumber = 1 - playerNumber;\n"
                + "    }\n"
                + "    if (playerNumber == 0) {\n"
                + "      write(\"Du hast gewonnen\");\n"
                + "    } else {\n"
                + "      write(\"Computer hat gewonnen\");\n"
                + "    }\n"
                + "  }\n",
            "if(x == 5 5 5) { x = 2; }", // https://piazza.com/class/j7hxt8rsfjy1gk?cid=2303
            "int a", // missing semicolon
            "a = b; int a;" // decl after stmt
        };
    for (String badProgram : badPrograms) {
      String[] token = MiniJavaParser.lex(badProgram);
      assertEquals(-1, MiniJavaParser.parseProgram(token));
    }
  }
}
