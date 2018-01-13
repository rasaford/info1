package dijkstra_angabe;

import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SeerettungTest2 {

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

  @Test
  public void findeWegBeispiel0() throws Exception {
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(12, eisschollen[1], eisschollen[3]));
    seewege.add(new Seeweg(3, eisschollen[0], eisschollen[2]));
    seewege.add(new Seeweg(3, eisschollen[2], eisschollen[4]));
    seewege.add(new Seeweg(1, eisschollen[3], eisschollen[5]));
    seewege.add(new Seeweg(5, eisschollen[5], eisschollen[4]));
    seewege.add(new Seeweg(2, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(15, eisschollen[1], eisschollen[5]));

    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 4);
    assertEquals(3, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[2], schollen.get(1));
    assertEquals(eisschollen[4], schollen.get(2));
  }

  @Test
  public void findeWegBeispiel1() throws Exception {
    seewege.add(new Seeweg(1, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(100, eisschollen[0], eisschollen[2]));
    seewege.add(new Seeweg(1, eisschollen[1], eisschollen[2]));
    seewege.add(new Seeweg(50, eisschollen[1], eisschollen[3]));
    seewege.add(new Seeweg(1, eisschollen[2], eisschollen[3]));

    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 3);
    assertEquals(4, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[1], schollen.get(1));
    assertEquals(eisschollen[2], schollen.get(2));
    assertEquals(eisschollen[3], schollen.get(3));
  }
}

