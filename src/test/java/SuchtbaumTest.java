import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Random;
import org.junit.Test;

public class SuchtbaumTest {

  private Random random = new Random();

  @Test
  public void testContains() throws InterruptedException {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 10000;
    for (int i = 0; i < n; i++) {
      testSet.add(random.nextInt(20 * n));
    }
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (Integer i : testSet) {
      suchti.insert(i);
    }
    for (int i = 0; i < n; i++) {
      assertEquals(testSet.contains(i), suchti.contains(i));
    }
  }


  // TODO: fix tree deletion
  @Test
  public void testContainsRemove() throws InterruptedException {
    for (int j = 0; j < 10000; j++) {
      HashSet<Integer> testSet = new HashSet<>();
      int n = 10;
      for (int i = 0; i < n; i++) {
        testSet.add(random.nextInt(20 * n));
      }
      Suchtbaum<Integer> suchti = new Suchtbaum<>();
      for (Integer i : testSet) {
        suchti.insert(i);
      }
      for (int i = 0; i < n; i++) {
        int next = random.nextInt(20 * n);
        if (testSet.contains(next)) {
          System.out.println(suchti.toString());
          suchti.remove(next);
          testSet.remove(next);
        }
      }
      for (int i = 0; i < n; i++) {
        assertEquals(testSet.contains(i), suchti.contains(i));
      }
    }
  }
}
