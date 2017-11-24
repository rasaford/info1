import java.lang.reflect.Field;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAufgabe5_7 {

  // dirty hacks to get aroud the private static field
  public void setRows(int rows) {
    try {
      Field f = aufgabe5_7.class.getDeclaredField("lines");
      f.setAccessible(true);
      f.setInt(null, rows);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println(e);
    }
  }

  @Test
  public void swap() {
    int[] matrix = new int[]{
        1, 2, 3,
        5, 6, 7
    };
    setRows(2);
    aufgabe5_7.swap(matrix, 0, 1);
    assertArrayEquals(new int[]{
        5, 6, 7,
        1, 2, 3
    }, matrix);
  }

  @Test
  public void multLine() {
    int[] matrix = new int[]{
        1, 2, 3,
        5, 6, 7
    };
    setRows(2);
    aufgabe5_7.multLine(matrix, 1, -1);
    assertArrayEquals(new int[]{
        1, 2, 3,
        -5, -6, -7
    }, matrix);
  }

  @Test
  public void multAddLne() {
    int[] matrix = new int[]{
        1, 2, 3,
        5, 6, 7
    };
    setRows(2);
    aufgabe5_7.multAddLine(matrix, 0, 1, -2);
    assertArrayEquals(new int[]{
        -9, -10, -11,
        5, 6, 7
    }, matrix);
  }


  @Test
  public void searchSwap() {
    int[] matrix = new int[]{
        0, 0, 3, 4,
        0, 0, 3, 5,
        1, 1, 1, 1
    };
    setRows(3);
    aufgabe5_7.searchSwap(matrix, 0);
    aufgabe5_7.printMatrix(matrix);
    assertArrayEquals(new int[]{
        1, 1, 1, 1,
        0, 0, 3, 4,
        0, 0, 3, 5,
    }, matrix);
  }

  @Test
  public void lcm() {
    assertEquals(12, aufgabe5_7.kgv(4, 6));
    assertEquals(12, aufgabe5_7.kgv(-4, 6));
    assertEquals(12, aufgabe5_7.kgv(-4, 12));
    assertEquals(22, aufgabe5_7.kgv(2, 11));
  }

  @Test
  public void solve1() {
    int[] matrix = new int[]{
        2, 3, 0, 8,
        1, 1, 1, 6,
        2, 0, 1, 5,
    };
    setRows(3);
    assertArrayEquals(new int[]{1, 2, 3},
        aufgabe5_7.solveSystem(matrix));
  }

  @Test
  public void solve2() {
    int[] matrix = new int[]{
        1, 2, 3, 8,
        4, 0, 6, 26,
        1, 0, 0, 5
    };
    setRows(3);
    assertArrayEquals(new int[]{5, 0, 1},
        aufgabe5_7.solveSystem(matrix));
  }

  @Test
  public void solve3() {
    int[] matrix = new int[]{
        0, 1, 0, 1,
        0, 0, 1, 2,
        1, 0, 0, 3,
    };
    setRows(3);
    assertArrayEquals(new int[]{3, 1, 2},
        aufgabe5_7.solveSystem(matrix));
  }

  @Test
  public void solve4() {
    int[] matrix = new int[]{
        2, 6
    };
    setRows(1);
    assertArrayEquals(new int[]{3},
        aufgabe5_7.solveSystem(matrix));
  }

  @Test
  public void rowEchelonToResult() {
    setRows(3);
    assertArrayEquals(new int[]{5, -6, 3},
        aufgabe5_7.rowEchelonToResult(new int[]{
            1, 2, 3, 2,
            0, -1, -2, 0,
            0, 0, -2, -6
        }));
  }
}
