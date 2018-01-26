public class aufgabe2_9 extends MiniJava {

  public static void main(String[] args) {
    int userInput = getIntInRange(1, Integer.MAX_VALUE);

    writeConsole(String.format("\\begin{tabular}{%s}\n", times(userInput, "1")));
    for (int i = 1; i <= userInput; i++) {
      for (int j = 0; j < userInput; j++) {
        String str = (j == userInput - 1) ? "%d\\\\\n" : "%d & ";
        writeConsole(String.format(str, (int) Math.pow(i, j)));
      }
    }
    writeConsole("\\done{tabular}\n");
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

  /**
   * times performs string multiplication.
   * The given string gets concatenated n times.
   *
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

