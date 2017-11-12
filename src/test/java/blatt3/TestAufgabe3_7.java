package blatt3;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAufgabe3_7 {

  @Test
  public void testAdd() {
    assertEquals(-3, aufgabe3_7.add(new int[]{1,2,3,4,5,6}));
    assertEquals(0, aufgabe3_7.add(new int[]{1,1,1,1,1,1}));
  }

  @Test
  public void testSecond() {
    assertEquals(2, aufgabe3_7.second(new int[]{2,2}));
    assertEquals(4, aufgabe3_7.second(new int[]{123,4}));
  }

  @Test
  public void testTwoSum() {
    assertArrayEquals(new int[]{4,2}, aufgabe3_7.twoSum(new int[]{2,2}));
    assertArrayEquals(new int[]{127, 4}, aufgabe3_7.twoSum(new int[]{123,4}));
    assertArrayEquals(new int[]{3,5,7,9,5}, aufgabe3_7.twoSum(new int[]{1,2,3,4,5}));
    assertArrayEquals(new int[]{0,0,5,5}, aufgabe3_7.twoSum(new int[]{0,0,0,5}));
  }
}
