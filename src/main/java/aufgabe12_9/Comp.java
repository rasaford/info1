package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public enum Comp {
  Equals, NotEquals, LessEqual, Less, GreaterEqual, Greater;

  String codeString() {
    switch (this) {
      case Equals:
        return "==";
      case Greater:
        return ">";
      case GreaterEqual:
        return ">=";
      case Less:
        return "<";
      case LessEqual:
        return "<=";
      case NotEquals:
        return "!=";
    }
    throw new RuntimeException("Unreachable");
  }
}
