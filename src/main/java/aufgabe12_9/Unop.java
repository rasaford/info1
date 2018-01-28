package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public enum Unop {
  Minus;

  String codeString() {
    switch (this) {
      case Minus:
        return "-";
    }
    throw new RuntimeException("Unreachable");
  }
}
