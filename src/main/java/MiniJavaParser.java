import java.util.Scanner;

public class MiniJavaParser {

  public static String readProgramConsole() {
    @SuppressWarnings("resource")
    Scanner sin = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (true) {
      String nextLine = sin.nextLine();
      if (nextLine.equals("")) {
        nextLine = sin.nextLine();
        if (nextLine.equals("")) {
          break;
        }
      }
      if (nextLine.startsWith("//")) {
        continue;
      }
      builder.append(nextLine);
      builder.append('\n');
    }
    return builder.toString();
  }

  public static String[] lex(String program) {
    return null; // Todo
  }

  public static int parseNumber(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseName(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseType(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseDecl(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseUnop(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseBinop(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseComp(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseExpression(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseBbinop(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseCondition(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseStatement(String[] program, int from) {
    return -1; // Todo
  }

  public static int parseProgram(String[] program) {
    return -1; // Todo
  }

  public static void main(String[] args) { // Todo
  }
}
