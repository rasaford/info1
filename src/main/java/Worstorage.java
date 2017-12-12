import java.util.ArrayList;

public class Worstorage {

  private Penguin[] ps;
  private int[] count;

  public Worstorage() {
    int k = 1;
    int n = (1 << k) - 1;
    ps = new Penguin[n];
    count = new int[k];
  }

  public void insert(Penguin penguin) {
    if (penguin == null) {
      return;
    }
    int insertIndex = search(penguin, 0);
    if (!isValid(insertIndex)) {
      tableDouble();
    }
    if (penguin.equals(ps[insertIndex])) {
      return;
    }
    ps[insertIndex] = penguin;
    count[level(insertIndex)] += 1;
  }

  private void tableDouble() {
    int[] newCount = new int[count.length + 1];
    for (int i = 0; i < count.length; i++) {
      newCount[i] = count[i];
    }
    count = newCount;
    Penguin[] newPs = new Penguin[(1 << count.length) - 1];
    for (int i = 0; i < ps.length; i++) {
      newPs[i] = ps[i];
    }
    ps = newPs;
  }

  /**
   * performs binary search on the array
   *
   * @return index of the penguin if contained, -1 else
   */
  private int search(Penguin penguin, int index) {
    Penguin node;
    if (!isValid(index) || (node = ps[index]) == null) {
      return index;
    }
    if (penguin.compareTo(node) == 0) {
      return index;
    }
    if (penguin.compareTo(node) > 0) {
      return search(penguin, right(index));
    } else {
      return search(penguin, left(index));
    }
  }

  /**
   * restores the BST property for the subtree at the index
   */
  public void delete(ArrayList<Penguin> p, int index) {
    if (!isValid(index) || ps[index] == null) {
      return;
    }
    p.add(ps[index]);
    delete(p, left(index));
    delete(p, right(index));
    ps[index] = null;
  }

  public boolean find(Penguin penguin) {
    if (penguin == null) {
      return false;
    }
    int index = search(penguin, 0);
    if (!isValid(index) || ps[index] == null) {
      return false;
    }
    return true;
  }

  public void remove(Penguin penguin) {
    if (penguin == null) {
      return;
    }
    int index = search(penguin, 0);
    if (!isValid(index) || !penguin.equals(ps[index])) {
      return;
    }
    int maxRight = max(right(index));
    ps[index] = ps[maxRight];
    ps[maxRight] = null;
    count[level(maxRight)] -= 1;

    ArrayList<Penguin> penguins = new ArrayList<>();
    delete(penguins, left(maxRight));
    for (Penguin p : penguins) {
      insert(p);
    }
  }

  private void swap(Penguin[] array, int a, int b) {
    Penguin temp = array[a];
    array[a] = array[b];
    array[b] = temp;
  }


  /**
   * max returns the max index of the right subtree
   */
  private int max(int index) {
    int right = right(index);
    if (!isValid(right) || ps[right] == null) {
      return index;
    }
    return max(right);
  }

  private int left(int index) {
    return 2 * (index + 1) - 1;
  }

  private int right(int index) {
    return 2 * (index + 1);
  }

  private boolean isValid(int index) {
    return index < (1 << count.length) - 1;
  }

  private int level(int index) {
    index++;
    return index < 2 ? 0 : (int) (Math.log(index) / Math.log(2));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Penguin p : ps) {
      sb.append(p == null ? "," : p.getCuddliness() + ",");
    }
    return sb.toString();
  }

  // Zum Testen (Code sollte außerhalb der zu testenden Klasse liegen)
  public static void main(String[] args) {
    Test.main();
  }
}

class Penguin implements Comparable<Penguin> {

  private int cuddliness;

  public Penguin(int cuddliness) {
    this.cuddliness = cuddliness;
  }

  public int getCuddliness() {
    return this.cuddliness;
  }

  public void setCuddliness(int cuddliness) {
    this.cuddliness = cuddliness;
  }

  // Note: this class has a natural ordering that is inconsistent with equals.
  public int compareTo(Penguin other) {
    int oc = other.cuddliness;
    if (cuddliness < oc) {
      return -1;
    }
    if (cuddliness > oc) {
      return 1;
    }
    return 0;
  }
}

class Test {

  public static void main() {
    System.out.println("Hier passiert was.");
  }
}
