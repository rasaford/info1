package blatt4;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestAufgabe4_8 {

  @Test
  public void testEncyption() {
    assertEquals("v3q8cGhP9fNaP9D6eReMkKW3v7aU7", aufgabe4_8.encrypt(
        "Pinguine sind knuffige Tiere.", 42, 22));
  }

  @Test
  public void testDecryption() {
    assertEquals("Pinguine sind knuffige Tiere.", aufgabe4_8.decrypt(
        "v3q8cGhP9fNaP9D6eReMkKW3v7aU7", 42, 22));
  }

}
