import java.util.HashSet;
import java.util.Set;

/**
 * Created by data on 27.10.17.
 */
public class DsSet {

  public static void main(String[] args) {
    for (int i = 1; i <= 4; i++) {
      set(i);
    }
  }

  private static void set(int level) {
    Set<String> set = new HashSet<>();
    set.add("a");

    for (int i = 0; i < level; i++) {
      Set<String> newSet = new HashSet<>();

      for (String elem : set) {
        newSet.add(elem);
      }
      for (String elem : set) {
        newSet.add(String.format("(%s)", elem));
      }
      for (String x : set) {
        for (String y : set) {
          newSet.add(String.format("(%s,%s)", x, y));
        }
      }
      set = newSet;
    }
    System.out.println("set:= " + set);
    System.out.println("size = " + set.size());
  }
}
