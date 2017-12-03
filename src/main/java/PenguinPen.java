import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;

public class PenguinPen extends JPanel {
  public static final int WALL = -3;
  public static final int FREE = -2;
  public static final int OUTSIDE = -1;
  public static final int ZOOKEEPER = 0;
  public static final int PENGUIN_OOO = 1;
  public static final int PENGUIN_OOI = 2;
  public static final int PENGUIN_OIO = 3;
  public static final int PENGUIN_OII = 4;
  public static final int PENGUIN_IOO = 5;

  public static final int MOVE_LEFT  = 0;
  public static final int MOVE_RIGHT = 1;
  public static final int MOVE_UP    = 2;
  public static final int MOVE_DOWN  = 3;
  public static final int NO_MOVE    = -1;

  private class Field extends JPanel {
    Point p; int x,y;
    public Field(int x, int y) {
      this.x = x;
      this.y = y;
      p = getLocation();
    }
    public void paint(Graphics g) {
      super.paint(g);
      if (myState[x][y] == WALL) {
        GradientPaint gradient =
            new GradientPaint(10, 50, Color.GRAY, getWidth(), 0, Color.DARK_GRAY);
        ((Graphics2D) g).setPaint(gradient);
      } else {
        GradientPaint gradient = new GradientPaint(0, 50, Color.WHITE, getWidth(), 0, Color.GRAY);
        ((Graphics2D) g).setPaint(gradient);
      }

      g.fillRect(p.getLocation().x, p.getLocation().y, getWidth() * 2, getHeight());

      switch (myState[x][y]) {
        case ZOOKEEPER:
          paintSymbol(g, Color.RED);
          break;
        case PENGUIN_OOO:
          drawPeng(g, 0);
          break;
        case PENGUIN_OOI:
          drawPeng(g, 1);
          break;
        case PENGUIN_OIO:
          drawPeng(g, 2);
          break;
        case PENGUIN_OII:
          drawPeng(g, 3);
          break;
        case PENGUIN_IOO:
          drawPeng(g, 4);
          break;
        default:
          break;
      }
    }
    private void paintSymbol(Graphics g, Color c) {
      GradientPaint gradient = new GradientPaint(15, 0, c, getWidth(), 0, Color.LIGHT_GRAY);
      ((Graphics2D) g).setPaint(gradient);
      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      g.fillOval((int) (getWidth() * 0.3), (int) (getHeight() * 0.3), (int) (getWidth() * 0.5),
          (int) (getHeight() * 0.5));
    }
    private void drawPeng(Graphics g, int index) {
      if (peng[index] == null) {
        if (index == 0) paintSymbol(g, Color.YELLOW);
        if (index == 1) paintSymbol(g, Color.BLUE);
        if (index == 2) paintSymbol(g, Color.BLACK);
        if (index == 3) paintSymbol(g, Color.MAGENTA);
        if (index == 4) paintSymbol(g, Color.GREEN);
        return;
      }
      ((Graphics2D) g).drawImage
       (peng[index], 0, 0,
        getWidth(), getHeight(), 0, 0,
        peng[index].getWidth(null),
        peng[index].getHeight(null),
        null);
    }
  }

  private int[][] myState;
  private static PenguinPen myPenguinPen;
  private JPanel fieldPanel = new JPanel();
  private JFrame myFrame;

  public PenguinPen() {
    for (int i = 1; i <= 5; i++) {
      File f = new File("tux" + i + ".png");
      if(f.exists() && !f.isDirectory()) { 
         peng[i-1] = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
      }
    }
  }

  private PenguinPen(int[][] state) {
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

    myFrame = new JFrame("Hilf Pingu!");
    fieldPanel.setLayout(new GridLayout(myState[0].length, myState.length));
    myFrame.getContentPane().add(fieldPanel);
    myFrame.setSize
      ((int)(IWH * scale) * myState.length,
       (int)(IWH * scale) * myState[0].length);
    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myFrame.addComponentListener(new ComponentHandler());
    myFrame.addKeyListener(new KeyHandler());
    myFrame.setVisible(true);

    update(state);
  }
  private void update(int[][] state) {
    for (int x = 0; x < myState.length; x++)
      for (int y = 0; y < myState[0].length; y++)
        myState[x][y] = state[x][y];
    fieldPanel.repaint();
  }

