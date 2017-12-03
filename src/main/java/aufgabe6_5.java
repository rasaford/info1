public class aufgabe6_5 extends Maze {

  private static int[][] maze;

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    int size = getIntInRange(0, Integer.MAX_VALUE,
        "How large do you want the maze to be?");
    int maxDist = getIntInRange(0, Integer.MAX_VALUE,
        "How far are you willing to go from the start?");
    init(size);
    int score = walk(1, 0, maxDist);
    MiniJava.write(String.format("DONE! Penguins saved: %d", score));
  }

  private static void init(int size) {
    maze = generateMaze(size);
  }

  public static int[][] generateMaze(int size) {
    return generatePenguinMaze(size, size);
  }

  public static int walk(int x, int y, int maxDistance) {
    if (!nearWall(x, y) || maxDistance == 0) {
      return 0;
    }
    int score = score(x, y);
    maze[x][y] = PLAYER;
    draw(maze);
    int[][] validTiles = validTiles(x, y);
    for (int[] tile : sort(validTiles)) {
      int newX = tile[0];
      int newY = tile[1];
      if (maze[newX][newY] != OLD_PATH_DONE) {
        maze[x][y] = OLD_PATH_ACTIVE;
        score += walk(newX, newY, maxDistance - 1);
        maze[x][y] = PLAYER;
      }
    }
    draw(maze);
    maze[x][y] = OLD_PATH_DONE;
    return score;
  }

  public static int[][] validTiles(int x, int y) {
    int[][] adjacent = new int[][]{
        {0, -1}, // topCenter
        {-1, 0}, // centerLeft
        {1, 0}, // centerRight
        {0, 1}, // bottomCenter
    };
    int[][] validPositions = new int[0][];
    for (int[] pos : adjacent) {
      int absX = x + pos[0];
      int absY = y + pos[1];
      if (absX == -1 || absY == -1) {
        continue;
      }
      int type = maze[absX][absY];
      if (type == FREE || type == PENGUIN) {
        int[][] tempPos = new int[validPositions.length + 1][];
        copy(validPositions, tempPos);
        tempPos[validPositions.length] = new int[]{absX, absY};
        validPositions = tempPos;
      }
    }
    return validPositions;
  }

  private static <T> void copy(T[] oldArray, T[] newArray) {
    if (oldArray.length > newArray.length) {
      return;
    }
    for (int i = 0; i < oldArray.length; i++) {
      newArray[i] = oldArray[i];
    }
  }

  private static boolean nearWall(int x, int y) {
    int[][] adjacent = new int[][]{
        {-1, -1}, // topLeft
        {0, -1}, // topCenter
        {1, -1}, // topRight
        {-1, 0}, // centerLeft
        {1, 0}, // centerRight
        {-1, 1}, // bottomLeft
        {0, 1}, // bottomCenter
        {1, 1} // bottomRight
    };
    for (int[] offset : adjacent) {
      int absX = x + offset[0];
      int absY = y + offset[1];
      // is it a valid index?
      if (absX == -1 || absY == -1) {
        continue;
      }
      if (maze[absX][absY] == WALL) {
        return true;
      }
    }
    return false;
  }

  /**
   * descending sort of the valid tiles array by score.
   *
   * @param array to be sorted
   */
  private static int[][] sort(int[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array.length; j++) {
        if (score(array[i][0], array[i][1]) > score(array[j][0], array[j][1])) {
          int[] temp = array[i];
          array[i] = array[j];
          array[j] = temp;
        }
      }
    }
    return array;
  }


  /**
   * value function for tile evaluation.
   *
   * @param y /***
   * @return value function for tile evaluation.
   */
  private static int score(int x, int y) {
    return (maze[x][y] == PENGUIN) ? 1 : 0;
  }

  /**
   * getIntInRange gets an int from the user in the given range.
   *
   * @param min min of the input value (inclusive)
   * @param max max of the input value (exclusive)
   * @param dialog Message to display to the user
   * @return user input
   */

  private static int getIntInRange(int min, int max, String dialog) {
    int input = MiniJava.readInt(dialog);
    if (input < min || input >= max) {
      MiniJava.write(
          String.format("The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max, dialog);
    }
    return input;
  }
}
