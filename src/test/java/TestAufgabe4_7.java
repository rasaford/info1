
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAufgabe4_7 {

  @Test
  public void TestMixedCase() {
    assertArrayEquals(new String[]{
        "Startcase: Handballtor",
        "UPPERCASE: HANDBALLTOR",
        "snake_case: hand_ball_tor",
        "PascalCase: HandBallTor",
    }, aufgabe4_7.mixedCase(new String[]{
        "hand", "BAll", "toR", ""
    }));
  }
}
