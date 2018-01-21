package aufgabe11_7;

public class Compiler {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  public static int[] compile(String code) {
    String[] tokens = Parser.lex(code);
    Program syntaxTree = new Parser(tokens).parse();
    if (syntaxTree == null) {
      throw new RuntimeException("code could not be parsed");
    }
    CodeGenerationVisitor visitor = new CodeGenerationVisitor();
    visitor.visit(syntaxTree);
    return visitor.getProgram();
  }
}
