import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.Test;

public class WoststorageTest {

  private void setCount(Worstorage w, int[] ps) {
    try {
      Field f = Worstorage.class.getDeclaredField("count");
      f.setAccessible(true);
      f.set(w, ps);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      System.err.println(e);
    }
  }

  private int[] getCount(Worstorage w) {
    try {
      Field f = Worstorage.class.getDeclaredField("count");
      f.setAccessible(true);
      return (int[]) f.get(w);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      System.err.println(e);
    }
    return null;
  }

  private void setPs(Worstorage w, Penguin[] ps) {
    try {
      Field f = Worstorage.class.getDeclaredField("ps");
      f.setAccessible(true);
      f.set(w, ps);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      System.err.println(e);
    }
  }

  private Penguin[] getPs(Worstorage w) {
    try {
      Field f = Worstorage.class.getDeclaredField("ps");
      f.setAccessible(true);
      return (Penguin[]) f.get(w);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      System.err.println(e);
    }
    return null;
  }

  @Test
  public void lotsOfRandomTesting() {
    for (int i = 0; i < 1 << 15; i++) {
      randomInsert();
    }
  }

  @Test
  public void randomInsert() {
    Worstorage w = new Worstorage();
    Set<Integer> nums = new HashSet<>();
    Random r = new Random();
    for (int i = 0; i < 100; i++) {
      nums.add(r.nextInt(1000000) - 500000);
    }
    for (Integer i : nums) {
      w.insert(new Penguin(i));
    }
    for (Integer i : nums) {
      if (!w.find(new Penguin(i))) {
        System.err.println("error");
      }
    }
  }

  @Test
  public void insertSimple() {
    Worstorage w = new Worstorage();
    Penguin p1 = new Penguin(1);
    Penguin p2 = new Penguin(2);
    Penguin p3 = new Penguin(3);
    Penguin p4 = new Penguin(4);
    w.insert(p2);
    w.insert(p3);
    w.insert(p1);
    w.insert(p4);

    w.insert(p1);
    w.insert(p1);
    w.insert(p1);
    w.insert(p4);
    w.insert(p4);
    w.insert(p4);
    assertArrayEquals(new Penguin[]{p2, p1, p3, null, null, null, p4},
        getPs(w));
  }

  @Test
  public void find() {
    Worstorage w = new Worstorage();
    Penguin p1 = new Penguin(1);
    Penguin p2 = new Penguin(2);
    Penguin p3 = new Penguin(3);
    Penguin p4 = new Penguin(4);
    Penguin p5 = new Penguin(5);
    setPs(w, new Penguin[]{p3, p2, p4});
    setCount(w, new int[2]);
    assertTrue(w.find(p3));
    assertTrue(w.find(p4));
    assertFalse(w.find(p1));
    assertFalse(w.find(p5));
  }

  @Test
  public void insertAndFind() {
    Worstorage w = new Worstorage();
    Penguin p1 = new Penguin(1);
    Penguin p2 = new Penguin(2);
    Penguin p3 = new Penguin(3);
    Penguin p4 = new Penguin(4);
    Penguin p5 = new Penguin(5);
    Penguin p6 = new Penguin(6);
    Penguin p7 = new Penguin(7);
    w.insert(p2);
    w.insert(p3);
    w.insert(p1);
    w.insert(p4);
    System.out.println(w.toString());

    assertTrue(w.find(p1));
    assertTrue(w.find(p2));
    assertTrue(w.find(p3));
    assertTrue(w.find(p4));

    assertFalse(w.find(p5));
    assertFalse(w.find(p6));
    assertFalse(w.find(p7));
  }

  @Test
  public void insertDelete() {
    Worstorage w = new Worstorage();
    Penguin p1 = new Penguin(1);
    Penguin p2 = new Penguin(2);
    Penguin p3 = new Penguin(3);
    Penguin p4 = new Penguin(4);
    Penguin p5 = new Penguin(5);
    Penguin p6 = new Penguin(6);
    Penguin p7 = new Penguin(7);
    w.insert(p2);
    w.insert(p3);
    w.insert(p1);
    w.insert(p4);

    assertTrue(w.find(p1));
    assertTrue(w.find(p2));
    assertTrue(w.find(p3));
    assertTrue(w.find(p4));

    w.remove(p1);
    w.remove(p2);
    w.remove(p3);
    w.remove(p4);
    w.remove(p5);
    w.remove(p6);
    w.remove(p7);

    assertFalse(w.find(p1));
    assertFalse(w.find(p2));
    assertFalse(w.find(p3));
    assertFalse(w.find(p4));
  }

  @Test
  public void paperExample() {
    Worstorage w = new Worstorage();
    assertArrayEquals(new Penguin[]{null}, getPs(w));
    assertArrayEquals(new int[]{0}, getCount(w));

    Penguin p3 = new Penguin(3);
    w.insert(p3);
    assertArrayEquals(new Penguin[]{p3}, getPs(w));
    assertArrayEquals(new int[]{1}, getCount(w));

    Penguin p2 = new Penguin(2);
    w.insert(p2);
    assertArrayEquals(new Penguin[]{p3, p2, null}, getPs(w));
    assertArrayEquals(new int[]{1, 1}, getCount(w));

    Penguin p1 = new Penguin(1);
    w.insert(p1);
    assertArrayEquals(new Penguin[]{p3, p2, null, p1, null, null, null}, getPs(w));
    assertArrayEquals(new int[]{1, 1, 1}, getCount(w));

    Penguin p5 = new Penguin(5);
    w.insert(p5);
    assertArrayEquals(new Penguin[]{p3, p2, p5, p1, null, null, null}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 1}, getCount(w));

    w.remove(p3);
    assertArrayEquals(new Penguin[]{p2, p1, p5}, getPs(w));
    assertArrayEquals(new int[]{1, 2}, getCount(w));

    Penguin p4 = new Penguin(4);
    w.insert(p4);
    assertArrayEquals(new Penguin[]{p2, p1, p5, null, null, p4, null}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 1}, getCount(w));

    Penguin p8 = new Penguin(8);
    w.insert(p8);
    assertArrayEquals(new Penguin[]{p2, p1, p5, null, null, p4, p8}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 2}, getCount(w));

    w.insert(p3);
    assertArrayEquals(new Penguin[]{p2,
        p1, p5,
        null, null, p4, p8,
        null, null, null, null, p3, null, null, null}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 2, 1}, getCount(w));

    w.insert(p4);
    assertArrayEquals(new Penguin[]{p2,
        p1, p5,
        null, null, p4, p8,
        null, null, null, null, p3, null, null, null}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 2, 1}, getCount(w));

    w.remove(p5);
    assertArrayEquals(new Penguin[]{p2,
        p1, p4,
        null, null, p3, p8}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 2}, getCount(w));

    Penguin p7 = new Penguin(7);
    w.insert(p7);
    assertArrayEquals(new Penguin[]{p2,
        p1, p4,
        null, null, p3, p8,
        null, null, null, null, null, null, p7, null}, getPs(w));
    assertArrayEquals(new int[]{1, 2, 2, 1}, getCount(w));
  }
}
