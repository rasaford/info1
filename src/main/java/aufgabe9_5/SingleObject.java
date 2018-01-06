package aufgabe9_5;

public class SingleObject extends Weihnachtsobjekt {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  // type of the object
  protected int background;
  protected int foreground;

  public SingleObject(int x, int y, int background, int foreground) {
    super(x, y);
    this.background = background;
    this.foreground = foreground;
  }

  @Override
  public void addObjektToSpielfeld(int[][] spielfeld) {
    spielfeld[x][y] = foreground != 0 ? (spielfeld[x][y] & 0xFF00) + foreground : spielfeld[x][y];
    spielfeld[x][y] =background != 0 ? (spielfeld[x][y] & 0xFF) + background : spielfeld[x][y];
  }


  @Override
  public String toString() {
    return "" + this.getClass().getName() + "{" +
        "x=" + x +
        ", y=" + y +
        ", background=" + background +
        ", foreground=" + foreground +
        ", markedForDeath=" + markedForDeath +
        "} ";
  }
}
