public class Worstorage {

  public Worstorage() {
    // TODO
  }

  public void insert(Penguin penguin) {
    // TODO
  }

  public boolean find(Penguin penguin) {
    // TODO
    return false;
  }

  public void remove(Penguin penguin) {
    // TODO
  }

  @Override
  public String toString() {
    // TODO
    return null;
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
    if (cuddliness < oc)
      return -1;
    if (cuddliness > oc)
      return 1;
    return 0;
  }
}

class Test {

  public static void main() {
    System.out.println("Hier passiert was.");
  }
}
