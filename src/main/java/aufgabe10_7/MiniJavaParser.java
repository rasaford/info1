package aufgabe10_7;

import aufgabe10_7.codegen.Assignment;
import aufgabe10_7.codegen.Bbinop;
import aufgabe10_7.codegen.Binary;
import aufgabe10_7.codegen.BinaryCondition;
import aufgabe10_7.codegen.Binop;
import aufgabe10_7.codegen.Bunop;
import aufgabe10_7.codegen.Call;
import aufgabe10_7.codegen.Comp;
import aufgabe10_7.codegen.Comparison;
import aufgabe10_7.codegen.Composite;
import aufgabe10_7.codegen.Condition;
import aufgabe10_7.codegen.Declaration;
import aufgabe10_7.codegen.EmptyStatement;
import aufgabe10_7.codegen.Expression;
import aufgabe10_7.codegen.False;
import aufgabe10_7.codegen.Function;
import aufgabe10_7.codegen.IfThen;
import aufgabe10_7.codegen.IfThenElse;
import aufgabe10_7.codegen.Number;
import aufgabe10_7.codegen.Program;
import aufgabe10_7.codegen.Read;
import aufgabe10_7.codegen.Return;
import aufgabe10_7.codegen.Statement;
import aufgabe10_7.codegen.True;
import aufgabe10_7.codegen.Unary;
import aufgabe10_7.codegen.UnaryCondition;
import aufgabe10_7.codegen.Unop;
import aufgabe10_7.codegen.Variable;
import aufgabe10_7.codegen.While;
import aufgabe10_7.codegen.Write;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MiniJavaParser {

  private String[] tokens;
  private int from;
  private Stack<Integer> fromStack = new Stack<>();

  private final int PADDING = 3;

  public MiniJavaParser(String program) {
    this.tokens = lex(program);
    this.from = 0;
  }

  private String[] lex(String input) {
    ArrayList<String> tokens = new ArrayList<>();
    int index = 0;
    while (index < input.length()) {
      char next = input.charAt(index);
      if (next == ' ' || next == '\n' || next == '\r' || next == '\t') {
        index++;
        continue;
      }
      switch (next) {
        case '{':
        case '}':
        case '(':
        case ')':
        case '+':
        case '-':
        case '/':
        case '*':
        case '%':
        case ';':
          index++;
          tokens.add("" + next);
          continue;

      }
      StringBuilder tokenBuilder = new StringBuilder();
      compBinAssLoop:
      while (index < input.length()) {
        char nextOp = input.charAt(index);
        switch (nextOp) {
          case '=':
          case '!':
          case '<':
          case '>':
          case '&':
          case '|':
            tokenBuilder.append(nextOp);
            break;
          default:
            break compBinAssLoop;
        }
        index++;
      }
      if (tokenBuilder.length() == 0) {
        while (index < input.length()) {
          char nextLetterNumber = input.charAt(index);
          if (nextLetterNumber >= 'a' && nextLetterNumber <= 'z'
              || nextLetterNumber >= 'A' && nextLetterNumber <= 'Z'
              || nextLetterNumber >= '0' && nextLetterNumber <= '9') {
            tokenBuilder.append(nextLetterNumber);
          } else {
            break;
          }
          index++;
        }
      }
      if (tokenBuilder.length() > 0) {
        tokens.add(tokenBuilder.toString());
      } else {
        index++;
        tokens.add("" + next);
      }
    }
    // Padding
    for (int i = 0; i < PADDING; i++) {
      tokens.add("");
    }
    return tokens.toArray(new String[0]);
  }

  public Program parse() {
    List<Function> functions = new ArrayList<>();
    Function f;
    while (true) {
      f = parseFunction();
      if (f == null) {
        break;
      }
      functions.add(f);
    }
    if (from != tokens.length - PADDING) {
      throw new RuntimeException(
          String.format("parse failed at token %d:%s\n", from, tokens[from]));
    }
    return new Program(functions.toArray(new Function[0]));
  }

  private Function parseFunction() {
    if (parseType() < 0) {
      return null;
    }
    String name = parseName();
    if (name == null) {
      return null;
    }

    List<String> parameters = parseParameters();
    if (parameters == null) {
      return null;
    }
    if (!tokens[from++].equals("{")) {
      return null;
    }

    List<Declaration> declarations = new ArrayList<>();
    Declaration decl;
    do {
      decl = parseDecl();
      if (decl == null) {
        break;
      }
      declarations.add(decl);
    } while (true);

    List<Statement> statements = new ArrayList<>();
    while (true) {
      Statement stmt = parseStatement();
      if (stmt == null) {
        break;
      }
      statements.add(stmt);
    }
    if (!tokens[from++].equals("}")) {
      return null;
    } else {
      return new Function(name,
          parameters.toArray(new String[0]),
          declarations.toArray(new Declaration[0]),
          statements.toArray(new Statement[0]));
    }

  }

  private List<String> parseParameters() {
    if (!valid() || !tokens[from].equals("(")) {
      return null;
    }
    List<String> params = new ArrayList<>();
    do {
      from++;
      if (parseType() < 0) {
        break;
      }
      String name = parseName();
      if (name == null) {
        return null;
      }
      params.add(name);
    } while (tokens[from].equals(","));
    if (!valid() || !tokens[from].equals(")")) {
      return null;
    }
    from++;
    return params;
  }

  private boolean valid() {
    return 0 <= from && from < tokens.length;
  }

  private Number parseNumber() {
    String num = tokens[from];
    for (int i = 0; i < num.length(); i++) {
      char next = num.charAt(i);
      if (!(next >= '0' && next <= '9')) {
        return null;
      }
    }
    from++;
    return new Number(Integer.parseInt(num));
  }

  private boolean isKeyword(String token) {
    switch (token) {
      case "true":
      case "false":
      case "int":
      case "while":
      case "if":
      case "else":
      case "read":
      case "write":
        return true;
      default:
        return false;
    }
  }

  private String parseName() {
    if (isKeyword(tokens[from])) {
      return null;
    }
    if (tokens[from].length() == 0) {
      return null;
    }
    char first = tokens[from].charAt(0);
    if (!(first >= 'a' && first <= 'z') && !(first >= 'A' && first <= 'Z')) {
      return null;
    }
    for (int i = 1; i < tokens[from].length(); i++) {
      char next = tokens[from].charAt(i);
      if (!(next >= 'a' && next <= 'z') && !(next >= 'A' && next <= 'Z')
          && !(next >= '0' && next <= '9')) {
        return null;
      }
    }
    from++;
    return tokens[from - 1];
  }

  private int parseType() {
    if (tokens[from].equals("int")) {
      from++;
      return 1;
    }
    return -1;
  }

  private Declaration parseDecl() {
    if (parseType() < 0) {
      return null;
    }
    List<String> names = new ArrayList<>();
    while (true) {
      String name = parseName();
      if (name == null) {
        return null;
      }
      names.add(name);
      if (!tokens[from].equals(",")) {
        break;
      }
      from++;
    }
    if (tokens[from].equals(";")) {
      from++;
    } else {
      return null;
    }
    return new Declaration(names.toArray(new String[0]));
  }

  private Unop parseUnop() {
    if (tokens[from].equals("-")) {
      return Unop.Minus;
    }
    return null;
  }

  private Binop parseBinop() {
    switch (tokens[from]) {
      case "+":
        from++;
        return Binop.Plus;
      case "-":
        ;
        from++;
        return Binop.Minus;
      case "*":
        ;
        from++;
        return Binop.MultiplicationOperator;
      case "/":
        from++;
        return Binop.DivisionOperator;
      case "%":
        from++;
        return Binop.Modulo;
      default:
        return null;
    }
  }

  private Comp parseComp() {
    switch (tokens[from]) {
      case "==":
        from++;
        return Comp.Equals;
      case "!=":
        from++;
        return Comp.NotEquals;
      case "<=":
        from++;
        return Comp.LessEqual;
      case "<":
        from++;
        return Comp.Less;
      case ">=":
        from++;
        return Comp.GreaterEqual;
      case ">":
        from++;
        return Comp.Greater;
      default:
        return null;
    }
  }

  private Expression parseExpressionNoBinop() {
    // <number>
    Number number = parseNumber();
    if (number != null) {
      return number;
    }
    // <name>
    String name = parseName();
    if (name != null) {
      if (tokens[from].equals("(")) {
        from++;
        List<Expression> expressions = new ArrayList<>();
        while (!tokens[from].equals(")")) {
          Expression expr = parseExpression();
          if (expr == null) {
            return null;
          }
          expressions.add(expr);
          if (!tokens[from].equals(",")) {
            break;
          }
          from++;
        }
        return (tokens[from++].equals(")")) ?
            new Call(name, expressions.toArray(new Expression[0])) :
            null;
      } else {
        return new Variable(name);
      }
    }
    // (<expr>)
    if (tokens[from].equals("(")) {
      from++;
      Expression expr = parseExpression();
      if (expr == null) {
        return null;
      }
      if (tokens[from].equals(")")) {
        from++;
        return expr;
      } else {
        return null;
      }
    }
    // <unop> <expr>
    Unop unop = parseUnop();
    if (unop == null) {
      return null;
    }
    Expression expr = parseExpression();
    if (expr == null) {
      return null;
    }
    return new Unary(unop, expr);
  }

  private Expression parseExpression() {
    Expression expr = parseExpressionNoBinop();
    if (expr == null) {
      return null;
    }
    while (true) {
      Binop binop = parseBinop();
      if (binop == null) {
        break;
      }
      Expression right = parseExpressionNoBinop();
      if (right == null) {
        return null;
      }
      expr = new Binary(expr, binop, right);
    }
    return expr;
  }

  private Bbinop parseBbinop() {
    switch (tokens[from]) {
      case "&&":
        from++;
        return Bbinop.And;
      case "||":
        from++;
        return Bbinop.Or;
      default:
        return null;
    }
  }

  private Bunop parseBunop() {
    if (tokens[from].equals("!")) {
      from++;
      return Bunop.Not;
    }
    return null;
  }

  private Condition parseConditionNoBinary() {
    // true
    if (tokens[from].equals("true")) {
      return new True();
    }
    // false
    if (tokens[from].equals("false")) {
      return new False();
    }
    // (<cond>)
    if (tokens[from].equals("(")) {
      from++;
      Condition cond = parseCondition();
      if (cond == null) {
        return null;
      }
      if (tokens[from].equals(")")) {
        from++;
        return cond;
      } else {
        return null;
      }
    }
    Bunop bunop = parseBunop();
    if (bunop != null) {
      if (tokens[from].equals("(")) {
        from++;
        Condition cond = parseCondition();
        if (cond == null) {
          return null;
        }
        if (tokens[from].equals(")")) {
          from++;
          return new UnaryCondition(bunop, cond);
        } else {
          return null;
        }
      }
    }
    // <expr> <comp> <expr>
    Expression expr = parseExpression();
    if (expr == null) {
      return null;
    }
    Comp comp = parseComp();
    if (comp == null) {
      return null;
    }
    Expression right = parseExpression();
    if (right == null) {
      return null;
    }
    return new Comparison(expr, comp, right);
  }


  private Condition parseCondition() {
    Condition cond = parseConditionNoBinary();
    if (!valid()) {
      return null;
    }

    while (true) {
      Bbinop bbinop = parseBbinop();
      if (bbinop == null) {
        break;
      }
      Condition right = parseConditionNoBinary();
      if (right == null) {
        return null;
      }
      cond = new BinaryCondition(cond, bbinop, right);
    }
    return cond;
  }

  private Statement parseAssignment() {
    String name = parseName();
    if (name == null || !tokens[from].equals("=")) {
      return null;
    }
    from += 1;
    if (from + 2 < tokens.length
        && (tokens[from] + tokens[from + 1] + tokens[from + 2]).equals("read()")) {
      return new Read(name);
    }
    Expression expression = parseExpression();
    if (expression == null) {
      return null;
    }
    if (!tokens[from].equals(";")) {
      return null;
    }
    from++;
    return new Assignment(name, expression);
  }

  private Condition parseWhileIteCond() {
    if (tokens[from].equals("(")) {
      from++;
    } else {
      return null;
    }
    Condition cond = parseCondition();
    if (!valid() || !tokens[from].equals(")")) {
      return null;
    }
    from++;
    return cond;
  }

  private Statement parseIte() {
    if (!tokens[from].equals("if")) {
      return null;
    }
    from++;
    Condition cond = parseWhileIteCond();
    if (!valid()) {
      return null;
    }
    Statement then = parseStatement();
    if (!valid()) {
      return null;
    }
    Statement els = null;
    if (tokens[from].equals("else")) {
      from++;
      els = parseStatement();
    }
    return (els == null) ? new IfThen(cond, then) : new IfThenElse(cond, then, els);
  }

  private Statement parseStatement() {
    switch (tokens[from]) {
      case ";":
        from++;
        System.out.println("created empty statement. this is stupid");
        return new EmptyStatement();
      case "{":
        from++;
        List<Statement> statements = new ArrayList<>();
        while (true) {
          Statement stmt = parseStatement();
          if (stmt == null) {
            break;
          }
          statements.add(stmt);
        }
        if (tokens[from++].equals("}")) {
          return new Composite(statements.toArray(new Statement[0]));
        } else {
          return null;
        }
      case "while":
        from++;
        Condition cond = parseWhileIteCond();
        if (cond == null) {
          return null;
        }
        Statement block = parseStatement();
        return (block == null) ? null : new While(cond, block);
      case "if":
        return parseIte();
      case "write":
        if (!tokens[from + 1].equals("(")) {
          return null;
        }
        from += 2;
        Expression exp = parseExpression();
        if (!valid() ||
            !tokens[from].equals(")") ||
            !tokens[from + 1].equals(";")) {
          return null;
        }
        return new Write(exp);
      case "return":
        from++;
        Expression expr = parseExpression();
        if (expr == null || !tokens[from++].equals(";")) {
          return null;
        }
        return new Return(expr);
      default:
        return parseAssignment();
    }
  }
}
