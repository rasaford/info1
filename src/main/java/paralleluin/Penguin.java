
package paralleluin;

import java.util.Random;

public class Penguin implements Runnable {

  public static final int adultAge = 26;

  private Random random = new Random();
  private volatile boolean done;

  private boolean female;
  private int x;
  private int y;
  private int age;
  private int type;
  private Colony col;
  private volatile boolean breeding;

  public Penguin(boolean female, int x, int y, int age, Colony col) {
    this.female = female;
    this.x = x;
    this.y = y;
    this.age = age;
    this.col = col;
    if (age < adultAge) {
      type = female ? GUI.KLEINUININ : GUI.KLEINUIN;
    } else {
      type = female ? GUI.FRAUIN : GUI.MANNUIN;
    }
    System.out.printf("Spawned %s on (%d,%d) with age %d\n", this, x, y, age);
  }

  private void waitRandom() throws InterruptedException {
    int rand = 200 + random.nextInt(301);
    System.out.printf("%s waiting for %dms on (%d, %d)\n", this, rand, x, y);
    Thread.sleep(rand);
  }

  /**
   * @return if the move was successful
   */
  private boolean move() {
    int dir = random.nextInt(4);
    int newX = x;
    int newY = y;
    switch (dir) {
      case 0: // up
        newY--;
        break;
      case 1: // down
        newY++;
        break;
      case 2: // left
        newX--;
        break;
      case 3: // right
        newX++;
        break;
    }
    if (!col.isValid(newX, newY)) {
      synchronized (col.squareLocks[x][y]) {
        col.remove(x, y);
      }
      remove();
      return true;
    }
    int oldX, oldY;
    if (!col.isEmpty(newX, newY)) {
      return false;
    }
    synchronized (col.squareLocks[newX][newY]) {
      synchronized (col.squareLocks[x][y]) {
        col.move(this, x, y, newX, newY);
        oldX = x;
        oldY = y;
        x = newX;
        y = newY;
      }
    }
    System.out.printf("moving %s from (%d, %d) to (%d, %d)\n", this, oldX, oldY, newX, newY);
    return true;
  }

  private Thread spawn() {
    Penguin p = new Penguin(random.nextBoolean(), x, y, 0, col);
    return new Thread(p);
  }

  /**
   * @return if the search was successful
   */
  private boolean findPartner() {
    if (type != GUI.FRAUIN) {
      return false;
    }
    boolean ocean;
    synchronized (col.squareLocks[x][y]) {
      ocean = col.onOcean(x, y);
    }
    int newX = x - 1;
    if (!col.isValid(newX, y)) {
      return false;
    }
    Penguin other;
    boolean otherOnOcean;
    synchronized (col.squareLocks[newX][y]) {
      other = col.getPenguin(newX, y);
      if (other == null || other.isBreeding()) {
        return false;
      }
      otherOnOcean = col.onOcean(newX, y);
      if (ocean != otherOnOcean || ocean) {
        return false;
      }
    }
    if (other.getFg() != GUI.MANNUIN) {
      return false;
    }
    // 5 % chance to breed
//    if (random.nextInt(20) != 0) {
//      return false;
//    }
    try {
      if (random.nextBoolean()) {
        breed();
        return true;
      } else {
        other.setBreeding();
        return false;
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return false;
  }

  private synchronized void breed() throws InterruptedException {
    System.out.printf("\n\nBREEDING \n\n%s is breeding on (%d, %d)\n", this, x, y);
    int oldType = type;
    type = GUI.SCHWANGUIN;
    breeding = true;
    synchronized (col.squareLocks[x][y]) {
      col.setState(x, y, GUI.SCHWANGUIN);
    }
    // breeding lasts between 9 and 13 seconds
    int breedTimer = 9 + random.nextInt(5);
    breedTimer *= 1000;
    Thread.sleep(breedTimer);
    int babyX = x;
    int babyY = y;
    Thread babyPenguin = spawn();
    while (!move()) {
      System.out.println("############# unable to move #######");
    }
    babyPenguin.start();
    type = oldType;
    synchronized (col.squareLocks[x][y]) {
      col.setState(x, y, oldType);
    }
    breeding = false;
    System.out.printf("spawned baby penguin at (%d, %d)\n", babyX, babyY);
  }

  public synchronized void setBreeding() {
    if (!breeding) {
      breeding = true;
    }
  }

  public boolean isBreeding() {
    return type == GUI.SCHWANGUIN;
  }

  private void age() {
    age++;
    if (age >= adultAge && (type == GUI.KLEINUIN || type == GUI.KLEINUININ)) {
      type = female ? GUI.FRAUIN : GUI.MANNUIN;
    }
  }

  public void remove() {
    done = true;
    System.out.printf("removing %s from (%d, %d)\n", this, x, y);
  }

  public void run() {
    while (!done) {
      try {
        if (breeding) {
          breed();
        } else if (!findPartner()) {
          move();
        }
        waitRandom();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      age();
    }
  }

  public int getFg() {
    return type;
  }

  @Override
  public String toString() {
    switch (type) {
      case GUI.FRAUIN:
        return "FRAUIN";
      case GUI.MANNUIN:
        return "MANNUIN";
      case GUI.KLEINUIN:
        return "KLEINUIN";
      case GUI.KLEINUININ:
        return "KLEINUININ";
      case GUI.SCHWANGUIN:
        return "SCHWANGUIN";
      case GUI.NIXUIN:
        return "NIXUIN";
      default:
        return "";
    }
  }
}
