package aufgabe10_7;

import aufgabe10_7.codegen.CodeGenerationVisitor;
import aufgabe10_7.codegen.Program;

public class Compiler {

  public static int[] compile(String code) {
    Program syntaxTree = new MiniJavaParser(code).parse();
    if (syntaxTree == null) {
      throw new RuntimeException("code could not be parsed");
    }
    CodeGenerationVisitor visitor = new CodeGenerationVisitor();
    visitor.visit(syntaxTree);
    return visitor.getProgram();
  }
}
