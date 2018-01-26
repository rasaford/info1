package aufgabe12_9;

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
