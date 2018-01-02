package aufgabe9_5;

public class Baumstamm extends MultiObject {

  public Baumstamm(int x, int y, int breite) {
    super(x, y, breite);
    int left = x - breite / 2;
    if (breite == 0) {
      parts.add(new StammMitte(left++, y));
      parts.add(new StammMitte(left++, y));
      return;
    }
    parts.add(new StammLinks(left++, y));
    for (int i = 0; i < 2 * breite; i++) {
      parts.add(new StammMitte(left++, y));
    }
    parts.add(new StammRechts(left++, y));
  }
}
