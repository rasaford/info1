package aufgabe9_5;

import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;

public class BitteNichtAbgeben extends JPanel {

  public static final int ROTATE_0 = 0;
  public static final int ROTATE_90 = 1 << 16;
  public static final int ROTATE_180 = 2 << 16;
  public static final int ROTATE_270 = 3 << 16;

  public static final int BACKGROUND_EMPTY = 0;
  public static final int BACKGROUND_TRUNK_MIDDLE = 1 << 8;
  public static final int BACKGROUND_TRUNK_LEFT = 2 << 8;
  public static final int BACKGROUND_TRUNK_RIGHT = 3 << 8;
  public static final int BACKGROUND_GREEN_MIDDLE = 4 << 8;
  public static final int BACKGROUND_GREEN_LEFT = 5 << 8;
  public static final int BACKGROUND_GREEN_RIGHT = 6 << 8;
  public static final int BACKGROUND_SURROUNDING = 18 << 8;

  public static final int FOREGROUND_EMPTY = 0;
  public static final int FOREGROUND_SNOWFLAKE = 1;
  public static final int FOREGROUND_BAUBLE = 2;
  public static final int FOREGROUND_PENGUIN = 3;

  public static final int KEY_LEFT = 0;
  public static final int KEY_RIGHT = 1;
  public static final int KEY_UP = 2;
  public static final int KEY_DOWN = 3;
  public static final int NO_KEY = -1;

  private static Image[][][] myImage = load();

  private static int compose(int rot, int back, int fore) {
    return rot + back + fore;
  }

  private class Field extends JPanel {

    Point p;
    int x, y;

    public Field(int x, int y) {
      this.x = x;
      this.y = y;
      p = getLocation();
    }

    public void paint(Graphics g) {
      super.paint(g);
      int background = bgOf(myState[x][y]);
      if (background == BACKGROUND_SURROUNDING) {
        GradientPaint gradient =
            new GradientPaint(10, 50, Color.GRAY, getWidth(), 0, Color.DARK_GRAY);
        ((Graphics2D) g).setPaint(gradient);
      } else {
        GradientPaint gradient = new GradientPaint(0, 50, Color.WHITE, getWidth(), 0, Color.GRAY);
        ((Graphics2D) g).setPaint(gradient);
      }

      g.fillRect(p.getLocation().x, p.getLocation().y, getWidth() * 2, getHeight());

      if (background != BACKGROUND_SURROUNDING) {
        drawSymbol(g, myState[x][y]);
      }
    }

    private void drawSymbol(Graphics g, int what) {
      String iName = BitteNichtAbgeben.toString(what);
      int i1 = rtShift(what), i2 = bgShift(what), i3 = fgShift(what);
      //System.out.println(i1 +","+i2+","+i3);
      if (i1 >= image.length || i2 >= image[0].length || i3 >= image[0][0].length) {
        System.out.println("\n\nERROR: " + what + " is not a valid value!\n");
        return;
      }
      if (image[i1][i2][i3] == null) {
        System.out.println("Fehlende Datei: " + iName);
        System.exit(-1);
      }
      ((Graphics2D) g).drawImage
          (image[i1][i2][i3], 0, 0,
              getWidth(), getHeight(), 0, 0,
              image[i1][i2][i3].getWidth(null),
              image[i1][i2][i3].getHeight(null),
              null);
    }
  }

  private int[][] myState;
  private static BitteNichtAbgeben myBitteNichtAbgeben;
  private JPanel fieldPanel = new JPanel();
  private JFrame myFrame;

  private static Image[][][] load() {
    Image[][][] image = new Image[4][7][4];
    for (int i1 = 0; i1 < image.length; i1++) {
      for (int i2 = 0; i2 < image[0].length; i2++) {
        for (int i3 = 0; i3 < image[0][0].length; i3++) {
          String iName = "./pics/" + _2(i1) + _2(i2) + _2(i3) + ".png";
          File f = new File(iName);
          if (f.exists() && !f.isDirectory()) {
            //System.out.println("Loading image " + iName + ".");
            image[i1][i2][i3] = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
          } else if (i1 > 0) {
            image[i1][i2][i3] = image[i1 - 1][i2][i3];
          } else {
            System.out.println("Fehlende Datei: " + iName);
            System.exit(-1);
          }
        }
      }
    }
    return image;
  }

