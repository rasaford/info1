import org.junit.Test;

import static org.junit.Assert.*;

public class MIniJavaParserTest {

  @Test
  public void taskExampleParse(){
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
    assertNotEquals(-1 ,
        MiniJavaParser.parseProgram(lexed));
  }
}
