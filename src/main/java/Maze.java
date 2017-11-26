import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;

@SuppressWarnings("serial")
public class Maze extends JPanel {
  public static final int FREE = 0;
  public static final int WALL = 1;
  public static final int PLAYER = 2;
  public static final int OLD_PATH_ACTIVE = 3;
  public static final int OLD_PATH_DONE = 4;
  public static final int PENGUIN = 5;

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
      if (spielFeld[x][y] == WALL) {
        GradientPaint gradient =
            new GradientPaint(10, 50, Color.GRAY, getWidth(), 0, Color.DARK_GRAY);
        ((Graphics2D) g).setPaint(gradient);
      } else {
        GradientPaint gradient = new GradientPaint(0, 50, Color.WHITE, getWidth(), 0, Color.GRAY);
        ((Graphics2D) g).setPaint(gradient);
      }

      g.fillRect(p.getLocation().x, p.getLocation().y, getWidth() * 2, getHeight());

      switch (spielFeld[x][y]) {
        case PLAYER:
          paintSymbol(g, Color.RED);
          break;
        case OLD_PATH_ACTIVE:
          paintSymbol(g, Color.BLUE);
          break;
        case OLD_PATH_DONE:
          paintSymbol(g, Color.GRAY);
          break;
        case PENGUIN:
          drawPeng(g);
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

    private void drawPeng(Graphics g) {
      if (peng == null) {
        paintSymbol(g, Color.YELLOW);
        return;
      }
      ((Graphics2D) g).drawImage(peng, 0, 0, getWidth(), getHeight(), 0, 0, peng.getWidth(null),
          peng.getHeight(null), null);
    }
  }

  private int[][] spielFeld;
  private static Maze myMaze;
  private JPanel fieldPanel = new JPanel();
  private JFrame myFrame;

  public Maze() {
    File f = new File("tux.png");
    if (f.exists() && !f.isDirectory()) {
      peng = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());

    }
    /* Intentionally left blank */
  }

  private Maze(int[][] feld) {
    this();
    spielFeld = new int[feld.length][feld[0].length];
    Field[][] field = new Field[spielFeld.length][spielFeld[0].length];
    for (int y = 0; y < spielFeld[0].length; y++) {
      for (int x = 0; x < spielFeld.length; x++) {
        field[x][y] = new Field(x, y);
        fieldPanel.add(field[x][y]);
        spielFeld[x][y] = feld[x][y];
      }
    }

    myFrame = new JFrame("A-Maze-Ing");
    fieldPanel.setLayout(new GridLayout(spielFeld[0].length, spielFeld.length));
    myFrame.getContentPane().add(fieldPanel);
    myFrame.setSize(IWH * 10, IWH * 10 * (spielFeld[0].length) / (spielFeld.length));
    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myFrame.addComponentListener(new ComponentHandler());
    myFrame.addKeyListener(new KeyHandler());
    myFrame.setVisible(true);

    update(feld);
  }

  private void update(int[][] feld) {
    for (int x = 0; x < spielFeld.length; x++)
      for (int y = 0; y < spielFeld[0].length; y++)
        spielFeld[x][y] = feld[x][y];
    fieldPanel.repaint();
  }

  public static void draw(int[][] maze) {
    if (myMaze == null) {
      myMaze = new Maze(maze);
    }
    while (myMaze.pause) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException ie) {
      }
    }
    myMaze.update(maze);
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
          myMaze.myFrame.dispose();
          System.exit(0);
          break;
        case KeyEvent.VK_PLUS:
          wannaSleep -= 50;
          wannaSleep = wannaSleep < 0 ? 0 : wannaSleep;
          System.out.println("delay=" + wannaSleep);
          break;
        case KeyEvent.VK_MINUS:
          wannaSleep += 50;
          System.out.println("delay=" + wannaSleep);
          break;
        case KeyEvent.VK_B:
          System.out.println("break");
          pause = true;
          break;
        case KeyEvent.VK_C:
          System.out.println("continue");
          pause = false;
          break;
        default:
          break;
      }
    }
  }

  private Image peng;
  private static long wannaSleep = 100;
  private static final int IWH = 40;
  private boolean pause = false;

  public static int[][] generateMaze(int width, int height) {
    return generateMaze(width, height, false, false);
  }

  public static int[][] generatePenguinMaze(int width, int height) {
    return generateMaze(width, height, true, false);
  }

  public static int[][] generateStandardMaze(int width, int height) {
    return generateMaze(width, height, false, true);
  }

  public static int[][] generateStandardPenguinMaze(int width, int height) {
    return generateMaze(width, height, true, true);
  }

  private static int[][] generateMaze(int width, int height, boolean penguins, boolean standard) {
    width = Math.max(width, 3);
    height = Math.max(height, 3);

    int[][] maze = new int[width][height];

    // Borders:
    // if(!penguins){
    for (int i = 0; i < width; i++) {
      maze[i][0] = WALL;
      maze[i][height - 1] = WALL;
    }
    for (int i = 0; i < height; i++) {
      maze[0][i] = WALL;
      maze[width - 1][i] = WALL;
    }
    // }

    // Random obstacles:
    Random random = new Random();
    if (standard) {
      random.setSeed(0);
    }
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (random.nextInt(4) == 0) {
          maze[x][y] = WALL;
        }

      }
    }

    if (penguins) {
      // Random penguins:
      for (int y = 1; y < height - 1; y++) {
        for (int x = 1; x < width - 1; x++) {
          if (random.nextInt(16) == 0) {
            maze[x][y] = PENGUIN; // if(WALL != maze[x][y])
          }
        }
      }
    }
    if (!penguins) {
      // Exit
      maze[width - 1][height - 2] = FREE;
      maze[width - 2][height - 2] = FREE;
    }
    // Entrance
    maze[1][0] = FREE;
    maze[1][1] = FREE;

    return maze;
  }
}
