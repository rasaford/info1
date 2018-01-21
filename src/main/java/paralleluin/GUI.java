/*************************************************/
/* In dieser Klasse soll nichts geändert werden. */
/*************************************************/

package paralleluin;
import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;

public class GUI extends JPanel {
  /******************************************************************/

  public static final int NIXUIN = 0; // kein Pingu
  public static final int FRAUIN = 1; // Weibchen
  public static final int MANNUIN = 2; // Männchen
  public static final int SCHWANGUIN = 3; // brütender Pingu
  public static final int KLEINUIN = 4; // Pinguin-Bub
  public static final int KLEINUININ = 5; // Pinguin-Mädl

  protected void generateAntarctic(int[][] landscape, Penguin[][] placed, boolean standard) {
    generate(landscape,placed,standard);
  }

  protected static void setForeground(int[][] where, int x, int y, int fg) {
    where[x][y] = compose(rtOf(where[x][y]), bgOf(where[x][y]),fgOf(fg));
  }

  public static boolean ocean(int[][] where, int x, int y, int fg) {
    return bgOf(where[x][y]) == BACKGROUND_WATER;
  }

  /******************************************************************/



  private static final int BACKGROUND_ISLAND = 1 << 8;
  private static final int BACKGROUND_WATER = 0;
  private static final int FOREGROUND_EMPTY = NIXUIN;
  private static final int FOREGROUND_PENGUIN_M = FRAUIN;
  private static final int FOREGROUND_PENGUIN_F = MANNUIN;
  private static final int FOREGROUND_PENGUIN_B = SCHWANGUIN;
  private static final int FOREGROUND_PENGUIN_LM = KLEINUIN;
  private static final int FOREGROUND_PENGUIN_LF = KLEINUININ;
  private static final int ROTATE_0 = 0;
  private static final int NO_KEY    = -1;

  private static Image[][][] myImage = load();

  private static int compose(int rot, int back, int fore) {
    return rot + back + fore;
  }

  private class Field extends JPanel {
    Point p; int x,y;
    public Field(int x, int y) {
      this.x = x;
      this.y = y;
      p = getLocation();
    }
    public void paint(Graphics g) {
      super.paint(g);
      int background = bgOf(myState[x][y]);
      int foreground = fgOf(myState[x][y]);
      if (background == BACKGROUND_ISLAND) {
        GradientPaint gradient =
            new GradientPaint(75, 80, Color.BLUE, getWidth(), 0, Color.WHITE);
        ((Graphics2D) g).setPaint(gradient);
      } else if (background == BACKGROUND_WATER) {
        GradientPaint gradient = new GradientPaint(31, 30, Color.WHITE, getWidth(), 0, Color.BLUE);
        ((Graphics2D) g).setPaint(gradient);
      } else {
        System.out.println("Waaaaaaaaaaaah [" + x + "][" + y + "] = " + myState[x][y] + " :-(");
        //System.exit(0);
      }

      g.fillRect(p.getLocation().x, p.getLocation().y, getWidth() * 2, getHeight());

      if (foreground != FOREGROUND_EMPTY) {
        drawSymbol(g, myState[x][y], x, y);
      }
    }

    private void drawSymbol(Graphics g, int what, int x, int y) {
      String iName = GUI.toString(what);
      int i1 = rtShift(what), i2 = bgShift(what), i3 = fgShift(what);
      i2=0;
      //System.out.println(i1 +","+i2+","+i3);
      if (i1 >= image.length || i2 >= image[0].length || i3 >= image[0][0].length) {
        System.out.println("\n\nERROR: " + what + " is not a valid value! " + x + "," + y + "\n");
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
  private static GUI myGUI;
  private JPanel fieldPanel = new JPanel();
  private JFrame myFrame;

  private static Image[][][] load() {
    Image[][][] image = new Image[1][1][6];
    for (int i1 = 0; i1 < image.length; i1++) {
      for (int i2 = 0; i2 < image[0].length; i2++) {
        for (int i3 = 0; i3 < image[0][0].length; i3++) {
          String iName = "./pics/" + _2(i1) + _2(i2) + _2(i3) + ".png";
          File f = new File(iName);
          if(f.exists() && !f.isDirectory()) {
            //System.out.println("Loading image " + iName + ".");
            try {
              image[i1][i2][i3] = ImageIO.read(f);
            } catch(Exception e) {
              System.out.println("Datei " + iName + " konnte nicht geladen werden.");
              System.exit(-1);
            }
          } else if (i1 > 0) {
            image[i1][i2][i3] = image[i1-1][i2][i3];
          } else {
            System.out.println("Fehlende Datei: " + iName);
            System.exit(-1);
    } } } }
    return image;
  }

  public GUI() {
    image = myImage;
  }

  private GUI(int[][] state) {
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

    myFrame = new JFrame("Rettet die Antarktis!");
    fieldPanel.setLayout(new GridLayout(myState[0].length, myState.length));
    myFrame.getContentPane().add(fieldPanel);
    myFrame.setSize (IWH * myState.length, IWH * myState[0].length);
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

  protected static void draw(int[][] myState) {
    if (myGUI == null) {
      myGUI = new GUI(myState);
      try {
        Thread.sleep(delayStart);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
    }
    while (myGUI.pause) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException ie) {}
    }
    myGUI.update(myState);
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
        default:
          break;
  } } }

  private Image[][][] image;
  private boolean pause = false;
  private static final long myDelay = 0;
  private static final long delayStart = 100;
  private static final int IWH = 30;
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
      s += _2(arg << (8 * (i-1)));
      if(s.length()%2==1)s="0"+s;
    }
    return s + ".png";
  }

  private void generate(int[][] landscape, Penguin[][] placed, boolean standard) {
    Random random = new Random();
    if (standard) {
      random.setSeed(0);
    }
    int width = landscape.length;
    int height = landscape[0].length;
    for (int i = 0; i < landscape.length; i++) {
      for (int j = 0; j < landscape[i].length; j++) {
        if(Math.abs(2*width/5-j)+Math.abs(i-2*height/3)<((width+height)/7)+1) {
          landscape[i][j] = BACKGROUND_ISLAND;
        } else {
          landscape[i][j] = BACKGROUND_WATER;
        }
        switch (random.nextInt(width*(width+height)/5)) {
          case 1:
            placed[i][j] = new Penguin(true,i,j,random.nextInt(Penguin.adultAge)+Penguin.adultAge,(Colony)this);
            setForeground(landscape,i,j,placed[i][j].getFg());
            break;
          case 2:
            placed[i][j] = new Penguin(false,i,j,random.nextInt(Penguin.adultAge)+Penguin.adultAge,(Colony)this);
            setForeground(landscape,i,j,placed[i][j].getFg());
            break;
          case 4:
            placed[i][j] = new Penguin(true,i,j,random.nextInt(Penguin.adultAge),(Colony)this);
            setForeground(landscape,i,j,placed[i][j].getFg());
            break;
          case 5:
            placed[i][j] = new Penguin(false,i,j,random.nextInt(Penguin.adultAge),(Colony)this);
            setForeground(landscape,i,j,placed[i][j].getFg());
            break;
          default:
            placed[i][j] = null;
            break;
        }
      }
    }
  }
}
