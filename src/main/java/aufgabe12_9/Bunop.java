package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public enum Bunop {
  Not;

  String codeString() {
    switch (this) {
      case Not:
        return "!";
    }
    throw new RuntimeException("Unreachable");
  }
}
