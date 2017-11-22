public class aufgabe2_7 extends MiniJava {

  public static void main(String[] args) {
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
      write(String.format(
          "The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max);
    }
    return input;
  }
}

