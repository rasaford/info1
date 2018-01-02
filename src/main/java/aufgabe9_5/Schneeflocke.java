package aufgabe9_5;

public class Schneeflocke extends Decoration {

  private boolean moveable = false;

  public Schneeflocke(int x, int y) {
    super(x, y, 0, WeihnachtsElfen.FOREGROUND_SNOWFLAKE);
  }

  // TODO fix snowflakes movement

  @Override
  public int moveLeft(boolean[][] staticObjects) {
    if (moveable) {
      moveable = false;
      fallend = true;
      if (super.moveLeft(staticObjects) == 1 && !super.moveDown(staticObjects)) {
        super.moveRight(staticObjects);
      }
      return 1;
    }
    return 0;
  }

  @Override
  public int moveRight(boolean[][] staticObjects) {
    if (moveable) {
      moveable = false;
      fallend = true;
      if (super.moveRight(staticObjects) == 0 && !super.moveDown(staticObjects)) {
        super.moveLeft(staticObjects);
      }
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
