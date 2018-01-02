package aufgabe9_5;

import java.util.ArrayList;
import java.util.List;


public class Weihnachtsbaum extends BitteNichtAbgeben {

  private static final int[][] landscape = generateLandscape(30, 33);
  private static List<Weihnachtsobjekt> objekte = new ArrayList<>();
  private static boolean[][] staticObjects = new boolean[30][33];
  private static boolean firstPhase = true;

  private static final int SPAWN_X = 13;
  private static final int SPAWN_Y = 1;

  public static void keyPressed(int key) {
    update(objekte);
    switch (key) {
      case KEY_LEFT:
        moveLeft();
        break;
      case KEY_RIGHT:
        moveRight();
        break;
      case KEY_UP:
        firstPhase = !firstPhase;
        System.out.println("switched phase");
      case KEY_DOWN:
        if (firstPhase) {
          firstPhase();
        } else {
          secondPhase();
        }
        moveDown();
        break;
    }
    WeihnachtsElfen.removeMarkedForDeath(objekte);
    update(objekte);
  }

  public static void firstPhase() {
    WeihnachtsElfen.newRandomObject();
    int[] obj = WeihnachtsElfen.randomObjects[WeihnachtsElfen.currentRandomObject];
    if (obj[0] == WeihnachtsElfen.FALLING_TRUNK) {
      Baumstamm b = new Baumstamm(SPAWN_X, SPAWN_Y, obj[1]);
      objekte.add(b);
      System.out.println("tree added");
    } else if (obj[0] == WeihnachtsElfen.FALLING_GREEN) {
      Ast a = new Ast(SPAWN_X, SPAWN_Y, obj[1]);
      objekte.add(a);
      System.out.println("green added");
    }
  }


  private static void moveDown() {
    for (Weihnachtsobjekt o : objekte) {
      o.moveDown(staticObjects);
    }
  }

  private static void moveLeft() {
    for (Weihnachtsobjekt o : objekte) {
      o.moveLeft(staticObjects);
    }
  }

  private static void moveRight() {
    for (Weihnachtsobjekt o : objekte) {
      o.moveRight(staticObjects);
    }
  }

  private static void secondPhase() {
  }

  /*********************************************/
  /* Ab hier soll nichts mehr geändert werden! */

  /*********************************************/

  public static void main(String[] args) {
    draw(landscape);
    handleUserInput();
  }

  public static void update(List<Weihnachtsobjekt> objs) {
    // clear the board
    for (int i = 1; i < landscape.length - 1; i++) {
      for (int j = 1; j < landscape[i].length - 1; j++) {
        landscape[i][j] = WeihnachtsElfen.FOREGROUND_EMPTY + WeihnachtsElfen.BACKGROUND_EMPTY;
      }
    }
    // clear the static objects
    for (int i = 0; i < staticObjects.length; i++) {
      for (int j = 0; j < staticObjects[i].length; j++) {
        staticObjects[i][j] = i == 0 ||
            j == 0 ||
            i == staticObjects.length - 1 ||
            j == staticObjects[i].length - 1;
      }
    }
    // redraw
    for (Weihnachtsobjekt w : objs) {
      w.addObjektToSpielfeld(landscape);
      w.addObjectStatic(staticObjects);
    }
    draw(landscape);
  }

  private static void handleUserInput() {
    while (true) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
      int step = nextStep();
      if (step != NO_KEY) {
        System.out.print(step + ",");
        keyPressed(step);
      }
    }
  }
}
