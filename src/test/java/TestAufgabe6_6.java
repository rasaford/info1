import static org.junit.Assert.*;

import org.junit.Test;

public class TestAufgabe6_6 {

  @Test
  public void quadraticFormula() {
    assertArrayEquals(new int[]{7, 11},
        aufgabe6_6.quadraticFormula(
            new double[]{38.5, -9, 0.5}
        ));
  }

  @Test
  public void findRootRecursive() {
    assertEquals(8,
        aufgabe6_6.findRootRecursive(new double[]{-308, -33.5, 5, 0.5},
            -20, 20));
  }

  @Test
  public void findIntervalRecursive() {
    assertArrayEquals(new int[]{-20, 20},
        aufgabe6_6.findIntervalRecursive(new double[]{-308, -33.5, 5, 0.5},
            -2, 2, 10));
  }

  @Test
  public void hornerSchema() {
    assertTrue(aufgabe6_6.hornerSchema(
        new double[]{-308, -33.5, 5, 0.5}, 8).length == 3);
  }
}
