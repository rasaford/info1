package aufgabe9_5;


public class Weihnachtsobjekt {

  protected int x, y;
  protected boolean fallend = true;
  protected boolean markedForDeath = false;

  public Weihnachtsobjekt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void addObjektToSpielfeld(int[][] spielfeld) {
    spielfeld[x][y] = 1;
  }


  public boolean moveDown(boolean[][] staticObjects) {
    if (staticObjects[x][y]) {
      return false;
    }
    y--;
    return true;
  }


  public int moveLeft(boolean[][] staticObjects) {
    if (staticObjects[x][y]) {
      return -1;
    }
    if (x - 1 < 0) {
      x--;
    } else {
      markedForDeath = true;
    }
    return 1;
  }


  public int moveRight(boolean[][] staticObjects) {
    if (staticObjects[x][y]) {
      return -1;
    }
    if (x + 1 < staticObjects.length) {
      x++;
    } else {
      markedForDeath = true;
    }
    return 1;
  }
}
