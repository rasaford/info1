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
  }

}
