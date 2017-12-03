public class aufgabe6_7 extends MiniJava {

  private static int base;
  private static int printWidth;

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    base = getIntInRange(2, 37,
        "What base do you want to represent the numbers in?");
    int[] a = readNumber();
    int[] b = readNumber();
    printWidth = a.length + b.length + 2;

    System.out.printf("\\begin{tabular}{%s}\n", multiply("c", printWidth));
    System.out.print(printHeader(a, b) + "\n");
    System.out.println("\\hline");
    int[] res = mul(a, b, base);
    System.out.println("\\hline");
    System.out.println(toLatex("=", res));
    System.out.println("\\end{tabular}");
  }

  public static int[] readNumber() {
    String number = MiniJava.readString(String.format("Enter the number in base %d", base));
    int[] temp = new int[number.length()];
    for (int i = 0; i < number.length(); i++) {
      int index = number.length() - i - 1;
      int digit = atoi(number.charAt(i));
      if (digit > base - 1) {
        return readNumber();
      }
      temp[index] = digit;
    }
    // trim any leading zeros
    int offset = 0;
    for (int i = temp.length - 1; i >= 0; i--) {
      if (temp[i] != 0) {
        offset = temp.length - i - 1;
        break;
      }
    }
    int[] out = new int[temp.length - offset];
    for (int i = 0; i < out.length; i++) {
      out[i] = temp[i];
    }
    return out;
  }

  private static String multiply(String input, int times) {
    String res = "";
    for (int i = 0; i < times; i++) {
      res += input;
    }
    return res;
  }

  private static String printHeader(int[] a, int[] b) {
    String header = "";
    for (int i = a.length - 1; i >= 0; i--) {
      header += String.format("& %d ", a[i]);
    }
    header += "& $\\ast$ ";
    for (int i = b.length - 1; i >= 0; i--) {
      header += String.format("& %d ", b[i]);
    }
    return header + "\\\\";
  }

  private static String toLatex(String prefix, int[] input) {
    String res = prefix;
    int index = input.length - 1;
    for (int i = 1; i < printWidth; i++) {
      res += "&";
      if (printWidth - i <= input.length) {
        res += String.format(" %s", itoa(input[index--]));
      }
    }
    return res + "\\\\";
  }

  /**
   * getIntInRange gets an int from the user in the given range.
   *
   * @param min min of the input value (inclusive)
   * @param max max of the input value (exclusive)
   * @param dialog Message to display to the user
   * @return user input
   */
  private static int getIntInRange(int min, int max, String dialog) {
    int input = MiniJava.readInt(dialog);
    if (input < min || input >= max) {
      MiniJava.write(
          String.format("The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max, dialog);
    }
    return input;
  }

  public static String toString(int[] number) {
    String out = "";
    for (int i = number.length - 1; i >= 0; i--) {
      out += itoa(number[i]);
    }
    return out;
  }

  public static char itoa(int i) {
    return i < 10 ? (char) (i + 48) : (char) (i + 55);
  }

  public static int atoi(char digit) {
    return ('0' <= digit && digit <= '9') ? digit - '0' : digit - 'A' + 10;
  }

  public static int[] add(int[] a, int[] b, int base) {
    if (a.length > b.length) {
      int[] temp = a;
      a = b;
      b = temp;
    }
    int[] out = new int[b.length + 1]; // +1 for the carry bit
    for (int i = 0; i < a.length; i++) {
      out[i] = a[i];
    }
    int carry = 0;
    for (int i = 0; i < b.length; i++) {
      int res = out[i] + b[i] + carry;
      out[i] = res % base;
      carry = res / base;
    }
    out[out.length - 1] = carry;
    return trim(out);
  }

  public static int[] mulDigit(int[] a, int digit, int shift, int base) {
    int[] out = new int[a.length + shift];
    for (int i = 0; i < a.length; i++) {
      out[i + shift] = a[i];
    }
    int[] copy = new int[out.length];
    for (int i = 0; i < out.length; i++) {
      copy[i] = out[i];
    }
    // -poor- lazy man's multiplication:
    for (int i = 0; i < digit - 1; i++) {
      out = add(out, copy, base);
    }
    return out;
  }

  public static int[] mul(int[] a, int[] b, int base) {
    int[] out = new int[0];
    if (b.length > a.length) {
      int[] temp = a;
      a = b;
      b = temp;
    }
    for (int i = 0; i < b.length; i++) {
      int[] partialResult = mulDigit(a, b[i], i, base);
      out = add(partialResult, out, base);
      System.out.println(toLatex("+", partialResult));
    }
    return trim(out);
  }

  private static int[] trim(int[] a) {
    int zeros = 0;
    for (int i = a.length - 1; i >= 0; i--) {
      if (a[i] != 0) {
        break;
      }
      zeros++;
    }
    int[] trim = new int[a.length - zeros];
    for (int i = 0; i < trim.length; i++) {
      trim[i] = a[i];
    }
    return trim;
  }
}
