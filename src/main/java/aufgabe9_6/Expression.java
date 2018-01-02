package aufgabe9_6;

public class Expression {

  class Variable {

    private String name;
  }

  class Number {

    private int value;
  }

  class Binary {

    private Expression lhs;
    private Binop operator;
    private Expression rhs;
  }

  class Unary {

    private Unop operator;
    private Expression operand;
  }

  class Call {

    private String functionName;
    private Expression[] arguments;
  }
}
