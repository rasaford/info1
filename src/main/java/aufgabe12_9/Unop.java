package aufgabe12_9;

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
