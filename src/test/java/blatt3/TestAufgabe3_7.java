package blatt3;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAufgabe3_7 {

  @Test
  public void testAdd() {
    assertEquals(-3, aufgabe3_7.add(new int[]{1,2,3,4,5,6}));
    assertEquals(0, aufgabe3_7.add(new int[]{1,1,1,1,1,1}));
  }

}
