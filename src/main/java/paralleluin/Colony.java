package paralleluin;

public class Colony extends GUI {

  private final int[][] landscape;
  private final Penguin[][] placed;

  public final Object[][] squareLocks;
  public final Object drawLock = new Object();


  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public Colony(int width, int height, boolean standard) {
    placed = new Penguin[width][height];
    landscape = new int[placed.length][placed[0].length];
    squareLocks = new Object[placed.length][placed[0].length]; // all still null
    generateAntarctic(landscape, placed, standard);
    draw();
    for (int x = 0; x < squareLocks.length; x++) {
      for (int y = 0; y < squareLocks[x].length; y++) {
        squareLocks[x][y] = new Object();
      }
    }
  }

  public void startSimulation() {
    int penguins = 0;
    start:
    for (int x = 0; x < placed.length; x++) {
      for (int y = 0; y < placed[x].length; y++) {
        if (placed[x][y] != null) {
          Penguin p = placed[x][y];
          Thread t1 = new Thread(p);
          String name = Long.toHexString(System.nanoTime());
          t1.setName(name.substring(name.length() - 4, name.length()));
          t1.start();
          penguins++;
//          break start;
        }
      }
    }
    System.out.println("penguins = " + penguins);
  }

  private void draw() {
    synchronized (drawLock) {
      draw(landscape);
    }
  }


  public void remove(int x, int y) {
    setForeground(landscape, x, y, GUI.NIXUIN);
    placed[x][y] = null;
    draw();
  }

  /**
   * only to be called when the current Thread has the locks for both (x, y) AND (xNew, yNew)
   */
  public void move(Penguin peng, int x, int y, int xNew, int yNew) {
    setForeground(landscape, x, y, GUI.NIXUIN);
    placed[x][y] = null;
    if (!isValid(xNew, yNew)) {
      peng.remove();
      return;
    }
    setForeground(landscape, xNew, yNew, peng.getFg());
    placed[xNew][yNew] = peng;
    draw();
  }

  public boolean isValid(int x, int y) {
    return 0 <= x && x < landscape.length && 0 <= y && y < landscape[0].length;
  }

  public Penguin getPenguin(int x, int y) {
    return placed[x][y];
  }

  public void setState(int x, int y, int fg) {
    setForeground(landscape, x, y, fg);
  }

  /**
   * only to be called if the current Thread has the lock for (x, y)
   */
  public boolean isEmpty(int x, int y) {
    return isValid(x, y) && ((landscape[x][y] & 0xFF) == GUI.NIXUIN);
  }

  public boolean onOcean(int x, int y) {
    return ocean(landscape, x, y);
  }
}
