import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class SuchtbaumTest {

  private Random random = new Random();

  @Test
  public void testContains() throws InterruptedException {
    for (int j = 0; j < 100; j++) {
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
  }


  @Test
  public void testContainsRemove() throws InterruptedException {
    for (int j = 0; j < 100; j++) {
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
        int next = random.nextInt(20 * n);
        if (testSet.contains(next)) {
          suchti.remove(next);
          testSet.remove(next);
        }
      }
      for (int i = 0; i < n; i++) {
        assertEquals(testSet.contains(i), suchti.contains(i));
      }
    }
  }
  @Test
  public void testRemoveSize() throws InterruptedException {
    int n = 1000;
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    List<Integer> liste = new ArrayList<>();
    for(int i = 0; i < n; i++) {
      liste.add(i);
    }
    Collections.shuffle(liste);

    for(Integer i: liste) {
      suchti.insert(i);
    }

    int count = 0;
    for(int i = 0; i < n; i++) {
      if(Math.random() < 0.5) {
        suchti.remove(i);
        count++;
      }
    }

    int sizeCount = 0;

    for(int i = 0; i < n; i++) {
      if(suchti.contains(i))
        sizeCount++;
    }

    assertEquals(sizeCount, n - count);
  }

}
