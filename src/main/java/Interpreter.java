import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Interpreter extends MiniJava {

  public static final int NOP = 0;
  public static final int ADD = 1;
  public static final int SUB = 2;
  public static final int MUL = 3;
  public static final int MOD = 4;
  public static final int LDI = 5;
  public static final int LDS = 6;
  public static final int STS = 7;
  public static final int JUMP = 8;
  public static final int JE = 9;
  public static final int JNE = 10;
  public static final int JLT = 11;
  public static final int IN = 12;
  public static final int OUT = 13;
  public static final int CALL = 14;
  public static final int RETURN = 15;
  public static final int HALT = 16;
  public static final int ALLOC = 17;
  private static Map<String, Token> tokens;

  private static int[] stack;
  // frame pointer
  private static int ebp;
  // stack pointer
  private static int esp;
  // instruction pointer
  private static int eip;

  public static void main(String[] args) {
    init();
  }

  public static void init() {
    stack = new int[128];
    esp = -1;
    ebp = 0;
    eip = 0;
    tokens = new HashMap<>();
    tokens.put("NOP", new Token(NOP, false, (imm) -> {
    }));
    tokens.put("ADD", new Token(ADD, false, (imm) -> push(pop() + pop())));
    tokens.put("SUB", new Token(SUB, false, (imm) -> push(pop() - pop())));
    tokens.put("MUL", new Token(MUL, false, (imm) -> push(pop() * pop())));
    tokens.put("MOD", new Token(MOD, false, (imm) -> push(pop() % pop())));
    tokens.put("LDI", new Token(LDI, true, Interpreter::push));
    tokens.put("LDS", new Token(LDS, true, (imm) -> push(stack[ebp + imm])));
    tokens.put("STS", new Token(STS, true, (imm) -> stack[ebp + imm] = pop()));
    tokens.put("JUMP", new Token(JUMP, true, (imm) -> eip = imm));
    tokens.put("JE", new Token(JE, true, (imm) -> eip = pop() == pop() ? imm : eip));
    tokens.put("JNE", new Token(JNE, true, (imm) -> eip = pop() != pop() ? imm : eip));
    tokens.put("JLT", new Token(JLT, true, (imm) -> eip = pop() < pop() ? imm : eip));
    tokens.put("CALL", new Token(CALL, true, (imm) -> {
      int address = pop();
      int[] args = new int[imm];
      for (int i = 0; i < imm; i++) {
        args[i] = pop();
      }
      push(ebp);
      push(eip);
      for (int i = args.length - 1; i >= 0; i--) {
        push(args[i]);
      }
      ebp = esp;
      eip = address;
    }));
    tokens.put("RETURN", new Token(RETURN, true, (imm) -> {
      int ret = pop();
      for (int i = 0; i < imm; i++) {
        pop();
      }
      eip = pop();
      ebp = pop();
      push(ret);
    }));
    tokens.put("IN", new Token(IN, false, (imm) -> push(read())));
    tokens.put("OUT", new Token(OUT, false, (imm) -> System.out.println(pop())));
    tokens.put("HALT", new Token(HALT, false, (imm) -> eip = -1));
    tokens.put("ALLOC", new Token(ALLOC, true, (imm) -> {
      ebp = esp;
      esp += imm;
    }));
  }


  public static int[] parse(String textProgram) {
    List<Integer> parse = new ArrayList<>();
    String[] lines = textProgram.toUpperCase().split("\n");
    Map<String, Integer> labels = resolveLabels(lines);

    for (int line = 0; line < lines.length; line++) {
      String[] command = lines[line].split("\\s");
      if (command.length == 0 || command.length > 2) {
        errorf("cannot parse line %d", line);
      }
      String op = command[0];
      if (isLabel(op) || op.trim().isEmpty() || op.startsWith("//")) {
        parse.add(0);
      } else {
        parse.add(parseOp(labels, command, line));
      }
    }
    return toArray(parse);
  }

  private static boolean isLabel(String line) {
    return line.matches("\\w+:");
  }

  private static Map<String, Integer> resolveLabels(String[] program) {
    Map<String, Integer> labels = new HashMap<>();
    for (int i = 0; i < program.length; i++) {
      String command = program[i].trim();
      if (isLabel(command)) {
        labels.put(command.substring(0, command.indexOf(":")),
            i);
      }
    }
    return labels;
  }

  private static int parseOp(Map<String, Integer> labels, String[] command, int line) {
    String op = command[0];
    if (!tokens.containsKey(op)) {
      errorf("unknown Token %s", op);
    }
    Token token = tokens.get(op);
    if (!token.immediate) {
      return token.opcode << 16;
    }
    if (command.length != 2) {
      errorf("cannot parse line %d: Instruction %s requires an immediate", line, op);
    }
    String imm = command[1];
    int immediate = 0;
    try {
      immediate = labels.containsKey(imm) ? labels.get(imm) : Short.parseShort(imm);
    } catch (NumberFormatException e) {
      errorf("cannot parse immediate %s", imm);
    }
    return (int) token.opcode << 16 | immediate & 0xFFFF;
  }


  private static int[] toArray(List<Integer> list) {
    int[] out = new int[list.size()];
    int index = 0;
    for (Integer i : list) {
      out[index++] = i;
    }
    return out;
  }

  public static int pop() {
    return stack[esp--];
  }

  public static void push(int value) {
    stack[++esp] = value;
  }

  public static int execute(int[] code) {
    for (eip = 0; eip < code.length; eip++) {
      short opcode = (short) (code[eip] >> 16);
      short immediate = (short) (code[eip] & 0xFFFF);

      for (Map.Entry<String, Token> t : tokens.entrySet()) {
        Token token = t.getValue();
        if (token.opcode == opcode) {
          token.function.accept(immediate);
          break;
        }
      }
      if (eip == -1) { // has the execution been halted?
        return pop();
      }
    }
    return pop();
  }


  public static void errorf(String format, Object... args) {
    error(String.format(format, args));
  }

  public static void error(String message) {
    throw new RuntimeException(message);
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


  static class Token {

    public short opcode;
    public boolean immediate;
    Consumer<Short> function;

    public Token(int opcode, boolean immediate, Consumer<Short> function) {
      this.opcode = (short) opcode;
      this.immediate = immediate;
      this.function = function;
    }
  }
}
