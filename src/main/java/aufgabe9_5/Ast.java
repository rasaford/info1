package aufgabe9_5;

public class Ast extends MultiObject {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  public Ast(int x, int y, int breite) {
    super(x, y, breite);
    int left = x - breite / 2;
    parts.add(new AstLinks(left++, y));
    for (int i = 0; i < 2 * breite; i++) {
      parts.add(new AstMitte(left++, y));
    }
    parts.add(new AstRechts(left++, y));
  }
}
