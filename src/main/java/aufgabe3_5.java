
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class aufgabe3_5 extends MiniJava {

  private static Map<Long, Integer> map = new HashMap<>();

  public static void main(String[] args) {
    int[][] tri = pascal(getIntInRange(0, Integer.MAX_VALUE,
        "how many rows do you want to have the triangle for"));
    writeConsole(tri);
  }

  private static void writeConsole(int[][] input) {
    for (int[] i : input) {
      writeConsole(Arrays.toString(i));
      writeConsole("\n");
    }
  }

  private static int[][] pascal(int rows) {
    int[][] tri = new int[rows][];
    for (int i = 0; i < rows; i++) {
      tri[i] = new int[i + 1];
      for (int j = 0; j <= i; j++) {
        tri[i][j] = binom(i, j);
      }
    }
    return tri;
  }

  private static int binom(int n, int k) {
    if (n == k || k == 0) {
      return 1;
    }
    Integer result = map.get(merge(n, k));
    if (result != null) {
      return result;
    }

    int res = binom(n - 1, k - 1) + binom(n - 1, k);
    map.put(merge(n, k), res);
    return res;
  }

  private static long merge(int a, int b) {
    long res = ((long) a) << 32;
    res |= b;
    return res;
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
