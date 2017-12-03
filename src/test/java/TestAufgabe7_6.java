import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;

public class TestAufgabe7_6 {


  @Test
  public void slowSort() {
    int[] array = new int[]{4, 3, 1, 2, 5};
    aufgabe7_6.slowSort(array);
    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
  }

  @Test
  public void slowSort1() {
    int[] array = new int[]{2, 1};
    aufgabe7_6.slowSort(array);
    assertArrayEquals(new int[]{1, 2}, array);
  }

  @Test
  public void slowSort2() {
    int[] array = new int[]{2, 1, -44, 55};
    aufgabe7_6.slowSort(array);
    assertArrayEquals(new int[]{-44, 1, 2, 55}, array);
  }

  @Test
  public void slowSort3() {
    int[] array = new int[]{0, 0, -11, 22, 123, 0};
    aufgabe7_6.slowSort(array);
    assertArrayEquals(new int[]{-11, 0, 0, 0, 22, 123}, array);
  }

  @Test
  public void slowSort4() {
    int[] array = new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18};
    aufgabe7_6.slowSort(array);
    assertArrayEquals(new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18}, array);
  }

  @Test
  public void slowSort5() {
    int[] array = new int[]{17, 15, 13, 11, 9, 7, 5, 3, 1};
    aufgabe7_6.slowSort(array);
    assertArrayEquals(new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17}, array);
  }

  @Test
  public void evenSlowerSort() {
    int[] array = new int[]{4, 3, 1, 2, 5};
    aufgabe7_6.evenSlowerSort(array);
    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
  }

  @Test
  public void evenSlowerSort1() {
    int[] array = new int[]{2, 1};
    aufgabe7_6.evenSlowerSort(array);
    assertArrayEquals(new int[]{1, 2}, array);
  }

  @Test
  public void evenSlowerSort2() {
    int[] array = new int[]{2, 1, -44, 55};
    aufgabe7_6.evenSlowerSort(array);
    assertArrayEquals(new int[]{-44, 1, 2, 55}, array);
  }

  @Test
  public void evenSlowerSort3() {
    int[] array = new int[]{0, 0, -11, 22, 123, 0};
    aufgabe7_6.evenSlowerSort(array);
    assertArrayEquals(new int[]{-11, 0, 0, 0, 22, 123}, array);
  }

  @Test
  public void evenSlowerSort4() {
    int[] array = new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18};
    aufgabe7_6.evenSlowerSort(array);
    assertArrayEquals(new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18}, array);
  }

  @Test
  public void evenSlowerSort5() {
    int[] array = new int[]{17, 15, 13, 11, 9, 7, 5, 3, 1};
    aufgabe7_6.evenSlowerSort(array);
    assertArrayEquals(new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17}, array);
  }
}
