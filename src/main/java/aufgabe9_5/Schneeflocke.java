package aufgabe9_5;

public class Schneeflocke extends Decoration {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private boolean moveable = false;

  public Schneeflocke(int x, int y) {
    super(x, y, 0, WeihnachtsElfen.FOREGROUND_SNOWFLAKE);
  }


  @Override
  public int moveLeft(boolean[][] staticObjects) {
    if (moveable) {
      moveable = false;
      fallend = true;
      if (super.moveLeft(staticObjects) == 1 && !super.moveDown(staticObjects)) {
        fallend = true;
        super.moveRight(staticObjects);
      }
      fallend = tryDownMove(staticObjects);
      return 1;
    }
    return 0;
  }

  @Override
  public int moveRight(boolean[][] staticObjects) {
    if (moveable) {
      moveable = false;
      fallend = true;
      if (super.moveRight(staticObjects) == 1 && !super.moveDown(staticObjects)) {
        fallend = true;
        super.moveLeft(staticObjects);
      }
      fallend = tryDownMove(staticObjects);
      return 1;
    }
    return 0;
  }

  @Override
  public boolean moveDown(boolean[][] staticObjects) {
    boolean move = super.moveDown(staticObjects);
    moveable = !move;
    return move;
  }
}