  public static void draw(int[][] myState) {
    if (myPenguinPen == null) {
      myPenguinPen = new PenguinPen(myState);
      try {
         Thread.sleep(100);
      } catch (InterruptedException ie) {
         /* Intentionally left blank */
      }
    }
    while (myPenguinPen.pause) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException ie) {}
    }
    myPenguinPen.update(myState);
    try {
      Thread.sleep(wannaSleep);
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
          System.out.println(pause?"break":"continue");
          break;
        case KeyEvent.VK_LEFT:
          step(MOVE_LEFT);
          break;
        case KeyEvent.VK_RIGHT:
          step(MOVE_RIGHT);
          break;
        case KeyEvent.VK_UP:
          step(MOVE_UP);
          break;
        case KeyEvent.VK_DOWN:
          step(MOVE_DOWN);
          break;
        default:
          break;
      }
    }
  }

  private Image[] peng = new Image[5];
  private static final long wannaSleep = 100;
  private static final int IWH = 40;
  private static double scale = 1.0;
  private boolean pause = false;
  private static Object cLock = new Object();
  private static String moveQueue = "";

  private void step(int direction) {
    synchronized(cLock) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
      moveQueue+=""+direction;
    }
  }

  public static int nextStep() {
    synchronized(cLock) {
      if(moveQueue.length() == 0) {
        return NO_MOVE;
      }
      char c = moveQueue.charAt(0);
      moveQueue = moveQueue.substring(1,moveQueue.length());
      return c-(int)'0';
    }
  }

  public static int[][] generatePenguinPen(int width, int height) {
    int seed = (new Random()).nextInt();
    System.out.println("generatePenguinPen: seed=" + seed);
    return generatePenguinPen(width, height, seed);
  }

  public static int[][] generateStandardPenguinPen(int width, int height) {
    return generatePenguinPen(width, height, 0);
  }

  public static int[][] generatePenguinPen(int width, int height, int seed) {
    width = Math.max(width, 3);
    height = Math.max(height, 3);

    int[][] myState = new int[width][height];

    // Borders:
    for (int i = 0; i < myState.length; i++) {
      for (int j = 0; j < myState[i].length; j++) {
        myState[i][j] = FREE;
      }
      myState[i][0] = WALL;
      myState[i][myState[i].length - 1] = WALL;
    }
    for (int j = 0; j < myState[0].length; j++) {
      myState[0][j] = WALL;
    }
    for (int j = 0; j < myState[myState.length - 1].length; j++) {
      myState[myState.length - 1][j] = WALL;
    }

    // Random obstacles:
    Random random = new Random();
    random.setSeed(seed);
    for (int i = 0; i < myState.length; i++) {
      for (int j = 0; j < myState[i].length; j++) {
        if (random.nextInt(6) == 0) {
          myState[i][j] = WALL;
    } } }

    // Random penguins:
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if(WALL != myState[x][y]) {
          switch (random.nextInt(31)) {
            case 0 :
              myState[x][y] = PENGUIN_OOO;
              break;
            case 1 :
              myState[x][y] = PENGUIN_OOI;
              break;
            case 2 :
              myState[x][y] = PENGUIN_OIO;
              break;
            case 3 :
              myState[x][y] = PENGUIN_OII;
              break;
            case 4 :
              myState[x][y] = PENGUIN_IOO;
              break;
            default
            :
            break
            ;
     } } } }

    // Entrance
    myState[1][0] = ZOOKEEPER;
    myState[1][1] = FREE;
    myState[2][1] = FREE;

    return myState;
  }
}
