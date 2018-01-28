package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public enum Bbinop {
  And, Or;

  String codeString() {
    switch (this) {
      case And:
        return "&&";
      case Or:
        return "||";
    }
    throw new RuntimeException("Unreachable");
  }
}
