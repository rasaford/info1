package aufgabe12_9;

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
