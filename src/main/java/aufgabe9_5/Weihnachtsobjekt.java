package aufgabe9_5;


public abstract class Weihnachtsobjekt {

  protected int x, y;
  protected boolean fallend = true;
  protected boolean markedForDeath = false;

  public Weihnachtsobjekt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public abstract void addObjektToSpielfeld(int[][] spielfeld);

  public void addObjectStatic(boolean[][] staticObjects) {
    staticObjects[x][y] |= !fallend;
  }


  boolean tryDownMove(boolean[][] staticObjects) {
    return y + 1 < staticObjects[0].length &&
        !staticObjects[x][y + 1];
  }

  public boolean moveDown(boolean[][] staticObjects) {
    if (!fallend) {
      return false;
    }
    if (!tryDownMove(staticObjects)) {
      fallend = false;
      return false;
    }
    boolean prev = staticObjects[x][y];
    staticObjects[x][y] = false;
    y++;
    staticObjects[x][y] = prev;
    return true;
  }


  public int moveLeft(boolean[][] staticObjects) {
    if (!fallend) {
      return 0;
    }
    int left = x - 1;
    if (left < 1) {
      markedForDeath = true;
      return -1;
    }
    if (staticObjects[left][y]) {
      return 0;
    }
    boolean prev = staticObjects[x][y];
    staticObjects[x][y] = false;
    x = left;
    staticObjects[left][y] = prev;
    return 1;
  }


  public int moveRight(boolean[][] staticObjects) {
    if (!fallend) {
      return 0;
    }
    int right = x + 1;
    if (right >= staticObjects.length - 1) {
      markedForDeath = true;
      return -1;
    }
    if (staticObjects[right][y]) {
      return 0;
    }
    boolean prev = staticObjects[x][y];
    staticObjects[x][y] = false;
    x = right;
    staticObjects[right][y] = prev;
    return 1;
  }

}
