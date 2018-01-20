package aufgabe11_7;

public class TailCallOptimization {

  public static void optimize(int[] program) {
    // detect all functions
    int functionStart = 0;
    for (int i = 0; i < program.length - 2; i++) {
      SteckInstruction current = SteckInstruction.decode(program[i]);
      SteckInstruction next = SteckInstruction.decode(program[i + 1]);
      SteckInstruction after = SteckInstruction.decode(program[i + 2]);

      if (current.getOperation().equals(SteckOperation.NOP)
          && current.getImmediate() == 0xABCD) {
        functionStart = i;
      }
      // if tail recursion
      if (current.getOperation().equals(SteckOperation.LDI)
          && next.getOperation().equals(SteckOperation.CALL)
          && current.getImmediate() == functionStart
          && after.getOperation().equals(SteckOperation.RETURN)) {
        System.out.println("tail recursion " + (i + 1));
        optimizeCall(program, i, functionStart, next.getImmediate());
      }
    }
    System.out.println(Interpreter.programToString(program));
  }

  private static void optimizeCall(int[] program, int startIndex, int functionIndex, int argCount) {
    int parameterIndex = 0;
    // overwrite function parameters
    for (int i = 0; i < argCount; i++) {
      program[startIndex + i] = SteckOperation.STS.encode(parameterIndex--);
    }
    program[startIndex + argCount] = SteckOperation.LDI.encode(0);
    program[startIndex + argCount + 1] = SteckOperation.NOT.encode(0);
    program[startIndex + argCount + 2] = SteckOperation.JUMP.encode(functionIndex);
    program[startIndex + argCount + 3] = SteckOperation.RETURN.encode(argCount);
  }
}
