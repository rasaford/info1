package aufgabe9_5;

import java.util.LinkedList;
import java.util.List;

public class MultiObject extends Weihnachtsobjekt {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  protected List<SingleObject> parts;
  protected int breite;

  public MultiObject(int x, int y, int breite) {
    super(x, y);
    this.breite = breite;
    this.parts = new LinkedList<>();
  }

  @Override
  public void addObjektToSpielfeld(int[][] spielfeld) {
    for (SingleObject p : parts) {
      p.addObjektToSpielfeld(spielfeld);
    }
  }

  @Override
  public void addObjectStatic(boolean[][] staticObjects) {
    for (SingleObject p : parts) {
      p.addObjectStatic(staticObjects);
    }
  }

  @Override
  public boolean moveDown(boolean[][] staticObjects) {
    if (!fallend) {
      return false;
    }
    boolean movable = true;
    for (SingleObject s : parts) {
      movable &= s.tryDownMove(staticObjects);
    }
    if (!movable) {
      for (SingleObject o : parts) {
        o.fallend = false;
      }
      fallend = false;
      return false;
    }
    for (SingleObject s : parts) {
      s.moveDown(staticObjects);
    }
    super.moveDown(staticObjects);
    return true;
  }

  @Override
  public int moveLeft(boolean[][] staticObjects) {
    int moveStatus = 1;
    for (SingleObject s : parts) {
      if (moveStatus != 1) {
        break;
      }
      moveStatus = s.moveLeft(staticObjects);
    }
    if (moveStatus == -1) {
      for (SingleObject o : parts) {
        o.markedForDeath = true;
      }
      delete();
      return -1;
    }
    super.moveLeft(staticObjects);
    return moveStatus;
  }

  @Override
  public int moveRight(boolean[][] staticObjects) {
    int moveStatus = 1;
    for (int i = parts.size() - 1; i >= 0; i--) {
      SingleObject s = parts.get(i);
      if (moveStatus != 1) {
        break;
      }
      moveStatus = s.moveRight(staticObjects);
    }
    if (moveStatus == -1) {
      for (SingleObject o : parts) {
        o.markedForDeath = true;
      }
      delete();
      return -1;
    }
    super.moveRight(staticObjects);
    return moveStatus;
  }

  @Override
  void delete() {
    super.delete();
    System.out.printf("deleted %s on x:%d y:%d\n", this.getClass().getSimpleName(), x, y);
  }

  @Override
  public String toString() {
    return "aufgabe9_5.MultiObject{" +
        "parts=" + parts +
        ", breite=" + breite +
        '}';
  }
}
