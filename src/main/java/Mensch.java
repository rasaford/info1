public class Mensch extends Aerger {

  private static int[][] players;
  private static int currentPlayer;

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    players = new int[][]{
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1}};
    gameLoop();
  }

  private static void gameLoop() {
    currentPlayer = 0;
    draw();
    int winner;
    int prevDice = 0;
    int prevPlayer = 0;

    while ((winner = winner(players)) == -1) {
      int fig = getIntInRange(1, 5, String.format(
          "%s rolled a %d\n"
              + "It's %s's turn.\n"
              + "What figure do you want to move?",
          resolveName(prevPlayer), prevDice, resolveName(currentPlayer))
      ) - 1;
      int dice = dice();
      boolean success = move(currentPlayer, fig, dice);
      draw();
      if (!success) {
        write(String.format("Couldn't move Figure %d to field %d",
            fig + 1, get(currentPlayer, fig)));
      } else {
        prevDice = dice;
        prevPlayer = currentPlayer;
        nextPlayer();
      }
      draw();
    }
    write(String.format("%s won! Congratulations.", resolveName(winner)));
  }

  private static int get(int player, int fig) {
    return players[player][fig];
  }

  private static String resolveName(int index) {
    switch (index) {
      case 0:
        return "Yellow";
      case 1:
        return "Blue";
      case 2:
        return "Green";
      case 3:
        return "Red";
      default:
        return "";
    }
  }

  private static boolean move(int player, int fig, int offset) {
    int current = players[player][fig];
    int startPos = player * 10;
    // is figure done ?
    if (current == 40) {
      return false;
    }
    // is this the figures first move ?
    if (current == -1) {
      players[player][fig] = (startPos + offset) % 40;
      return true;
    }
    int newPos = current + offset;
    if (before(current, startPos) && !before(current + offset, startPos)) {
      players[player][fig] = 40;
      return true;
    } else {
      newPos %= 40;
      return kickFigure(player, newPos, fig);
    }
  }

  public static boolean before(int a, int b) {
    int steps = 0;
    while (a != b) {
      a = (a + 1) % 40;
      steps++;
    }
    return steps < 20;
  }

  public static boolean kickFigure(int player, int newPos, int fig) {
    for (int i = 0; i < players[player].length; i++) {
      if (players[player][i] == newPos) {
        return false;
      }
    }
    for (int i = 0; i < players.length; i++) {
      if (i == player) {
        continue;
      }
      // reset the other figure
      for (int j = 0; j < players[i].length; j++) {
        if (players[i][j] == newPos) {
          players[i][j] = -1;
        }
      }
    }
    // set players figure to the newPosition
    players[player][fig] = newPos;
    return true;
  }


  public static int winner(int[][] players) {
    for (int i = 0; i < players.length; i++) {
      boolean winner = true;
      for (int j : players[i]) {
        winner &= j >= 40;
      }
      if (winner) {
        return i;
      }
    }
    return -1;
  }

  private static void draw() {
    try {
      Thread.sleep(20);
    } catch (InterruptedException e) {
    }
    // Yellow | Blue | Red | Green
    paintField(players[0], players[1], players[3], players[2]);
  }

  private static void nextPlayer() {
    currentPlayer = (currentPlayer + 1) % 4;
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
    int input = readInt(dialog);
    if (input < min || input >= max) {
      write(String.format("The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max, dialog);
    }
    return input;
  }
}
