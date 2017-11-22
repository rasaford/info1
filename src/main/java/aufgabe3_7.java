public class aufgabe3_7 extends MiniJava {

  public static void main(String[] args) {
    adder();
    secondLargest();
    twoElementSum();
  }

  public static void adder() {
    int[] input = readArray();
    write(add(input));
  }

  public static int add(int[] input) {
    int sum = 0;
    for (int i = 0; i < input.length; i++) {
      sum = (i % 2 == 0) ? sum + input[i] : sum - input[i];
    }
    return sum;
  }

  public static void secondLargest() {
    int[] input = readArray();
    write(second(input));
  }

  public static int second(int[] input) {
    int max = Integer.MIN_VALUE, second = Integer.MIN_VALUE;
    for (int i : input) {
      if (i > max) {
        max = i;
      }
      if (i < max && i > second) {
        second = i;
      }
    }
    return (second == Integer.MIN_VALUE) ? max : second;
  }

  public static void twoElementSum() {
    int[] input = readArray();
    write(twoSum(input));
  }

  public static int[] twoSum(int[] input) {
    for (int i = 0; i < input.length - 1; i++) {
      input[i] = input[i] + input[i + 1];
    }
    return input;
  }

  public static int[] readArray() {
    int size = getIntInRange(2, Integer.MAX_VALUE,
        "how many numbers do you wish to enter into the array (have to be >= 2)");
    int[] array = new int[size];
    for (int i = 0; i < size; i++) {
      array[i] = getIntInRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    return array;
  }

  private static void write(int[] input) {
    String elements = "";
    for (int i = 0; i < input.length; i++) {
      elements += (i == input.length - 1) ? input[i] : input[i] + ",";
    }
    write("[" + elements + "]");
  }

  /**
   * getIntInRange gets an int from the user in the given range.
   *
   * @param min min of the input value (inclusive)
   * @param max max of the input value (exclusive)
   * @return user input
   */
  private static int getIntInRange(int min, int max) {
    return getIntInRange(min, max,
        String.format("enter a number in the range %d <= x < %d", min, max));
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
