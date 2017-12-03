import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestAufgabe7_6 {


  @Test
  public void slowSort() {
    int[] array = new int[] {4,3,1,2,5};
    aufgabe7_6.slowSort(array);
   assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
  }
}
