import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MiniJavaParser {

  public static void main(String[] args) {
    String prog = readProgramConsole();
    String[] tokens = lex(prog);
    System.out.println(parseProgram(tokens));
  }

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
    ArrayList<String> tokens = new ArrayList<>();
    Set<String> keywords = new HashSet<>(
        Arrays.asList("int", "read", "write", "if", "else", "while", "true", "false"));
    Set<String> binop = new HashSet<>(
        Arrays.asList("-", "+", "*", "/", "%"));
    Set<String> comp = new HashSet<>(
        Arrays.asList("==", "!=", "<=", "<", ">=", ">"));
    Set<String> bunop = new HashSet<>(
        Arrays.asList("!"));
    Set<String> bbinop = new HashSet<>(
        Arrays.asList("&&", "|"));
    Set<String> misc = new HashSet<>(
        Arrays.asList("{", "}", "(", ")", ",", ";", "="));

    String lastWord = "";
    for (int i = 0; i < program.length(); i++) {
      char c = program.charAt(i);
      if (c == ' ' || c == '\n') {
        continue;
      }
      lastWord += c;
      int match = matches(lastWord, keywords, binop, comp, bunop, bbinop, misc);

      if (match == 1) {
        tokens.add(lastWord);
        lastWord = "";
      } else if (match == 0) {
        int len = lastWord.length();
        tokens.add(lastWord.substring(0, len - 1));
        tokens.add(lastWord.substring(len - 1, len));
        lastWord = "";
      }
    }
    return tokens.toArray(new String[0]);
  }

  /**
   * Checks if the word matches any of the given collections.
   *
   * @param word String to find a match for
   * @param ops Collections to match against
   * @return 1 if a full match was found, 0 if the end matched, -1 if no match was found.
   */
  private static int matches(String word, Collection<String>... ops) {
    for (Collection<String> op : ops) {
      if (op.contains(word)) {
        return 1;
      }
      for (String o : op) {
        if (word.endsWith(o)) {
          return 0;
        }
      }
    }
    return -1;
  }


  public static int parseNumber(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].matches("\\d+") ? from + 1 : -1;
  }

  public static int parseName(String[] program, int from) {
    if (!valid(program, from) || program[from].length() == 0) {
      return from;
    }
    return program[from].matches("^[a-zA-Z]([a-zA-Z]|\\d)*$") ?
        from + 1 : -1;
  }

  public static int parseType(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].equals("int") ? from + 1 : -1;
  }

  public static int parseDecl(String[] program, int from) {
    from = parseType(program, from);
    from = parseName(program, from);
    while (valid(program, from)) {
      if (!program[from].equals(",") || program[from].equals(";")) {
        break;
      }
      from = parseName(program, ++from);
    }
    return valid(program, from) && program[from].equals(";") ?
        from + 1 : -1;
  }

  public static int parseUnop(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].equals("-") ? from + 1 : -1;
  }

  public static int parseBinop(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].matches("[-\\+\\*/%]") ? from + 1 : -1;
  }

  public static int parseComp(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].matches("(==)|(!=)|(<=)|<|(>=)|>") ? from + 1 : -1;
  }

  public static int parseExpression(String[] program, int from) {
    // <number>
    int next = parseNumber(program, from);
    if (valid(program, next)) {
      return next;
    }
    // <name>
    next = parseName(program, from);
    if (valid(program, next)) {
      return next;
    }
    // ( <expr> )
    if (valid(program, from) && program[from].equals("(")) {
      next = parseExpression(program, from + 1);
      return valid(program, next) && program[next].equals(")") ? next + 1 : -1;
    }
    // <unop> <expr>
    next = parseUnop(program, from);
    next = parseExpression(program, next);
    if (valid(program, next)) {
      return next;
    }
    // <expr> <binop> <expr>
    next = parseExpression(program, from);
    next = parseBinop(program, next);
    return parseExpression(program, next);
  }

  public static int parseBbinop(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].matches("(&&)|(\\|\\|)") ? from + 1 : -1;
  }

  public static int parseBunop(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    return program[from].equals("!") ? from + 1 : -1;
  }

  public static int parseCondition(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    // true | false
    if (program[from].matches("(true)|(false)")) {
      return from + 1;
    }
    // ( <cond> )
    if (program[from].equals("(")) {
      int next = parseCondition(program, from + 1);
      return valid(program, next) && program[next].equals(")") ? next + 1 : -1;
    }
    // <expr> <comp> <expr>
    int next = parseExpression(program, from);
    next = parseComp(program, next);
    next = parseExpression(program, next);
    if (valid(program, next)) {
      return next;
    }
    // <bunop> ( <cond> )
    next = parseBunop(program, from);
    if (!valid(program, next) || !program[next].equals("(")) {
      return -1;
    }
    next = parseCondition(program, next + 1);
    if (valid(program, next) && program[next].equals(")")) {
      return next + 1;
    }
    // <cond> <bbunop> <cond>
    next = parseExpression(program, from);
    next = parseBinop(program, next);
    next = parseExpression(program, next);
    return next;
  }

  public static int parseStatement(String[] program, int from) {
    if (!valid(program, from)) {
      return from;
    }
    String t = program[from];
    int next = 0;
    // ;
    if (t.equals(";")) {
      return from + 1;
    }
    // { <stmt>* }
    if (t.equals("{")) {
      from = parseStatement(program, from + 1);
      return from + 1;
    }
    // <name> = <expr>;
    // <name> = read();
    next = parseName(program, from);
    if (!valid(program, next) || !program[next].equals("=")) {
      return -1;
    }
    int temp = parseExpression(program, next + 1);
    if (valid(program, temp) && program[temp].equals(";")) {
      return temp + 1;
    }
    if (program.length - next >= 4
        && program[next].equals("read")
        && program[next + 1].equals("(")
        && program[next + 2].equals(")")
        && program[next + 3].equals(";")) {
      return next + 4;
    }
    // write(<expr>);
    next = from + 1;
    if (t.equals("write") && program[next].equals("(")) {
      next = parseExpression(program, next + 1);
      if (!valid(program, next) || !program[next].equals(")")) {
        return -1;
      }
      next = parseStatement(program, next + 1);
      return next + 1;
    }
    // if (<cond>) <stmt> else <stmt>
    // if (<cond>) <stmt>
    next = from + 1;
    if (t.equals("if") && valid(program, next) && program[next].equals(")")) {
      next = parseCondition(program, next + 1);
      if (!valid(program, next) || !program[next].equals(")")) {
        return -1;
      }
      next = parseStatement(program, next + 1);
      if (valid(program, next) && program[next].equals("else")) {
        return parseStatement(program, next + 1);
      }
      return next;
    }
    // while ( <cond> ) <stmt>
    next = from + 1;
    if (t.equals("while") && valid(program, next) && program[next].equals("(")) {
      next = parseCondition(program, next + 1);
      if (!valid(program, next) || !program[next].equals(")")) {
        return -1;
      }
      return parseStatement(program, next);
    }
    return -1;
  }


  public static int parseProgram(String[] program) {
    if (program == null || program.length == 0) {
      return -1;
    }
    int prevDecl = 0;
    int currentDecl = 0;
    while (valid(program, currentDecl)) {
      prevDecl = currentDecl;
      currentDecl = parseDecl(program, prevDecl);
    }
    if (currentDecl == program.length) { // is the parse already done?
      return currentDecl;
    }
    int prevStmt = prevDecl;
    int currentStmt = prevDecl;
    while (valid(program, currentStmt)) {
      prevStmt = currentStmt;
      currentStmt = parseStatement(program, prevStmt);
    }
    return prevDecl == 0 && prevStmt == prevDecl ? -1 : prevStmt;
  }

  private static boolean valid(String[] program, int from) {
    return 0 <= from && from < program.length;
  }
}
