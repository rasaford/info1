package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class SteckInstruction {

  private SteckOperation operation;
  private int immediate;

  public SteckInstruction(SteckOperation operation, int immediate) {
    this.operation = operation;
    this.immediate = immediate;
  }

  private static int extractImmediateValue(SteckOperation operation, int encodedInstruction) {
    int immediate = encodedInstruction & 0xffff;
    switch (operation.getImmediateType()) {
      case Signed:
        if ((immediate & 0x8000) != 0) {
          immediate |= 0xffff0000;
        }
        return immediate;
      case Unsigned:
        return immediate;
      case None:
        return 0;
    }
    return 0;
  }

  public static SteckInstruction decode(int encodedInstruction) {
    int opCode = encodedInstruction >>> 16;
    SteckOperation operation = SteckOperation.getOperationbyOpcode(opCode);
    if (operation == null) {
      return null;
    }
    int immediate = extractImmediateValue(operation, encodedInstruction);
    return new SteckInstruction(operation, immediate);
  }

  public SteckOperation getOperation() {
    return operation;
  }

  public int getImmediate() {
    return immediate;
  }

  @Override
  public String toString() {
    String result = operation.toString();
    if (operation.getImmediateType() != ImmediateType.None) {
      result += " " + immediate;
    }
    return result;
  }
}
