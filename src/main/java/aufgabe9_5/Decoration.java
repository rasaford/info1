package aufgabe9_5;

public class Decoration extends SingleObject {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  public Decoration(int x, int y, int background, int foreground) {
    super(x, y, background, foreground);
  }

  @Override
  void delete() {
    super.delete();
    System.out.printf("deleted %s on x:%d y:%d\n", this.getClass().getSimpleName(), x, y);
  }

  @Override
  public boolean moveDown(boolean[][] staticObjects) {
    boolean res = super.moveDown(staticObjects);
    fallend = tryDownMove(staticObjects);
    return res;
  }
}
