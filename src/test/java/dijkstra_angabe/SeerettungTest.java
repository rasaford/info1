package dijkstra_angabe;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class SeerettungTest {


  @Test
  public void findeWegBeispiel() {
    Eisscholle[] eisschollen = new Eisscholle[6];
    List<Seeweg> seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(12, eisschollen[1], eisschollen[3]));
    seewege.add(new Seeweg(15, eisschollen[0], eisschollen[2]));
    seewege.add(new Seeweg(10, eisschollen[2], eisschollen[4]));
    seewege.add(new Seeweg(1, eisschollen[3], eisschollen[5]));
    seewege.add(new Seeweg(5, eisschollen[5], eisschollen[4]));
    seewege.add(new Seeweg(2, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(15, eisschollen[1], eisschollen[5]));

    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 4);
    assertEquals(4, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[1], schollen.get(1));
    assertEquals(eisschollen[3], schollen.get(2));
    assertEquals(eisschollen[4], schollen.get(3));
  }


  /*
  A - B - C
  D - E - F
   */
  @Test
  public void noPossiblePath() {
    Eisscholle[] eisschollen = new Eisscholle[6];
    List<Seeweg> seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(10, eisschollen[1], eisschollen[2]));
    seewege.add(new Seeweg(20, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(20, eisschollen[4], eisschollen[5]));
    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 5);
    assertNull(schollen);
  }

  @Test
  public void cyclicGraph() {
    Eisscholle[] eisschollen = new Eisscholle[6];
    List<Seeweg> seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(10, eisschollen[1], eisschollen[2]));
    seewege.add(new Seeweg(10, eisschollen[2], eisschollen[3]));
    seewege.add(new Seeweg(10, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(10, eisschollen[4], eisschollen[5]));
    seewege.add(new Seeweg(10, eisschollen[5], eisschollen[0]));
    seewege.add(new Seeweg(10, eisschollen[1], eisschollen[0]));
    seewege.add(new Seeweg(10, eisschollen[2], eisschollen[1]));
    seewege.add(new Seeweg(10, eisschollen[3], eisschollen[2]));
    seewege.add(new Seeweg(10, eisschollen[4], eisschollen[3]));
    seewege.add(new Seeweg(10, eisschollen[5], eisschollen[4]));
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[5]));
    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 5);
    assertEquals(2, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[5], schollen.get(1));
  }

  @Test
  public void emptyGraph() {
    Eisscholle[] eisschollen = new Eisscholle[6];
    List<Seeweg> seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 5);
    assertNull(schollen);
  }

  @Test
  public void indexOutOfBounds() {
    Eisscholle[] eisschollen = new Eisscholle[6];
    List<Seeweg> seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, -1, 4);
    assertNull(schollen);
    schollen = Seerettung.findeWeg(eisschollen, seewege, 10, 11);
    assertNull(schollen);
    schollen = Seerettung.findeWeg(eisschollen, seewege, 0, -1);
    assertNull(schollen);
  }

  /**
   * dijkstra does not work with negative weight cycles. Therefore disallowing any negative weight
   * edges
   */
  @Test(expected = RuntimeException.class)
  public void negativeWeightSeeweg() {
    Eisscholle[] eisschollen = new Eisscholle[6];
    List<Seeweg> seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
    seewege.add(new Seeweg(-10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(-10, eisschollen[1], eisschollen[2]));
    seewege.add(new Seeweg(-5, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(-20, eisschollen[4], eisschollen[5]));
    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 5);
    assertNull(schollen);
  }
}