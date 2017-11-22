package blatt2;

import java.util.Random;

/**
 * Created by data on 01.11.17.
 */
public class Meiern {

  public static void main(String[] args) {
    String player = "Human";
    int prev = 0;
    while (true) {
      int res = roll();
      System.out.printf("%s rolled %d : score: %d \n", player, res, eval(res));

      if (eval(res) < eval(prev)) {
        System.out.printf("the winner is: %s\n", player.equals("Human") ? "AI" : "Human");
        break;
      }

      prev = res;
      player = player.equals("Human") ? "AI" : "Human";
    }
  }

  private static int roll() {
    Random r = new Random();
    r.setSeed(System.nanoTime());
    int a = r.nextInt(6) + 1;
    int b = r.nextInt(6) + 1;
    if (a < b) {
      int temp = a;
      a = b;
      b = temp;
    }
    return a * 10 + b;
  }

  private static int eval(int res) {
    int points = 100;
    if (res == 21) {
      return points;
    }
    for (int i = 66; i >= 11; i -= 11) {
      points--;
      if (res == i) {
        return points;
      }
    }
    for (int i = 65; i >= 31; i--) {
      points--;
      if (res == i) {
        return points;
      }
    }
    return -1;
  }
}
