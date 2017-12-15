import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HilfPingu extends PenguinPen {

  private static final int[][] penguinPen = generateStandardPenguinPen(24, 17);
  private static int playerX = 1;
  private static int playerY = 0;
  private static List<I.Penguin> penguins = new ArrayList<>();

  public static void move(int direction) {
    if (!movePlayer(direction)) {
      return;
    }
    feed(playerX, playerY);
    draw(penguinPen);
    for (I.Penguin p : penguins) {
      p.move();
    }
    draw(penguinPen);
    if (penguins.size() == 0) {
      MiniJava.write("congratulations, you won!");
      System.exit(0);
    }
  }


  private static boolean movePlayer(int dir) {
    int[] rel = dirToRel(dir);
    int x = playerX + rel[0];
    int y = playerY + rel[1];
    if (x < 0 || y < 0 || get(x, y) == WALL) {
      return false;
    }
    System.out.printf("(%d, %d) ==> (%d, %d) -- Spielschritt.\n", playerX, playerY, x, y);
    penguinPen[playerX][playerY] = FREE;
    playerX = x;
    playerY = y;
    penguinPen[playerX][playerY] = ZOOKEEPER;
    return true;
  }

  private static int[] dirToRel(int dir) {
    switch (dir) {
      case MOVE_RIGHT:
        return new int[]{1, 0};
      case MOVE_LEFT:
        return new int[]{-1, 0};
      case MOVE_UP:
        return new int[]{0, -1};
      case MOVE_DOWN:
        return new int[]{0, 1};
      default:
        return null;
    }
  }


  private static void feed(int x, int y) {
    int[][] adjacent = new int[][]{{1, 1}, {0, 1}, {1, 1}, {-1, 0}, {0, 0}, {1, 0}, {-1, -1},
        {0, -1},
        {1, -1}};
    for (int[] adj : adjacent) {
      int absX = x + adj[0];
      int absY = y + adj[1];
      Iterator<I.Penguin> it = penguins.iterator();
      while (it.hasNext()) {
        I.Penguin p = it.next();
        if (p.x == absX && p.y == absY) {
          p.feed();
          it.remove();
          System.out.printf("(%d, %d) wird frei -- Pinguin ist satt.\n", absX, absY);
        }
      }
    }
  }

  private static int get(int x, int y) {
    if (x < 0 || y < 0) {
      return OUTSIDE;
    }
    return penguinPen[x][y];
  }

  // this is a dirty hack to allow polymorphism in a static context within a single file
  private static class I {

    private class Penguin {

      public int x = 0;
      public int y = 0;

      public Penguin(int x, int y) {
        this.x = x;
        this.y = y;
      }

      public void move() {
      }

      void movePenguin(int type, int newX, int newY) {
        penguinPen[x][y] = FREE;
        penguinPen[newX][newY] = type;
        System.out.printf("(%d, %d) ==> (%d, %d) -- Pinguin vom Typ %d flüchtet\n", x, y,
            newX, newY, type);
        x = newX;
        y = newY;
      }

      public void feed() {
        penguinPen[x][y] = FREE;
      }
    }

    private class Fauluin extends Penguin {

      public int type = PENGUIN_OOO;

      public Fauluin(int x, int y) {
        super(x, y);
      }

      @Override
      public void move() {
      }
    }

    private class Wechsulin extends Penguin {

      public int type = PENGUIN_OIO;
      private boolean hitWall = false;
      private int[] oldDir = new int[]{0, -1}; // first move has to be up

      public Wechsulin(int x, int y) {
        super(x, y);
      }

      @Override
      public void move() {
        if (!hitWall) {
          int newX = x + 1;
          int newY = y;
          if (get(newX, newY) == FREE) {
            movePenguin(type, newX, newY);
          }
          if (get(newX, newY) != WALL) {
            return;
          }
          hitWall = true;
        }
        int[] newDir = moveAlongWall();
        movePenguin(type, x + newDir[0], y + newDir[1]);
        if (newDir[0] == 0 && newDir[1] == 0) {
          return;
        }
        oldDir = newDir;
      }

      private int[] moveAlongWall() {
        int[] rightDir = rightHandVector();
        int ahead = get(x + oldDir[0], y + oldDir[1]);
        int right = get(x + rightDir[0], y + rightDir[1]);
        if (PENGUIN_OOO <= ahead && ahead <= PENGUIN_IOO ||
            PENGUIN_OOO <= right && right <= PENGUIN_IOO) {
          return new int[]{0, 0};
        }
        if (ahead == FREE && right == WALL) {
          return oldDir;
        }
        if ((ahead == FREE || ahead == WALL) && right == FREE) {
          return rightDir;
        }
        if (ahead == OUTSIDE) {
          oldDir[0] *= -1;
          oldDir[1] *= -1;
          return oldDir;
        }
        if (ahead == WALL && right == WALL) {
          rightDir[0] *= -1;
          rightDir[1] *= -1;
          right = get(x + rightDir[0], y + rightDir[1]);
          if (right == FREE) {
            return rightDir;
          }
          oldDir[0] *= -1;
          oldDir[1] *= -1;
          return oldDir;
        }
        return null; // for debugging to detect invalid configurations
      }

      private int[] rightHandVector() {
        int[] right = new int[]{0, 0};
        if (oldDir[0] == 0 && oldDir[1] == 1) {
          right = new int[]{-1, 0};
        } else if (oldDir[0] == -1 && oldDir[1] == 0) {
          right = new int[]{0, -1};
        } else if (oldDir[0] == 0 && oldDir[1] == -1) {
          right = new int[]{1, 0};
        } else if (oldDir[0] == 1 && oldDir[1] == 0) {
          right = new int[]{0, 1};
        }
        return right;
      }
    }

    private class Zufullin extends Penguin {

      int type = PENGUIN_OOI;

      public Zufullin(int x, int y) {
        super(x, y);
      }

      @Override
      public void move() {
        int[] dirs = new int[]{0, 1, 2, 3};
        // random permutation of all possible dirs
        for (int i = 0; i < dirs.length; i++) {
          int randomIndex = (int) (Math.random() * dirs.length);
          int temp = dirs[i];
          dirs[i] = dirs[randomIndex];
          dirs[randomIndex] = temp;
        }
        for (int i : dirs) {
          int[] dir = dirToRel(i);
          int newX = dir[0] + x;
          int newY = dir[1] + y;
          if (newX < 0 || newY < 0) {
            continue;
          }
          if (get(newX, newY) == FREE) {
            movePenguin(type, newX, newY);
            break;
          }
        }
      }
    }

    private class Springuin extends Penguin {

      public int type = PENGUIN_OII;

      public Springuin(int x, int y) {
        super(x, y);
      }

      @Override
      public void move() {
        int randomX = (int) (Math.random() * penguinPen.length);
        int randomY = (int) (Math.random() * penguinPen[0].length);
        if (get(randomX, randomY) != FREE) {
          move();
          return;
        }
        movePenguin(type, randomX, randomY);
      }
    }

    private class Schlauin extends Penguin {

      public int type = PENGUIN_IOO;

      public Schlauin(int x, int y) {
        super(x, y);
      }

      @Override
      public void move() {
        int[] dirs = new int[]{0, 1, 2, 3};
        // sort all possible dirs by the distance metric
        for (int i = 0; i < dirs.length; i++) {
          for (int j = 0; j < dirs.length; j++) {
            int[] relI = dirToRel(dirs[i]);
            int[] relJ = dirToRel(dirs[j]);
            if (dist(x + relI[0], y + relI[1], playerX, playerY) >
                dist(x + relJ[0], y + relJ[1], playerX, playerY)) {
              int temp = dirs[i];
              dirs[i] = dirs[j];
              dirs[j] = temp;
            }
          }
        }
        for (int i : dirs) {
          int newX = dirToRel(i)[0] + x;
          int newY = dirToRel(i)[1] + y;
          if (newX < 0 || newY < 0) {
            continue;
          }
          if (get(newX, newY) == FREE) {
            movePenguin(type, newX, newY);
            return;
          }
        }
      }

      private int dist(int x, int y, int playerX, int playerY) {
        return Math.abs(x - playerX) + Math.abs(y - playerY);
      }
    }
  }

  private static void init() {
    for (int x = 0; x < penguinPen.length; x++) {
      for (int y = 0; y < penguinPen[0].length; y++) {
        int type = get(x, y);
        switch (type) {
          case PENGUIN_OOO:
            penguins.add(new I().new Fauluin(x, y));
            break;
          case PENGUIN_OOI:
            penguins.add(new I().new Zufullin(x, y));
            break;
          case PENGUIN_OIO:
            penguins.add(new I().new Wechsulin(x, y));
            break;
          case PENGUIN_OII:
            penguins.add(new I().new Springuin(x, y));
            break;
          case PENGUIN_IOO:
            penguins.add(new I().new Schlauin(x, y));
            break;
        }
      }
    }
  }

  /*********************************************/
  /* Ab hier soll nichts mehr geändert werden! */

  /*********************************************/

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    init();
    draw(penguinPen);
    handleUserInput();
  }

  private static void handleUserInput() {
    while (true) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
      int step = nextStep();
      if (step != NO_MOVE) {
        // System.out.print(step+",");
        move(step);
      }
    }
  }
}
