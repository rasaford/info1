package aufgabe11_7;

import static aufgabe11_7.SteckOperation.ADD;
import static aufgabe11_7.SteckOperation.NOP;
import static java.lang.Enum.valueOf;

import java.util.Hashtable;
import java.util.Scanner;

public class Interpreter extends MiniJava {

  public static SteckOperation getOperationByString(String instruction) {
    try {
      return SteckOperation.valueOf(instruction);
    } catch (IllegalArgumentException iax) {
      error("Invalid instruction: " + instruction);
      return ADD; // Never reached
    }
  }

  public static String readProgramConsole() {
    @SuppressWarnings("resource")
    Scanner sin = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (true) {
      String nextLine = sin.nextLine();
      if (nextLine.equals("")) {
        nextLine = sin.nextLine();
        if (nextLine.equals("")) {
          break;
        }
      }
      if (nextLine.startsWith("//")) {
        continue;
      }
      builder.append(nextLine);
      builder.append('\n');
    }
    return builder.toString();
  }

  public static String programToString(int[] program) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < program.length; i++) {
      if (i > 0) {
        sb.append('\n');
      }
      sb.append(i + ": ");
      sb.append(SteckInstruction.decode(program[i]));
    }
    return sb.toString();
  }

  public static int true_() {
    return -1;
  }

  public static int false_() {
    return 0;
  }

  static void error(String message) {
    throw new RuntimeException(message);
  }

  private int[] stack = new int[2048];
  private int stackPointer = -1;

  public int pop() {
    if (stackPointer < 0 || stackPointer >= stack.length) {
      error("Invalid stack access");
    }
    return stack[stackPointer--];
  }

  public void push(int value) {
    stackPointer++;
    if (stackPointer < 0 || stackPointer >= stack.length) {
      error(String.format("Invalid stack access %d", stackPointer));
    }
    stack[stackPointer] = value;
  }

  private int[] heap = new int[128];
  private int heapFreeSpace = 127;

  private void initializeHeap() {
    heap[heap.length - 1] = heap.length - 1;
  }

  public static int execute(int[] program) {
    return new Interpreter().executeHelp(program);
  }

  private int executeHelp(int[] program) {
    initializeHeap();
    int programCounter = 0;
    int framePointer = 0;
    execution:
    while (true) {
      SteckInstruction insn = SteckInstruction.decode(program[programCounter]);
      if (insn == null) {
        error("Invalid instruction at " + programCounter + ": " + program[programCounter] + ".");
      }
      int parameter = insn.getImmediate();
      programCounter++;
      switch (insn.getOperation()) {
        case NOP:
          break;
        case ADD: {
          int a = pop();
          int b = pop();
          push(a + b);
          break;
        }
        case SUB: {
          int a = pop();
          int b = pop();
          push(a - b);
          break;
        }
        case MUL: {
          int a = pop();
          int b = pop();
          push(a * b);
          break;
        }
        case MOD: {
          int a = pop();
          int b = pop();
          push(a % b);
          break;
        }
        case DIV: {
          int a = pop();
          int b = pop();
          push(a / b);
          break;
        }
        case AND: {
          int a = pop();
          int b = pop();
          push(a & b);
          break;
        }
        case OR: {
          int a = pop();
          int b = pop();
          push(a | b);
          break;
        }
        case NOT: {
          int a = pop();
          push(~a);
          break;
        }
        case SHL: {
          int a = pop();
          push(a << parameter);
          break;
        }
        case LDI: {
          push(parameter & 0xffff);
          break;
        }
        case LDS: {
          int stackAddress = framePointer + parameter;
          if (stackAddress < 0 || stackAddress >= stack.length) {
            error("Invalid stack access");
          }
          push(stack[stackAddress]);
          break;
        }
        case STS: {
          int stackAddress = framePointer + parameter;
          if (stackAddress < 0 || stackAddress >= stack.length) {
            error("Invalid stack access");
          }
          int value = pop();
          stack[stackAddress] = value;
          break;
        }
        case JUMP: {
          if (parameter < 0 || parameter > program.length) {
            error("Invalid jump destionation address");
          }
          int cond = pop();
          if (cond == -1) {
            programCounter = parameter;
          }
          break;
        }
        case EQ: {
          int a = pop();
          int b = pop();
          if (a == b) {
            push(true_());
          } else {
            push(false_());
          }
          break;
        }
        case LT: {
          int a = pop();
          int b = pop();
          if (a < b) {
            push(true_());
          } else {
            push(0);
          }
          break;
        }
        case LE: {
          int a = pop();
          int b = pop();
          if (a <= b) {
            push(true_());
          } else {
            push(false_());
          }
          break;
        }
        case IN: {
          int value = read();
          push(value);
          break;
        }
        case OUT: {
          int value = pop();
          write(value);
          break;
        }
        case CALL: {
          if (parameter < 0) {
            error("Invalid function argument count");
          }
          int functionAddress = pop();
          if (functionAddress < 0 || functionAddress > program.length) {
            error("Invalid function address");
          }
          int[] arguments = new int[parameter];
          for (int i = 0; i < arguments.length; i++) {
            arguments[i] = pop();
          }
          push(framePointer);
          push(programCounter);
          for (int i = 0; i < arguments.length; i++) {
            push(arguments[arguments.length - 1 - i]);
          }
          framePointer = stackPointer;
          programCounter = functionAddress;
          break;
        }
        case RETURN: {
          if (parameter < 0) {
            error("Invalid stack frame size");
          }
          int retVal = pop();
          for (int i = 0; i < parameter; i++) {
            pop();
          }
          programCounter = pop();
          if (programCounter < 0 || programCounter >= program.length) {
            error("Invalid return address on stack; the stack has been destroyed by the program.");
          }
          framePointer = pop();
          if (framePointer < -1 || framePointer >= stack.length) {
            error("Invalid frame pointer on stack; the stack has been destroyed by the program.");
          }
          push(retVal);
          break;
        }
        case HALT: {
          break execution;
        }
        case ALLOC: {
          if (parameter < 0) {
            error("Invalid stack allocation");
          }
          stackPointer += parameter;
          break;
        }
        case LDH: {
          int heapPointer = pop();
          int heapOffset = pop();
          if (heapPointer < 0 || heapPointer > heap.length) {
            error("heap pointer out of bounds");
          }
          int header = heap[heapPointer];
          int from = header & 0xFFFF;
          int to = header >>> 16;
          heapOffset = heapOffset == 0xFFFF ? -1 : heapOffset;
          if (heapOffset > to - from || heapOffset < -1) {
            error("heap offset out of bounds");
          } else {
            push(heap[from + heapOffset + 1]);
          }
          break;
        }
        case STH: {
          int heapPointer = pop();
          int heapOffset = pop();
          int value = pop();
          if (heapPointer < 0 || heapPointer > heap.length) {
            error("heap pointer out of bounds");
          }
          int header = heap[heapPointer];
          int from = header & 0xFFFF;
          int to = header >>> 16;
          if (heapOffset > to - from) {
            error("heap offset out of bounds");
          }
          heap[from + heapOffset + 1] = value;
          break;
        }
        case ALLOCH: {
          int size = pop();
          final int arrayHeaderSize = 1;
          if (size <= 0) {
            error("invalid heap allocation size");
          }
          size += arrayHeaderSize;
          if (heapFreeSpace - size - 1 < 0) {
            error("heap is full! Cannot allocate more space");
          }
          int prevHeader = heap[heap.length - 1];
          int prevTo = prevHeader >>> 16;

          int from = prevTo;
          int to = from + size;
          heap[heap.length - 1]--;
          // write header
          heap[heap[heap.length - 1]] = (to << 16) | (from & 0xFFFF);
          // write array length
          heap[from] = size - arrayHeaderSize;
          heapFreeSpace -= size + 1;
          push(heap[heap.length - 1]);
          break;
        }
      }
    }
    int retVal = pop();
    if (stackPointer >= 0) {
      error("Stack is tainted" + stackPointer);
    }
    return retVal;
  }

  public static int[] parse(String textProgram) {
    String[] lines = textProgram.split("\n");
    int[] program = new int[lines.length];
    // Man kann auch z.B. ein mehrdimensionales String-Array verwenden,
    // das ist aber mühsam und unschön, daher hier die Hashtabelle
    Hashtable<String, Integer> labels = new Hashtable<>();
    for (int i = 0; i < lines.length; i++) {
      String trimmed = lines[i].trim().replaceAll("\\s+", " ");
      if (trimmed.endsWith(":")) {
        labels.put(trimmed.substring(0, trimmed.length() - 1), i);
        continue;
      }
    }
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].length() == 0) {
        program[i] = NOP.encode();
        continue;
      }
      String trimmed = lines[i].trim().replaceAll("\\s+", " ");
      if (trimmed.endsWith(":")) {
        program[i] = NOP.encode();
        continue;
      }
      String[] cmdParam = trimmed.split(" ");
      if (cmdParam.length > 2) {
        error("Unable to parse input");
      }
      int parameter = 0;
      if (cmdParam.length > 1) {
        // Es gibt einen Parameter
        if (labels.containsKey(cmdParam[1]))
        // Der Parameter wurde irgendwo als Label definiert
        {
          parameter = labels.get(cmdParam[1]);
        } else
        // Der Parameter wurde nicht als Label definiert, ist also eine Zahl
        {
          parameter = Integer.parseInt(cmdParam[1]);
        }
      }
      program[i] = getOperationByString(cmdParam[0]).encode();
      if (parameter < Short.MIN_VALUE || parameter > Short.MAX_VALUE) {
        error("Parameter out of range");
      }
      program[i] |= parameter & 0xffff;
    }
    return program;
  }
}
