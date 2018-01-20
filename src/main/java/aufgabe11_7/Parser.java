package aufgabe11_7;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;

public class Parser {

  private static final int PADDING = 3;

  public static String[] lex(String input) {
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
      while (index < input.length() && tokenBuilder.length() < 2) {
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

  private String[] tokens;

  private int from;

  public Parser(String[] tokens) {
    this.tokens = tokens;
  }

  public Number parseNumber(String[] program) {
    for (int i = 0; i < program[from].length(); i++) {
      char next = program[from].charAt(i);
      if (!(next >= '0' && next <= '9')) {
        return null;
      }
    }
    return new Number(Integer.parseInt(program[from++]));
  }

  private static boolean isKeyword(String token) {
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
    return tokens[from++];
  }

  private Type parseType() {
    if (tokens[from++].equals("int")) {
      if (tokens[from].equals("[") && tokens[from + 1].equals("]")) {
        from += 2;
        return Type.IntegerArray;
      }
      return Type.Integer;
    } else {
      return null;
    }
  }

  private Declaration parseDecl() {
    Type type = parseType();
    if (type == null) {
      return null;
    }

    ArrayList<String> variables = new ArrayList<>();
    while (true) {
      String name = parseName();
      if (name == null) {
        return null;
      }
      variables.add(name);
      if (!tokens[from].equals(",")) {
        break;
      }
      from++;
    }
    if (tokens[from].equals(";")) {
      from += 1;
      return new Declaration(type, variables.toArray(new String[]{}));
    } else {
      return null;
    }
  }

  private Unop parseUnop() {
    if (tokens[from].equals("-")) {
      from = from + 1;
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
        from++;
        return Binop.Minus;
      case "*":
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
    // new int [<expr>]
    if (tokens[from].equals("new") &&
        tokens[from + 1].equals("int") &&
        tokens[from + 2].equals("[")) {
      from += 3;
      Expression size = parseExpression();
      if (size == null || !tokens[from++].equals("]")) {
        return null;
      }
      return new ArrayInitializer(size);
    }
    // length(<expr>)
    if (tokens[from].equals("length") && tokens[from + 1].equals("(")) {
      from += 2;
      Expression array = parseExpression();
      if (array == null || !tokens[from].equals(")")) {
        return null;
      }
      from++;
      return new ArrayLength(array);
    }
    // <number>
    int before = from;
    Expression number = parseNumber(tokens);
    if (number != null) {
      return number;
    }
    // <name>( ... )
    from = before;
    String functionName = parseName();
    if (functionName != null && tokens[from].equals("(")) {
      from++;
      ArrayList<Expression> arguments = new ArrayList<>();
      if (!tokens[from].equals(")")) {
        argumentLoop:
        while (true) {
          Expression nextArgument = parseExpression();
          if (nextArgument == null) {
            return null;
          }
          arguments.add(nextArgument);
          switch (tokens[from]) {
            case ",":
              from++;
              continue;
            case ")":
              break argumentLoop;
            default:
              return null;
          }
        }
      }
      if (!tokens[from++].equals(")")) {
        return null;
      }
      return new Call(functionName, arguments.toArray(new Expression[]{}));
    }
    // <name>
    from = before;
    String name = parseName();
    if (name != null) {
      return new Variable(name);
    }
    // (<expr>)
    from = before;
    if (tokens[from].equals("(")) {
      from += 1;
      Expression sub = parseExpression();
      if (sub == null) {
        return null;
      }
      if (tokens[from].equals(")")) {
        from = from + 1;
        return sub;
      } else {
        return null;
      }
    }
    // <unop> <expr>
    from = before;
    Unop unop = parseUnop();
    if (unop != null) {
      Expression sub = parseExpression();
      if (sub != null) {
        return new Unary(unop, sub);
      } else {
        return null;
      }
    }
    return null;
  }

  private Expression parseExpression() {
    Expression current = parseExpressionNoBinop();
    if (current == null) {
      return null;
    }

    while (true) {
      int before = from;
      Binop binop = parseBinop();
      if (binop == null) {
        from = before;
        break;
      }
      Expression next = parseExpressionNoBinop();
      if (next == null) {
        return null;
      }
      current = new Binary(current, binop, next);
    }
    if (tokens[from].equals("[")) {
      from++;
      Expression index = parseExpression();
      if (index == null || !tokens[from].equals("]")) {
        return null;
      }
      from++;
      return new ArrayAccess(current, index);
    }
    return current;
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
    if (tokens[from++].equals("!")) {
      return Bunop.Not;
    }
    return null;
  }

  private Condition parseConditionNoBinary() {
    // true
    if (tokens[from].equals("true")) {
      from++;
      return new True();
    }
    // false
    if (tokens[from].equals("false")) {
      from++;
      return new False();
    }
    // <expr> <comp> <expr>
    int before = from;
    Expression lhs = parseExpression();
    if (lhs != null) {
      Comp comp = parseComp();
      if (comp == null) {
        return null;
      }
      Expression rhs = parseExpression();
      if (rhs == null) {
        return null;
      }
      return new Comparison(lhs, comp, rhs);
    }
    // (<cond>)
    from = before;
    if (tokens[from].equals("(")) {
      from += 1;
      Condition sub = parseCondition();
      if (sub == null) {
        return null;
      }
      if (tokens[from].equals(")")) {
        from++;
        return sub;
      } else {
        return null;
      }
    }
    // <bunop> ( <cond> )
    from = before;
    Bunop bunop = parseBunop();
    if (bunop != null) {
      if (tokens[from].equals("(")) {
        from += 1;
        Condition sub = parseCondition();
        if (sub == null) {
          return null;
        }
        if (tokens[from].equals(")")) {
          from++;
          return new UnaryCondition(bunop, sub);
        } else {
          return null;
        }
      }
    }
    return null;
  }

  public Condition parseCondition() {
    Condition current = parseConditionNoBinary();
    if (current == null) {
      return null;
    }

    while (true) {
      int before = from;
      Bbinop bbinop = parseBbinop();
      if (bbinop == null) {
        from = before;
        break;
      }
      Condition rhs = parseConditionNoBinary();
      if (rhs == null) {
        return null;
      }
      current = new BinaryCondition(current, bbinop, rhs);
    }
    return current;
  }

  private Statement parseAssigment() {
    String variableName = parseName();
    if (variableName == null) {
      return null;
    }
    // <name> [<expr>] = <expr>;
    if (tokens[from].equals("[")) {
      from++;
      Expression index = parseExpression();
      if (index == null || !tokens[from].equals("]") || !tokens[from + 1].equals("=")) {
        return null;
      }
      from += 2;
      Expression expression = parseExpression();
      if (expression == null) {
        return null;
      }
      return new ArrayAssignment(variableName, index, expression);
    }
    if (!tokens[from].equals("=")) {
      return null;
    }
    from += 1;
    if (from + 2 < tokens.length
        && (tokens[from] + tokens[from + 1] + tokens[from + 2]).equals("read()")) {
      from = from + 3;
      return new Read(variableName);
    }
    Expression exp = parseExpression();
    if (exp == null) {
      return null;
    }
    return new Assignment(variableName, exp);
  }

  private Condition parseWhileIteCond() {
    if (tokens[from].equals("(")) {
      from++;
    } else {
      return null;
    }
    Condition cond = parseCondition();
    if (cond == null) {
      return null;
    }
    if (!tokens[from].equals(")")) {
      return null;
    }
    from = from + 1;
    return cond;
  }

  private Statement parseIte() {
    if (!tokens[from].equals("if")) {
      return null;
    }
    from++;
    Condition cond = parseWhileIteCond();
    if (cond == null) {
      return null;
    }
    Statement thenBranch = parseStatement();
    if (thenBranch == null) {
      return null;
    }
    if (from < tokens.length && tokens[from].equals("else")) {
      from++;
      Statement elseBranch = parseStatement();
      if (elseBranch == null) {
        return null;
      }
      return new IfThenElse(cond, thenBranch, elseBranch);
    } else {
      return new IfThen(cond, thenBranch);
    }
  }

  private Statement parseStatement() {
    switch (tokens[from]) {
      case ";": {
        from = from + 1;
        return new Composite(new Statement[]{});
      }
      case "{": {
        ArrayList<Statement> statements = new ArrayList<Statement>();
        from++;
        while (true) {
          int before = from;
          Statement next = parseStatement();
          if (next == null) {
            from = before;
            break;
          }
          statements.add(next);
        }
        if (tokens[from].equals("}")) {
          from = from + 1;
          return new Composite(statements.toArray(new Statement[]{}));
        } else {
          return null;
        }
      }
      case "while": {
        from++;
        Condition cond = parseWhileIteCond();
        if (cond == null) {
          return null;
        }
        Statement body = parseStatement();
        if (body == null) {
          return null;
        }
        return new While(cond, body);
      }
      case "if": {
        Statement ite = parseIte();
        if (ite == null) {
          return null;
        }
        return ite;
      }
      case "write": {
        if (!tokens[from + 1].equals("(")) {
          return null;
        }
        from += 2;
        Expression exp = parseExpression();
        if (exp == null || !tokens[from].equals(")")) {
          return null;
        }
        if (!tokens[from + 1].equals(";")) {
          return null;
        }
        from = from + 2;
        return new Write(exp);
      }
      case "return": {
        from++;
        Expression exp = parseExpression();
        if (exp == null) {
          return null;
        }
        if (!tokens[from++].equals(";")) {
          return null;
        }
        return new Return(exp);
      }
      default: {
        Statement ass = parseAssigment();
        if (ass != null) {
          if (tokens[from].equals(";")) {
            from += 1;
            return ass;
          } else {
            return null;
          }
        }
        return null;
      }
    }
  }

  private List<Pair<Type, String>> parseFunctionParameterList() {
    if (!tokens[from++].equals("(")) {
      return null;
    }
    ArrayList<Pair<Type, String>> parameters = new ArrayList<>();
    if (!tokens[from].equals(")")) {
      do {
        Type type = parseType();
        if (type == null) {
          return null;
        }
        String parameter = parseName();
        if (parameter == null) {
          return null;
        }
        parameters.add(new Pair<>(type, parameter));
      } while (tokens[from++].equals(","));
      from--;
    }
    if (!tokens[from++].equals(")")) {
      return null;
    }
    return parameters;
  }

  private Function parseFunction() {
    Type type = parseType();
    if (type == null) {
      return null;
    }
    String functionName = parseName();
    if (functionName == null) {
      return null;
    }
    List<Pair<Type, String>> param = parseFunctionParameterList();
    if (param == null) {
      return null;
    }
    List<Type> types = new ArrayList<>();
    List<String> names = new ArrayList<>();
    param.forEach(p -> {
      types.add(p.getA());
      names.add(p.getB());
    });
    if (!tokens[from++].equals("{")) {
      return null;
    }
    ArrayList<Declaration> declarations = new ArrayList<>();
    while (true) {
      int before = from;
      Declaration decl = parseDecl();
      if (decl == null) {
        from = before;
        break;
      }
      declarations.add(decl);
    }
    ArrayList<Statement> statements = new ArrayList<>();
    while (true) {
      int before = from;
      Statement stmt = parseStatement();
      if (stmt == null) {
        from = before;
        break;
      }
      statements.add(stmt);
    }
    if (!tokens[from++].equals("}")) {
      return null;
    }
    return new Function(functionName,
        names.toArray(new String[0]),
        types.toArray(new Type[0]),
        declarations.toArray(new Declaration[]{}),
        statements.toArray(new Statement[]{}));
  }

  private Program parseProgram() {
    ArrayList<Function> functions = new ArrayList<>();
    int before = from;
    while (true) {
      before = from;
      Function function = parseFunction();
      if (function == null) {
        from = before;
        break;
      } else {
        functions.add(function);
      }
    }
    return new Program(functions.toArray(new Function[]{}));
  }

  public Program parse() {
    from = 0;
    Program program = parseProgram();
    // Consume padding
    while (from < this.tokens.length && this.tokens[from].length() == 0) {
      from++;
    }
    if (from == this.tokens.length) {
      return program;
    }
    return null;
  }


  private class Pair<A, B> {

    private A a;
    private B b;

    public Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }

    public A getA() {
      return a;
    }

    public B getB() {
      return b;
    }
  }
}

