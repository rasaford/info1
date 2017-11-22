public class aufgabe2_8 extends MiniJava {

  public static void main(String[] args) {
    int userInput = getIntInRange(0, Integer.MAX_VALUE);

    if (isPrime(userInput)) {
      write(String.format("%d is prime", userInput));
    } else {
      write(String.format("%d is not prime", userInput));
    }
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
   * isPrime returns true if the given number is prime, false otherwise.
   *
   * @param number number to check
   * @return primality of number
   */
  private static boolean isPrime(int number) {
    if (number == 2 || number == 3) {
      return true;
    }
    if (number <= 1 || number % 2 == 0 || number % 3 == 0) {
      return false;
    }
    for (int i = 3; i * i <= number; i += 2) {
      if (number % i == 0) {
        return false;
      }
    }
    return true;
  }
}

