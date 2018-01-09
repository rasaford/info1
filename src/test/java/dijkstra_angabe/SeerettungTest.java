package dijkstra_angabe;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SeerettungTest {

  Eisscholle[] eisschollen;
  List<Seeweg> seewege;

  @Before
  public void setUp() throws Exception {
    eisschollen = new Eisscholle[6];
    seewege = new LinkedList<>();
    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void findeWegBeispiel() throws Exception {
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

}