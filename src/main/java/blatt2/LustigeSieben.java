package blatt2;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by data on 01.11.17.
 */
public class LustigeSieben {

  private static Scanner s = new Scanner(System.in);

  public static void main(String[] args) {
    int cash = 100;
    while (true) {
      System.out.println("enter the fields you want to pick, then the stake you want so set on it");
      int field = inputInRange(2, 13);
      int stake = inputInRange(1, cash + 1);
      cash -= stake;
      int roll = roll();
      System.out.printf("you put %d dollars on %d and rolled a %d\n", stake, field, roll);
      cash += eval(roll, field, stake);

      if (cash <= 0) {
        System.out.println("you're out of cash, goodbye.");
      }
      System.out.printf("you currently have %d dollars, do you want to continue playing?\n"
          + "enter 0 - if not, any other digit otherwise\n", cash);
      if (inputInRange(0, Integer.MAX_VALUE) == 0) {
        System.out.println("don't spend it all in one place ;)");
        break;
      }
    }
  }

  private static int roll() {
    Random r = new Random();
    return r.nextInt(6) + r.nextInt(6) + 2;
  }

  private static int inputInRange(int low, int high) {
    int res = 0;
    try {
      res = s.nextInt();
    } catch (InputMismatchException e) {
      s.next();
      System.out.println("the input value is not an integer, please try again");
      return inputInRange(low, high);
    }
    if (res < low || res >= high) {
      System.out
          .printf("the input value is not in the range %d -> %d enter a new value\n", low, high);
      return inputInRange(low, high);
    }
    return res;
  }

  private static int eval(int res, int field, int stake) {
    if (res == 7 && field == 7) {
      return stake * 3;
    }
    if (res == field) {
      return stake * 2;
    }
    if (res == 7) {
      return stake;
    }
    if (sameRange(2, 6, res, field) || sameRange(8, 12, res, field)) {
      return stake;
    }
    return 0;
  }

  private static boolean sameRange(int low, int high, int val1, int val2) {
    return (low <= val1 && val1 <= high) && (low <= val2 && val2 <= high);
  }
}
