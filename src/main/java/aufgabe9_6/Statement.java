package aufgabe9_6;


import javax.swing.plaf.nimbus.State;

public class Statement {

  class Assignment extends Statement {

    private String name;
    private Expression expression;

    public Assignment(String name, Expression expression) {
      this.name = name;
      this.expression = expression;
    }

    public String getName() {
      return name;
    }

    public Expression getExpression() {
      return expression;
    }
  }

  class Composite extends Statement {

    private Statement[] statements;

    public Composite(Statement[] statements) {
      this.statements = statements;
    }

    public Statement[] getStatements() {
      return statements;
    }
  }

  class IfThen extends Statement {

    private Condition cond;
    private Statement thenBranch;

    public IfThen(Condition cond, Statement thenBranch) {
      this.cond = cond;
      this.thenBranch = thenBranch;
    }

    public Condition getCond() {
      return cond;
    }

    public Statement getThenBranch() {
      return thenBranch;
    }
  }

  class IfThenElse extends Statement {

    private Condition cond;
    private Statement thenBranch;
    private Statement elseBranch;

    public IfThenElse(Condition cond, Statement thenBranch, Statement elseBranch) {
      this.cond = cond;
      this.thenBranch = thenBranch;
      this.elseBranch = elseBranch;
    }

    public Condition getCond() {
      return cond;
    }

    public Statement getThenBranch() {
      return thenBranch;
    }

    public Statement getElseBranch() {
      return elseBranch;
    }
  }

  class While extends Statement {

    private Condition cond;
    private Statement body;

    public While(Condition cond, Statement body) {
      this.cond = cond;
      this.body = body;
    }

    public Condition getCond() {
      return cond;
    }

    public Statement getBody() {
      return body;
    }
  }

  class Read extends Statement{

    private String name;

    public Read(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

}
