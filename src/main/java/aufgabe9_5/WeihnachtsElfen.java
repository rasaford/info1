package aufgabe9_5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WeihnachtsElfen {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  public static final int BACKGROUND_EMPTY = 0;
  // aufgabe9_5.Baumstamm:
  public static final int BACKGROUND_TRUNK_MIDDLE = 1 << 8;
  public static final int BACKGROUND_TRUNK_LEFT = 2 << 8;
  public static final int BACKGROUND_TRUNK_RIGHT = 3 << 8;
  // aufgabe9_5.Ast:
  public static final int BACKGROUND_GREEN_MIDDLE = 4 << 8;
  public static final int BACKGROUND_GREEN_LEFT = 5 << 8;
  public static final int BACKGROUND_GREEN_RIGHT = 6 << 8;

  public static final int FOREGROUND_EMPTY = 0;
  public static final int FOREGROUND_SNOWFLAKE = 1;
  public static final int FOREGROUND_BAUBLE = 2;
  public static final int FOREGROUND_PENGUIN = 3;

  public static final int KEY_LEFT = 0;
  public static final int KEY_RIGHT = 1;
  public static final int KEY_UP = 2;
  public static final int KEY_DOWN = 3;
  public static final int NO_KEY = -1;

  static void sortWeihnachtsbjectsByYCoordinate(List<Weihnachtsobjekt> liste) {
    Collections.sort(liste, WeihnachtsElfen.WeihnachtsobjekteComparator);
  }

  static void sortMultiObjectsByYCoordinate(List<MultiObject> liste) {
    Collections.sort(liste, WeihnachtsElfen.WeihnachtsobjekteComparator);
  }

  static void removeMarkedForDeath(List<Weihnachtsobjekt> objekte) {
    objekte.removeIf(o -> o.markedForDeath);
  }

  static List<Baumstamm> getAllBaumstaemme(ArrayList<Weihnachtsobjekt> liste) {
    return liste.stream()
        .filter(o -> o instanceof Baumstamm)
        .map(weihnachtsobjekt -> ((Baumstamm) weihnachtsobjekt))
        .collect(Collectors.toList());
  }

  static List<Ast> getAllAeste(ArrayList<Weihnachtsobjekt> liste) {
    return liste.stream()
        .filter(o -> o instanceof Ast)
        .map(weihnachtsobjekt -> (Ast) weihnachtsobjekt)
        .collect(Collectors.toList());
  }

  public static Comparator<Weihnachtsobjekt> WeihnachtsobjekteComparator
      = new Comparator<Weihnachtsobjekt>() {

    public int compare(Weihnachtsobjekt obj1, Weihnachtsobjekt obj2) {
      return obj2.y - obj1.y;
    }
  };

  public static final int FALLING_NONE = -1;
  public static final int FALLING_TRUNK = 0;
  public static final int FALLING_GREEN = 1;

  public static int[][] randomObjects;
  public static int currentRandomObject;

  public static void newRandomObject() {
    final int num = 831;
    final int no = FALLING_NONE;
    final int tr = FALLING_TRUNK;
    final int gr = FALLING_GREEN;
    if (null == randomObjects) {
      randomObjects = new int[num][2];
      currentRandomObject = num - 1;
      randomObjectParts = new int[][]
          {{tr, 1}, {tr, 0}, {tr, 0},
              {gr, 7}, {gr, 6}, {gr, 5}, {gr, 4}, {gr, 3},
              {gr, 6}, {gr, 5}, {gr, 4}, {gr, 3}, {gr, 2},
              {gr, 5}, {gr, 4}, {gr, 3}, {gr, 2}, {gr, 1},
              {gr, 4}, {gr, 3}, {gr, 2}, {gr, 1}, {gr, 0}
          };
    }
    if (currentRandomObject == num - 1) {
      for (int pos = 0; pos < randomObjects.length; pos++) {
        randomObjects[pos][0] = FALLING_NONE;
      }
      int pos = 0;
      for (int nr = 0; nr < randomObjectParts.length; nr++) {
        pos += (int) (4 * Math.random()) + 27 - nr / 3;
        if (pos >= randomObjects.length) {
          break;
        }
        randomObjects[pos][0] = randomObjectParts[nr][0];
        randomObjects[pos][1] = randomObjectParts[nr][1];
      }
      currentRandomObject = 0;
      return;
    }
    currentRandomObject++;
  }

  private static int[][] randomObjectParts;
}