  public BitteNichtAbgeben() {
    image = myImage;
  }

  private BitteNichtAbgeben(int[][] state) {
    this();
    myState = new int[state.length][state[0].length];
    Field[][] field = new Field[myState.length][myState[0].length];
    for (int y = 0; y < myState[0].length; y++) {
      for (int x = 0; x < myState.length; x++) {
        field[x][y] = new Field(x, y);
        fieldPanel.add(field[x][y]);
        myState[x][y] = state[x][y];
      }
    }

    myFrame = new JFrame("Fröhliche Weihnachten!");
    fieldPanel.setLayout(new GridLayout(myState[0].length, myState.length));
    myFrame.getContentPane().add(fieldPanel);
    myFrame.setSize(IWH * myState.length, IWH * myState[0].length);
    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myFrame.addComponentListener(new ComponentHandler());
    // myFrame.setExtendedState(myFrame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
    myFrame.addKeyListener(new KeyHandler());
    myFrame.setVisible(true);

    update(state);
  }

  private void update(int[][] state) {
    for (int x = 0; x < myState.length; x++) {
      for (int y = 0; y < myState[0].length; y++) {
        myState[x][y] = state[x][y];
      }
    }
    fieldPanel.repaint();
  }

  public static void draw(int[][] myState) {
    if (myBitteNichtAbgeben == null) {
      myBitteNichtAbgeben = new BitteNichtAbgeben(myState);
      try {
        Thread.sleep(delayStart);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
    }
    while (myBitteNichtAbgeben.pause) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException ie) {
      }
    }
    myBitteNichtAbgeben.update(myState);
    try {
      Thread.sleep(myDelay);
    } catch (InterruptedException ie) {
      /* Intentionally left blank */
    }
  }

  private class ComponentHandler extends ComponentAdapter {

    @Override
    public void componentResized(ComponentEvent e) {
      repaint();
    }
  }

  private class KeyHandler extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent ke) {
      switch (ke.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
          System.exit(0);
          break;
        case KeyEvent.VK_SPACE:
          pause = !pause;
          System.out.println(pause ? "break" : "continue");
          break;
        case KeyEvent.VK_LEFT:
          step(KEY_LEFT);
          break;
        case KeyEvent.VK_RIGHT:
          step(KEY_RIGHT);
          break;
        case KeyEvent.VK_UP:
          step(KEY_UP);
          break;
        case KeyEvent.VK_DOWN:
          step(KEY_DOWN);
          break;
        default:
          break;
      }
    }
  }

  private Image[][][] image;
  private boolean pause = false;
  private static final long myDelay = 0;
  private static final long delayStart = 500;
  private static final int IWH = 20;
  private static Object cLock = new Object();
  private static String moveQueue = "";

  private void step(int direction) {
    synchronized (cLock) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
      moveQueue += "" + direction;
    }
  }

  public static int nextStep() {
    synchronized (cLock) {
      if (moveQueue.length() == 0) {
        return NO_KEY;
      }
      char c = moveQueue.charAt(0);
      moveQueue = moveQueue.substring(1, moveQueue.length());
      return c - (int) '0';
    }
  }

  private static int fgOf(int arg) {
    return arg & 0x000000FF;
  }

  private static int bgOf(int arg) {
    return arg & 0x0000FF00;
  }

  private static int rtOf(int arg) {
    return arg & 0x00FF0000;
  }

  private static int fgShift(int arg) {
    return arg & 0x000000FF;
  }

  private static int bgShift(int arg) {
    return (arg >> 8) & 0x000000FF;
  }

  private static int rtShift(int arg) {
    return (arg >> 16) & 0x000000FF;
  }

  private static String _2(int arg) {
    int x = fgOf(arg);
    return (x > 9 ? "" : "0") + x;
  }

  private static String toString(int arg) {
    String s = "";
    for (int i = 3; i >= 1; i--) {
      s += _2(arg << (8 * (i - 1)));
      if (s.length() % 2 == 1) {
        s = "0" + s;
      }
    }
    return s + ".png";
  }


  private static void generateTree(int[][] a, int xo, int yo, int nr) {
    int y = yo;
    int left = compose(ROTATE_0, BACKGROUND_GREEN_LEFT, FOREGROUND_EMPTY);
    int right = compose(ROTATE_0, BACKGROUND_GREEN_RIGHT, FOREGROUND_EMPTY);
    int middle = compose(ROTATE_0, BACKGROUND_GREEN_MIDDLE, FOREGROUND_EMPTY);
    for (int i = nr + 3; i >= 4; i--) {
      for (int x = i; x >= i - 4; x--) {
        for (int j = 0; j <= x; j++) {
          a[xo - j][y] = a[xo + j - 1][y] = middle;
        }
        a[xo - (x + 1)][y] = left;
        a[xo + x][y] = right;
        y--;
      }
    }
    for (int i = 1; i <= 3; i++) {
      a[xo][yo + i] = a[xo - 1][yo + i] = compose(ROTATE_0, BACKGROUND_TRUNK_MIDDLE,
          FOREGROUND_EMPTY);
    }
    a[xo - 2][yo + 3] = compose(ROTATE_0, BACKGROUND_TRUNK_LEFT, FOREGROUND_EMPTY);
    a[xo + 1][yo + 3] = compose(ROTATE_0, BACKGROUND_TRUNK_RIGHT, FOREGROUND_EMPTY);
  }

  public static void generateBorders(int[][] myState) {
    for (int i = 0; i < myState.length; i++) {
      for (int j = 0; j < myState[i].length; j++) {
        myState[i][j] = BACKGROUND_EMPTY;
      }
      myState[i][0] = BACKGROUND_SURROUNDING;
      myState[i][myState[i].length - 1] = BACKGROUND_SURROUNDING;
    }
    for (int j = 0; j < myState[0].length; j++) {
      myState[0][j] = BACKGROUND_SURROUNDING;
    }
    for (int j = 0; j < myState[myState.length - 1].length; j++) {
      myState[myState.length - 1][j] = BACKGROUND_SURROUNDING;
    }
  }

  public static void generateRandomThings(int[][] myState) {
    Random random = new Random();
    for (int y = 0; y < myState[0].length; y++) {
      for (int x = 0; x < myState.length; x++) {
        if (BACKGROUND_SURROUNDING != myState[x][y]) {
          if (random.nextInt(20) == 0) {
            myState[x][y] = compose(rtOf(myState[x][y]), bgOf(myState[x][y]), FOREGROUND_SNOWFLAKE);
          } else if (random.nextInt(20) == 0 && bgOf(myState[x][y]) == BACKGROUND_GREEN_MIDDLE) {
            myState[x][y] = compose(rtOf(myState[x][y]), bgOf(myState[x][y]), FOREGROUND_BAUBLE);
          } else if (random.nextInt(35) == 0
              && (bgOf(myState[x][y]) == BACKGROUND_GREEN_LEFT
              || bgOf(myState[x][y]) == BACKGROUND_GREEN_MIDDLE
              || bgOf(myState[x][y]) == BACKGROUND_GREEN_RIGHT)) {
            myState[x][y] = compose(rtOf(myState[x][y]), bgOf(myState[x][y]), FOREGROUND_PENGUIN);
          }
        }
      }
    }
  }

  public static int[][] generateLandscape(int width, int height) {
    int[][] myState = new int[width][height];
    generateBorders(myState);
    //generateTree(myState, 15, 28, 4);
    // Schneeflöckchen
    //generateRandomThings(myState);
    return myState;
  }
}
