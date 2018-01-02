package aufgabe9_6;


public class Condition {


  class True {

  }

  class False {

  }


  class BinaryCondition {

    private Condition lhs;
    private Bbinop operator;
    private Condition rhs;
  }

  class Comparison {

    private Expression lhs;
    private Comp operator;
    private Expression rhs;
  }

  class UnaryCondition {

    private Bunop operator;
    private Condition operand;
  }
}
