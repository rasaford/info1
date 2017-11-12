package blatt3;

import miniJava.MiniJava;

public class aufgabe3_8 extends MiniJava {

  public static void main(String[] args) {
    int input = getIntInRange(0, Integer.MAX_VALUE,
        "enter a palindrome consisting only of numbers");
    if (palindrome(input)) {
      write("The number " + input + " is a palindrome");
    } else {
      write("The number " + input + " is not a palindrome");
    }
  }

  public static boolean palindrome(int input) {
    int number = input;
    int size = digits(input);
    byte[] numbers = new byte[size];

    for (int i = size - 1; i >= 0; i--) {
      numbers[i] = (byte) (number % 10);
      number /= 10;
    }

    for (int i = 0; i <= size / 2; i++) {
      if (numbers[i] != numbers[size - i - 1]) {
        return false;
      }
    }
    return true;
  }

  private static int digits(int number) {
    int count = 0;
    while (number != 0)  {
      number /= 10;
      count++;
    }
    return (count == 0) ? 1 : count;
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
    int input = readInt(dialog);
    if (input < min || input >= max) {
      write(String.format("The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max, dialog);
    }
    return input;
  }
}
