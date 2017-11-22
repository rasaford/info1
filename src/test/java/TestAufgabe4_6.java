
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAufgabe4_6 {

  @Test
  public void TestCountLetters() {
    assertEquals(
        "A: 3 C: 1 D: 2 E: 8 H: 2 I: 4 K: 1 L: 2 M: 3 N: 6 R: 2 S: 5 T: 3 U: 1 W: 2 Z: 1 Ä: 1 ",
        aufgabe4_6.countLetters("Dass ich erkenne, was die Welt \n"
            + "Im Innersten zusammenhält"));
  }

  @Test
  public void TestReplace() {
    assertEquals("Ocht olte Omeisen oßen om Obend Ononos",
        aufgabe4_6.replace('A', 'o', "Acht alte Ameisen aßen am Abend Ananas"));
  }

  @Test
  public void TestReverse() {
    assertEquals("aD knad hci ;hcuE nned tim ned .netoT baH hci hcim slamein nreg "
            + ".negnafeb mA netsiem beil hci rim eid ,nellov nehcsirf .negnaW rüF menie manhcieL "
            + "nib hci thcin uz ;suaH riM theg se eiw red eztaK tim red .suaM",
        aufgabe4_6.reverse("Da dank ich Euch; denn mit den Toten. Hab ich mich niemals gern "
            + "befangen. Am meisten lieb ich mir die vollen, frischen Wangen. Für einem Leichnam "
            + "bin ich nicht zu Haus; Mir geht es wie der Katze mit der Maus."));
  }
}
