import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Test;

public class TestAufgabe5_6 {

  // dirty hacks to get around the private static field
  private void setPlayers(int[][] players) {
    try {
      Field f = Mensch.class.getDeclaredField("players");
      f.setAccessible(true);
      f.set(null, players);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println(e);
    }
  }

  private int[][] getPlayers() {
    try {
      Field f = Mensch.class.getDeclaredField("players");
      f.setAccessible(true);
      return (int[][]) f.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println(e);
    }
    return null;
  }

  @Test
  public void before() {
    assertTrue(Mensch.before(39, 0));
    assertTrue(Mensch.before(5, 10));
    assertFalse(Mensch.before(39, 37));
  }

  @Test
  public void winner() {
    assertEquals(0, Mensch.winner(new int[][]{
        {40, 40, 40, 40},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1}}
    ));
    assertEquals(-1, Mensch.winner(new int[][]{
        {-1, 40, 40, 40},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1}}
    ));
    assertEquals(1, Mensch.winner(new int[][]{
        {-1, -1, -1, -1},
        {40, 40, 40, 40},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1}}
    ));
    assertEquals(2, Mensch.winner(new int[][]{
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {40, 40, 40, 40},
        {-1, -1, -1, -1}}
    ));
    assertEquals(3, Mensch.winner(new int[][]{
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {40, 40, 40, 40}}
    ));
  }

  @Test
  public void kickOwnFig1() {
    setPlayers(new int[][]{
        {10, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    });
    boolean res = Mensch.kickFigure(0, 10, 1);
    assertArrayEquals(new int[][]{
        {10, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    }, getPlayers());
    assertFalse(res);
  }

  @Test
  public void kickOwnFig2() {
    setPlayers(new int[][]{
        {-1, -1, -1, -1},
        {-1, 39, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    });
    boolean res = Mensch.kickFigure(1, 39, 3);
    assertArrayEquals(new int[][]{
        {-1, -1, -1, -1},
        {-1, 39, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    }, getPlayers());
    assertFalse(res);
  }

  @Test
  public void kickOther1() {
    setPlayers(new int[][]{
        {10, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    });
    boolean res = Mensch.kickFigure(1, 10, 1);
    assertArrayEquals(new int[][]{
        {-1, -1, -1, -1},
        {-1, 10, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    }, getPlayers());
    assertTrue(res);
  }

  @Test
  public void kickOther2() {
    setPlayers(new int[][]{
        {-1, -1, -1, -1},
        {-1, -1, 22, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
    });
    boolean res = Mensch.kickFigure(3, 22, 0);
    assertArrayEquals(new int[][]{
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {-1, -1, -1, -1},
        {22, -1, -1, -1},
    }, getPlayers());
    assertTrue(res);
  }
}
