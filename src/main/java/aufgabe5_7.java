public class aufgabe5_7 extends MiniJava {

  private static int lines;

  public static void main(String[] args) {
    lines = read("Geben Sie die Anzahl der Gliechungen ein");
    int[] matrix = readMatrix();
    printMatrix(matrix);
  }

  public static int[] readMatrix() {
    int[] matrix = new int[lines * (lines + 1)];
    for (int i = 0; i < lines; i++) {
      for (int j = 0; j < lines + 1; j++) {
        int in = read(String.format("enter the %dth coefficient for the %dth line\n" +
            "current matrix:\n" + pMatrix(matrix), j, i));
        set(matrix, i, j, in);
      }
    }
    return matrix;
  }

  public static int get(int[] matrix, int row, int col) {
    return matrix[row * (lines + 1) + col];
  }

  public static void set(int[] matrix, int row, int col, int val) {
    matrix[row * (lines + 1) + col] = val;
  }

  public static void multLine(int[] matrix, int row, int factor) {
    for (int i = 0; i < lines + 1; i++) {
      set(matrix, row, i,
          get(matrix, row, i) * factor);
    }
  }

  public static void divLine(int[] matrix, int row, int divisor) {
    for (int i = 0; i < lines + 1; i++) {
      set(matrix, row, i,
          get(matrix, row, i) / divisor);
    }
  }

  public static void multAddLine(int[] matrix, int row, int otherRow, int factor) {
    for (int i = 0; i < lines + 1; i++) {
      set(matrix, row, i,
          get(matrix, row, i) + get(matrix, otherRow, i) * factor);
    }
  }

  public static void swap(int[] matrix, int rowA, int rowB) {
    for (int i = 0; i < lines + 1; i++) {
      int temp = get(matrix, rowA, i);
      set(matrix, rowA, i,
          get(matrix, rowB, i));
      set(matrix, rowB, i, temp);
    }
  }

  public static void searchSwap(int[] matrix, int row) {
    int initialRow = row;
    while (get(matrix, row, initialRow) == 0) {
      row++;
    }
    while (initialRow != row) {
      swap(matrix, row, --row);
    }
  }

  public static int kgv(int a, int b) {
    if (a == 0 && a == b) {
      return 0;
    }
    a = a < 0 ? -a : a;
    b = b < 0 ? -b : b;
    return a * b / gcd(a, b);
  }

  private static int gcd(int numA, int numB) {
    if (numB == 0) {
      return numA;
    }
    if (numB > numA) {
      int temp = numA;
      numA = numB;
      numB = temp;
    }
    return gcd(numB, numA % numB);
  }

  public static int[] rowEchelonToResult(int[] matrix) {
    for (int i = lines - 1; i >= 0; i--) {
      for (int j = lines - 1; j > i; j--) {
        int factor = get(matrix, i, j);
        multAddLine(matrix, i, j, -factor);
      }
      int div = get(matrix, i, i);
      divLine(matrix, i, div);
    }
    // extract the result from the solved matrix
    int[] result = new int[lines];
    for (int i = 0; i < lines; i++) {
      result[i] = get(matrix, i, lines);
    }
    return result;
  }

  public static int[] solveSystem(int[] matrix) {
    for (int i = 0; i < lines; i++) {
      // only does anything if the current value on the diagonal is 0
      searchSwap(matrix, i);
      for (int j = i + 1; j < lines; j++) {
        int val = get(matrix, j, i);
        if (val == 0) {
          continue;
        }
        int diag = get(matrix, i, i);
        int lcm = kgv(diag, val);
        multLine(matrix, j, lcm / val);
        multAddLine(matrix, j, i, -lcm / diag);
      }
    }
    return rowEchelonToResult(matrix);
  }

  public static void printMatrix(int[] matrix) {
    writeConsole(pMatrix(matrix));
  }

  private static String pMatrix(int[] matrix) {
    String res = "";
    for (int i = 0; i < lines; i++) {
      for (int j = 0; j < lines + 1; j++) {
        res += get(matrix, i, j);
        res += j == lines ? "" : ", ";
      }
      res += "\n";
    }
    return res;
  }
}
