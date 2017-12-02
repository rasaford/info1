import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Test;

public class TestAufgabe6_7 {


  @Test
  public void testToString() {
    assertEquals("3FC1",
        aufgabe6_7.toString(new int[]{1, 12, 15, 3}));
  }

  @Test
  public void add() {
    assertArrayEquals(new int[]{3, 1, 6, 5, 7, 3, 2, 5},
        aufgabe6_7.add(new int[]{7, 5, 3, 1, 0, 6, 4, 2},
            new int[]{4, 3, 2, 4, 7, 5, 5, 2}, 8));
  }

  @Test
  public void add2() {
    assertArrayEquals(new int[]{6, 2, 8, 5, 2, 5, 1, 1, 1},
        aufgabe6_7.add(new int[]{8, 7, 4, 3, 2, 3},
            new int[]{7, 3, 3, 2, 0, 2, 1, 1, 1}, 9));
  }

  @Test
  public void mulDigit1() {
    assertArrayEquals(new int[]{0,0,1, 1, 1, 1},
        aufgabe6_7.mulDigit(new int[]{1, 1, 1, 1}, 1, 2, 2));
    assertArrayEquals(new int[]{0,0,4, 3, 2, 1},
        aufgabe6_7.mulDigit(new int[]{4, 3, 2, 1}, 1, 2, 5));
  }

  @Test
  public void mulDigit2() {
    assertArrayEquals(new int[]{4, 0, 11, 5},
        aufgabe6_7.mulDigit(new int[]{4, 3, 2, 1}, 5, 0, 16));
  }

  @Test
  public void mul1() {
//    assertArrayEquals(new int[]{0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1},
//        aufgabe6_7.mul(new int[]{0, 0, 1, 1, 0, 1, 0, 1, 0, 1},
//            new int[]{1, 1, 1, 0, 0, 0, 1, 1}, 2));
    assertArrayEquals(new int[]{4,11,3,1,2},
        aufgabe6_7.mul(new int[]{12, 7},
            new int[]{12, 10, 2}, 16));
  }

  @Test
  public void mul2() {
    assertArrayEquals(new int[]{6, 5, 8, 10, 1, 4, 5, 6, 11, 1, 2, 4},
        aufgabe6_7.mul(new int[]{14, 3, 15, 15, 0, 12},
            new int[]{5, 15, 15, 10, 7, 5}, 16));
  }

  @Test
  public void mul3() {
    assertArrayEquals(new int[]{8,5,4,10, 15, 11, 1},
        aufgabe6_7.mul(new int[]{8,9,3,2,1},
            new int[]{9,8,1}, 16));
  }
}
