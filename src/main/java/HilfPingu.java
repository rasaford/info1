public class HilfPingu extends PenguinPen {

  private static final int[][] penguinPen = generatePenguinPen(24, 17);

  public static void move(int direction) {
    System.out.println(direction);
  }








  /*********************************************/
  /* Ab hier soll nichts mehr geändert werden! */
  /*********************************************/

  public static void main(String[] args) {
    draw(penguinPen);
    handleUserInput();
  }

  private static void handleUserInput() {
    while(true) {
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
