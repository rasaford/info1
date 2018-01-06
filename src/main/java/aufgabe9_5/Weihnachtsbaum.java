package aufgabe9_5;

import java.util.ArrayList;


public class Weihnachtsbaum extends BitteNichtAbgeben {
  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode

  private static final int[][] landscape = generateLandscape(30, 33);
  private static ArrayList<Weihnachtsobjekt> objekte = new ArrayList<>();
  private static boolean[][] staticObjects = new boolean[30][33];
  private static boolean[][] staticObjectsForeground = new boolean[30][33];
  private static boolean firstPhase = true;

  private static final int SPAWN_X = 13;
  private static final int SPAWN_Y = 1;

  public static void keyPressed(int key) {
    update(objekte);
    spawnSnow();
    switch (key) {
      case KEY_LEFT:
        moveLeft();
        break;
      case KEY_RIGHT:
        moveRight();
        break;
      case KEY_UP:
        firstPhase = !firstPhase;
        System.out.printf("switched to phase %s\n", firstPhase ? "one" : "two");
      case KEY_DOWN:
        if (firstPhase) {
          spawnXMasTree();
        } else {
          spawnDecoration();
        }
        moveDown();
        break;
    }
    WeihnachtsElfen.removeMarkedForDeath(objekte);
    update(objekte);
  }

  public static void spawnXMasTree() {
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

  private static void spawnDecoration() {
    int type = (int) (Math.random() * 9);
    int xPos = (int) (Math.random() * 28) + 1;
    switch (type) {
      case 0:
        objekte.add(new Schneeflocke(xPos, SPAWN_Y));
        System.out.printf("Snowflake added on x:%d y:%d\n", xPos, SPAWN_Y);
        break;
      case 1:
        objekte.add(new Weihnachtskugel(xPos, SPAWN_Y));
        System.out.printf("Bauble added on x:%d y:%d\n", xPos, SPAWN_Y);
        break;
      case 2:
        objekte.add(new Pinguin(xPos, SPAWN_Y));
        System.out.printf("Penguin added on x:%d y:%d\n", xPos, SPAWN_Y);
        break;
    }
  }

  private static void spawnSnow() {
    int randX = (int) (Math.random() * 28) + 1;
    int randY = (int) (Math.random() * 31) + 1;
    while ((landscape[randX][randY] & 0xFF) != FOREGROUND_EMPTY) {
      randX = (int) (Math.random() * 28) + 1;
      randY = (int) (Math.random() * 31) + 1;
    }
    objekte.add(new Schneeflocke(randX, randY));
  }

  private static void moveDown() {
    for (Weihnachtsobjekt o : objekte) {
      if (o instanceof Decoration) {
        o.moveDown(staticObjectsForeground);
      } else {
        o.moveDown(staticObjects);
      }
    }
  }

  private static void moveLeft() {
    for (Weihnachtsobjekt o : objekte) {
      if (o instanceof Decoration) {
        o.moveLeft(staticObjectsForeground);
      } else {
        o.moveLeft(staticObjects);
      }
    }
  }

  private static void moveRight() {
    for (Weihnachtsobjekt o : objekte) {
      if (o instanceof Decoration) {
        o.moveRight(staticObjectsForeground);
      } else {
        o.moveRight(staticObjects);
      }
    }
  }

  /*********************************************/
  /* Ab hier soll nichts mehr geändert werden! */

  /*********************************************/

  public static void main(String[] args) {
    draw(landscape);
    handleUserInput();
  }

  public static void update(ArrayList<Weihnachtsobjekt> objs) {
    // clear the board
    for (int i = 1; i < landscape.length - 1; i++) {
      for (int j = 1; j < landscape[i].length - 1; j++) {
        landscape[i][j] = WeihnachtsElfen.FOREGROUND_EMPTY + WeihnachtsElfen.BACKGROUND_EMPTY;
      }
    }
    // clear the static objects
    for (int i = 0; i < staticObjects.length; i++) {
      for (int j = 0; j < staticObjects[i].length; j++) {
        boolean val = i == 0 || j == 0 ||
            i == staticObjects.length - 1 || j == staticObjects[i].length - 1;
        staticObjects[i][j] = val;
        staticObjectsForeground[i][j] = val;
      }
    }
    // redraw
    objs.forEach(o -> o.addObjektToSpielfeld(landscape));
    // redraw staticObjects for background
    objs.stream()
        .filter(o -> o instanceof Ast || o instanceof Baumstamm)
        .forEach(o -> o.addObjectStatic(staticObjects));
    // redraw staticObjects for foreground
    objs.stream()
        .filter(o -> o instanceof Ast)
        .map(o -> (Ast) o)
        .filter(ast -> !ast.fallend)
        .sorted(WeihnachtsElfen.WeihnachtsobjekteComparator)
        .forEachOrdered(ast ->
            ast.parts.forEach(a -> {
              boolean occupied = false;
              for (int i = a.y + 1; i < staticObjectsForeground[a.x].length - 1; i++) {
                occupied |= staticObjectsForeground[a.x][i];
              }
              staticObjectsForeground[a.x][a.y + 1] = !occupied;
            }));
    objs.stream()
        .filter(o -> o instanceof Decoration)
        .forEach(o -> o.addObjectStatic(staticObjectsForeground));
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
        switch (step) {
          case KEY_UP:
            System.out.println("KEY UP");
            break;
          case KEY_DOWN:
            System.out.println("KEY DOWN");
            break;
          case KEY_LEFT:
            System.out.println("KEY LEFT");
            break;
          case KEY_RIGHT:
            System.out.println("KEY RIGHT");
            break;
        }
        keyPressed(step);
      }
    }
  }
}
