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
    count[height(insertIndex)] += 1;
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
   * Fix moves every node in the subtree at index up one level.
   *
   * @param index starting index of the subtree to fix
   */
  private void fix(int index) {
    if (!isValid(index) || ps[index] == null) {
      return;
    }
    ps[parent(index)] = ps[index];
    count[height(parent(index))] += 1;
    fix(left(index));
    fix(right(index));
    ps[index] = null;
    count[height(index)] -= 1;
  }

  private void removeLayer() {
    int[] newCount = new int[count.length - 1];
    for (int i = 0; i < newCount.length; i++) {
      newCount[i] = count[i];
    }
    Penguin[] newPs = new Penguin[(ps.length - 1) / 2];
    for (int i = 0; i < newPs.length; i++) {
      newPs[i] = ps[i];
    }
    count = newCount;
    ps = newPs;
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
    int leftMax = max(left(index));
    ps[index] = ps[leftMax];
    ps[leftMax] = null;
    count[height(leftMax)] -= 1;

    // The left subtree of the max might have to be moved up one to restore the BST property.
    fix(left(leftMax));
    if (count[count.length - 1] == 0) {
      removeLayer();
    }
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

  /**
   * @return index of the parent
   */
  private int parent(int index) {
    return ((index + 1) >> 1) - 1;
  }

  /**
   * @return index of the left child
   */
  private int left(int index) {
    return 2 * (index + 1) - 1;
  }

  /**
   * @return index of the right child
   */
  private int right(int index) {
    return 2 * (index + 1);
  }

  /**
   * @return true if the index is less than the current height of the tree
   */
  private boolean isValid(int index) {
    return index < (1 << count.length) - 1;
  }

  /**
   * @return height of the index in the binary tree
   */
  private int height(int index) {
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

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
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
