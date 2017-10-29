public class H02 extends MiniJava {

  public static void main(String[] args) {
    Aufgabe_2_7();
    Aufgabe_2_8();
    Aufgabe_2_9();
  }

  public static void Aufgabe_2_7() {
    int userInput = getIntInRange(0, Integer.MAX_VALUE);

    int sum = 0;
    for (int i = 1; i <= userInput; i++) {
      if (i % 3 == 0 || i % 7 == 0) {
        sum += i;
      }
    }
    write(sum);
  }

  /**
   * getIntInRange gets an int from the user in the given range.
   *
   * @param min min of the input value (inclusive)
   * @param max max of the input value (exclusive)
   * @return user input
   */
  private static int getIntInRange(int min, int max) {
    int input = read();
    if (input < min || input >= max) {
      write(String.format("The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max);
    }
    return input;
  }

  public static void Aufgabe_2_8() {
    int userInput = getIntInRange(0, Integer.MAX_VALUE);
    if (isPrime(userInput)) {
      write(String.format("%d is prime", userInput));
    } else {
      write(String.format("%d is not prime", userInput));
    }
  }

  /**
   * isPrime returns true if the given number is prime, false otherwise.
   * @param n number to check
   * @return primality of n
   */
  private static boolean isPrime(int n) {
    if (n == 2 || n == 3) {
      return true;
    }
    if (n <= 1 || n % 2 == 0 || n % 3 == 0) {
      return false;
    }
    for (int i = 3; i * i <= n; i += 2) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static void Aufgabe_2_9() {
    int userInput = getIntInRange(1, Integer.MAX_VALUE);

    writeConsole(String.format("\\begin{tabular}{%s}\n", times(userInput, "1")));
    for (int i = 1; i <= userInput; i++) {
      for (int j = 0; j < userInput; j++) {
        String str = (j == userInput - 1) ? "%d\\\\\n" : "%d & ";
        writeConsole(String.format(str, (int) Math.pow(i, j)));
      }
    }
    writeConsole("\\end{tabular}\n");
  }

  /**
   * times performs string multiplication.
   * The given string gets concatenated n times.
   * @param n number of times to concatenate the string.
   * @param s string to multiply.
   * @return the multiplied string
   */
  private static String times(int n, String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append(s);
    }
    return sb.toString();
  }
}

