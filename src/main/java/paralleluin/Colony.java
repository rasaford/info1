package paralleluin;

public class Colony extends GUI {

  private final int[][] landscape;
  private final Penguin[][] placed;

  public final Object[][] squareLocks;
  public final Object drawLock = new Object();


  public Colony(int width, int height,boolean standard) {
    placed = new Penguin[width][height];
    landscape = new int[placed.length][placed[0].length];
    squareLocks = new Object[placed.length][placed[0].length]; // all still null
    generateAntarctic(landscape,placed,standard);

    // TODO Simulation hier starten

    synchronized(drawLock) {
      draw(landscape);
    }
  }

  public void move(Penguin peng, int x, int y, int xNew, int yNew) {
    // TODO
  }
}
