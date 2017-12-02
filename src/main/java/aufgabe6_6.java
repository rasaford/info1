import java.util.Arrays;

public class aufgabe6_6 {

  public static void main(String[] args) {
    double[] coefficients = new double[4];
    for (int i = 0; i < coefficients.length; i++) {
      coefficients[i] = getDoubleInRange(-Double.MAX_VALUE, Double.MAX_VALUE,
          String.format("coefficient for x^%d", i));
    }
    int[] interval = findIntervalRecursive(coefficients, -2, 2, 10);
    int root = findRootRecursive(coefficients, interval[0], interval[1]);
    double[] reduced = hornerSchema(coefficients, root);
    int[] roots = quadraticFormula(reduced);
    String res = "x_0 = " + root;
    for (int i = 0; i < roots.length; i++) {
      res += String.format(", x_%d = %d", i + 1, roots[i]);
    }
    MiniJava.write(String.format("The polynomial %.0fx^3 %.0fx^2 %.0fx^1 %.0fx^0 has the roots:\n"
        + res, coefficients[3], coefficients[2], coefficients[1], coefficients[0]));
  }

  /**
   * getDoubleInRange gets an int from the user in the given range.
   *
   * @param min min of the input value (inclusive)
   * @param max max of the input value (exclusive)
   * @param dialog Message to display to the user
   * @return user input
   */
  private static double getDoubleInRange(double min, double max, String dialog) {
    double input = MiniJava.readDouble(dialog);
    if (input < min || input >= max) {
      MiniJava.write(
          String.format("The given number has to be in the range %10.2f <= x < %10.2f", min, max));
      return getDoubleInRange(min, max, dialog);
    }
    return input;
  }

  public static int[] quadraticFormula(double[] coefficients) {
    double root = Math.pow(coefficients[1], 2) - 4 * coefficients[2] * coefficients[0];
    if (root < 0) {
      return new int[0];
    }
    root = Math.sqrt(root);
    double res1 = (-coefficients[1] - root) / (2 * coefficients[2]);
    double res2 = (-coefficients[1] + root) / (2 * coefficients[2]);
    return new int[]{(int) res1, (int) res2};
  }

  public static double[] hornerSchema(double[] coefficients, int x0) {
    double[][] table = new double[coefficients.length][];
    for (int i = 0; i < table.length; i++) {
      table[i] = new double[2];
      table[i][0] = coefficients[i];
    }
    double[] res = new double[coefficients.length - 1];
    for (int i = table.length - 2; i >= 0; i--) {
      double coefficient = table[i + 1][0] + table[i + 1][1] * x0;
      res[i] = coefficient;
      table[i][1] = coefficient;
    }
    return res;
  }

  public static double calculateY(double[] coefficients, int x) {
    double res = 0;
    for (int i = 0; i < coefficients.length; i++) {
      res += coefficients[i] * Math.pow(x, i);
    }
    return res;
  }

  public static int[] findIntervalRecursive(double[] coefficients, int a, int b, int factor) {
    double fA = calculateY(coefficients, a);
    double fB = calculateY(coefficients, b);
    if (fA < 0 != fB < 0) {
      return new int[]{a, b};
    }
    return findIntervalRecursive(coefficients, a * factor, b * factor, factor);
  }


  public static int findRootRecursive(double[] coefficients, int a, int b) {
    int m = (a + b) / 2;
    double fA = calculateY(coefficients, a);
    double fB = calculateY(coefficients, b);
    double fM = calculateY(coefficients, m);
    if (fA == 0) {
      return a;
    }
    if (fB == 0) {
      return b;
    }
    if (fM == 0) {
      return m;
    }
    if (fA < 0 != fM < 0) {
      return findRootRecursive(coefficients, a, m);
    }
    return findRootRecursive(coefficients, m, b);
  }
}
