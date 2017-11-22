import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

public class Aerger extends MiniJava {
  private static class Field extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static class Haus extends Field {
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      int spieler;
      boolean besetzt;

      public Haus(boolean besetzt, int spieler, int steinnr) {
        super(-1, 0, steinnr);
        this.spieler = spieler;
        this.besetzt = besetzt;
      }

      public void paint(Graphics g) {
        super.paint(g);
        if (besetzt)
          paintSpielstein(g, spieler, steinnr);
      }

    }

    int feldnummer, spieler, steinnr;
    Point p;

    public Field(int feldnummer, int spieler, int steinnr) {
      this.feldnummer = feldnummer;
      this.spieler = spieler;
      this.steinnr = steinnr;
      p = getLocation();
    }

    public void paint(Graphics g) {
      super.paint(g);
      // Hintergrund
      g.setColor(Color.BLACK);
      g.fillRect(p.getLocation().x, p.getLocation().y, getWidth() * 2, getHeight());
      // Feld
      Paint pa = ((Graphics2D) g).getPaint();
      g.setColor(Color.WHITE);
      GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, getWidth(), 0, Color.DARK_GRAY);
      ((Graphics2D) g).setPaint(gradient);
      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      int pad = getWidth() / 10;
      g.fillRect(p.getLocation().x + pad, p.getLocation().y + pad, (int) (getWidth() - 2 * pad),
          (int) (getHeight() - 2 * pad));
      g.setColor(Color.BLACK);
      if (feldnummer >= 0)
        g.drawString("" + feldnummer, 2 * pad, 3 * pad);

      // Spielstein

      paintSpielstein(g, spieler, steinnr);
      ((Graphics2D) g).setPaint(pa);
    }

    public Dimension getPreferredSize() {
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      int square = (Math.min(dim.width, dim.height) - 20) / 15;
      return new Dimension(square, square);
    }

    protected void paintSpielstein(Graphics g, int spieler, int steinnr) {
      int pad = getWidth() / 10;
      Color c;
      switch (spieler) {
        case 1:
          c = Color.ORANGE;
          break;
        case 2:
          c = Color.BLUE;
          break;
        case 3:
          c = Color.RED;
          break;
        default:
          c = Color.GREEN;
      }

      GradientPaint gradient = new GradientPaint(0, 0, c, getWidth(), 0, Color.WHITE);
      ((Graphics2D) g).setPaint(gradient);
      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      if (spieler != 0) {
        g.fillOval(pad + pad / 2, pad + pad / 2, getWidth() - (3 * pad), getHeight() - (pad * 3));
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont((float) 32));
        g.drawString((steinnr > 0) ? ("" + steinnr) : "", (int) (getWidth() * .5) - 8,
            (int) (getHeight() * .5) + 8);
      }
    }

  }

  private static JFrame myFrame;
  private static JPanel pan;

  private static int whichone(int y, int[] playerone, int[] playertwo, int[] playerthree,
      int[] playerfour) {
    for (int x = 0; x < 4; x++) {
      if (playerone[x] == y)
        return 4 + x;
    }
    for (int x = 0; x < 4; x++) {
      if (playertwo[x] == y)
        return 8 + x;
    }
    for (int x = 0; x < 4; x++) {
      if (playerthree[x] == y)
        return 12 + x;
    }
    for (int x = 0; x < 4; x++) {
      if (playerfour[x] == y)
        return 16 + x;
    }
    return 0;
  }

  public static void paintField(int[] playerone, int[] playertwo, int[] playerthree,
      int[] playerfour) {
    if (myFrame != null) {
      pan.removeAll();
    } else {
      myFrame = new JFrame("Spielfeld");
      pan = new JPanel();
    }
    GridBagConstraints c = new GridBagConstraints();
    pan.setLayout(new GridBagLayout());
    pan.setBackground(Color.BLACK);
    for (int x = 0; x < 40; x++) {
      int z = (x + 4) % 40;

      int figur = whichone(z, playerone, playertwo, playerthree, playerfour);

      if (x < 10) {
        c.gridx = x % 10;
        c.gridy = 0;
      } else if (x < 20) {
        c.gridy = x % 10;
        c.gridx = 10;
      } else if (x < 30) {
        c.gridx = 10 - x % 10;
        c.gridy = 10;
      } else {
        c.gridy = 10 - x % 10;
        c.gridx = 0;
      }
      pan.add(new Field(z, figur / 4, 1 + figur % 4), c);
    }
    c.gridx = 5;
    for (int x = 1; x < 5; x++) {
      c.gridy = x;
      int ttt = (playertwo[x - 1] > 39) ? 2 : 0;
      pan.add(new Field(-1, ttt, x), c);
    }
    for (int x = 9; x > 5; x--) {
      c.gridy = x;
      int ttt = (playerthree[9 - x] > 39) ? 3 : 0;
      pan.add(new Field(-1, ttt, 10 - x), c);
    }
    c.gridy = 5;
    for (int x = 1; x < 5; x++) {
      c.gridx = x;
      int ttt = (playerone[x - 1] > 39) ? 1 : 0;
      pan.add(new Field(-1, ttt, x), c);
    }
    for (int x = 9; x > 5; x--) {
      c.gridx = x;
      int ttt = (playerfour[9 - x] > 39) ? 4 : 0;
      pan.add(new Field(-1, ttt, 10 - x), c);
    }

    for (int x = 0; x < 4; x++) {
      c.gridx = 2 + x % 2;
      c.gridy = 2 + x / 2;

      pan.add(new Field.Haus(playerone[x] < 0, 1, x + 1), c);
    }
    for (int x = 0; x < 4; x++) {
      c.gridx = 7 + x % 2;
      c.gridy = 2 + x / 2;
      pan.add(new Field.Haus(playertwo[x] < 0, 2, x + 1), c);
    }
    for (int x = 0; x < 4; x++) {
      c.gridx = 2 + x % 2;
      c.gridy = 7 + x / 2;
      pan.add(new Field.Haus(playerthree[x] < 0, 3, x + 1), c);
    }
    for (int x = 0; x < 4; x++) {
      c.gridx = 7 + x % 2;
      c.gridy = 7 + x / 2;
      pan.add(new Field.Haus(playerfour[x] < 0, 4, x + 1), c);
    }

    myFrame.add(pan);
    myFrame.pack();
    // myFrame.setSize(920,920);
    myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    myFrame.setVisible(true);
  }

  public static void paintField(int[] one, int[] two) {
    int[] arr = {-1, -1, -1, -1};
    paintField(one, two, arr, arr);
  }
}
