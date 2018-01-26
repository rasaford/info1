package aufgabe12_9;

public enum SteckOperation {
  NOP(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  ADD(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  SUB(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  MUL(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  MOD(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  DIV(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  AND(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  OR(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  NOT(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  SHL(OpCodeCounter.opCodeCounter++, ImmediateType.Unsigned),
  LDI(OpCodeCounter.opCodeCounter++, ImmediateType.Unsigned),
  LDS(OpCodeCounter.opCodeCounter++, ImmediateType.Signed),
  STS(OpCodeCounter.opCodeCounter++, ImmediateType.Signed),
  LDH(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  STH(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  JUMP(OpCodeCounter.opCodeCounter++, ImmediateType.Unsigned),
  EQ(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  LT(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  LE(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  IN(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  OUT(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  CALL(OpCodeCounter.opCodeCounter++, ImmediateType.Unsigned),
  RETURN(OpCodeCounter.opCodeCounter++, ImmediateType.Unsigned),
  HALT(OpCodeCounter.opCodeCounter++, ImmediateType.None),
  ALLOC(OpCodeCounter.opCodeCounter++, ImmediateType.Unsigned),
  ALLOCH(OpCodeCounter.opCodeCounter++, ImmediateType.None);

  private static SteckOperation[] operationByOpcode;
  private int opCode;
  private ImmediateType immediateType;

  private SteckOperation(int opCode, ImmediateType immediateType) {
    this.opCode = opCode;
    this.immediateType = immediateType;
  }

  public static SteckOperation getOperationbyOpcode(int opCode) {
    if (operationByOpcode == null) {
      SteckOperation[] values = SteckOperation.values();
      operationByOpcode = new SteckOperation[values.length];
      for (int i = 0; i < values.length; i++) {
        SteckOperation insn = values[i];
        operationByOpcode[insn.getOpCode()] = insn;
      }
    }
    if (opCode < 0 || opCode >= operationByOpcode.length) {
      return null;
    } else {
      return operationByOpcode[opCode];
    }
  }

  public int getOpCode() {
    return opCode;
  }

  public ImmediateType getImmediateType() {
    return immediateType;
  }

  public int encode(int immediate) {
    return (getOpCode() << 16) | (immediate & 0xffff);
  }

  public int encode() {
    return (getOpCode() << 16);
  }
}

class OpCodeCounter {

  public static int opCodeCounter = 0;
}
