package aufgabe9_5;

public class Decoration extends SingleObject {

  public Decoration(int x, int y, int background, int foreground) {
    super(x, y, background, foreground);
  }

  @Override
  public boolean moveDown(boolean[][] staticObjects) {
//    if (!fallend) {
//      return false;
//    }
//    int downY = y + 1;
//    if (downY >= staticObjects[0].length) {
//      markedForDeath = true;
//      return false;
//    }
//    if (staticObjects[x][y]) {
//      boolean below = false;
//      for (int i = downY; i < staticObjects[0].length - 1; i++) {
//        below |= staticObjects[x][i];
//      }
//      if (!below) {
//        fallend = false;
//        return false;
//      }
//    }
//    y++;
//    return true;
//    if (y == staticObjects[x].length -2 ) {
//      markedForDeath = true;
//      return false;
//    }
    return super.moveDown(staticObjects);
  }
}
