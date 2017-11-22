import static org.junit.Assert.*;

import org.junit.Test;

public class TestAufgabe3_8 {

  @Test
  public void TestPlaindrome() {
    assertFalse(aufgabe3_8.palindrome(1231231233));
    assertTrue(aufgabe3_8.palindrome(1001));
    assertTrue(aufgabe3_8.palindrome(44));
    assertFalse(aufgabe3_8.palindrome(123));
    assertTrue(aufgabe3_8.palindrome(123321));
    assertTrue(aufgabe3_8.palindrome(9877789));
  }

}
