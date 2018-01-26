package korrektur;

import java.util.Random;

public class Klausur {
  private static Random random = new Random();

  private String vorname;
  private String nachname;

  // Es gibt 8 Aufgaben in der Klausur
  private String[] antworten = new String[8];
  private int[] punkte = new int[8];
  private int[] zweitkorrektur = new int[8];
  private int gesamtpunktzahl;
  private float note = 5.0f;

  public float getNote() {
    return note;
  }

  public void setNote(float note) {
    this.note = note;
  }

  public int getGesamtpunktzahl() {
    return gesamtpunktzahl;
  }

  public void setGesamtpunktzahl(int gesamtpunktzahl) {
    this.gesamtpunktzahl = gesamtpunktzahl;
  }

  public String getAntwort(int index) {
    if (index < 0 || index >= 8)
      throw new IllegalArgumentException("Index muss im Bereich [0,7] sein.");
    return antworten[index];
  }

  public int[] getZweitkorrektur() {
    return zweitkorrektur;
  }

  public void setZweitkorrektur(int index, int punkte) {
    if (index >= 0 && index < 8)
      this.zweitkorrektur[index] = punkte;
    else
      throw new IllegalArgumentException("Index muss im Bereich [0,7] sein.");
  }

  public int[] getPunkte() {
    return punkte;
  }

  public void setPunkte(int index, int punkte) {
    if (index >= 0 && index < 8)
      this.punkte[index] = punkte;
    else
      throw new IllegalArgumentException("Index muss im Bereich [0,7] sein.");
  }

  public String getVorname() {
    return vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public Klausur() {
    this.vorname = createVorname();
    this.nachname = createNachname();

    for (int i = 0; i < antworten.length; i++) {
      antworten[i] = getRandomAntwort();
    }
  }

  private String getRandomAntwort() {
    int zufall = random.nextInt(4);
    switch (zufall) {
      case 0:
        return "a";
      case 1:
        return "b";
      case 2:
        return "c";
      default:
        return "d";
    }
  }

  @Override
  public String toString() {
    return nachname + ", " + vorname + " hat " + gesamtpunktzahl
        + " von 40 möglichen Punkten und eine " + note + " geschrieben.";
  }

  private static String[] vornamen = new String[] {"Ean", "Markus", "Patrik", "Jonte", "Milan",
      "Bent", "Rouven", "Aljoscha", "Johnny", "Said", "Salim", "Lorenz", "Gerald", "Samuel",
      "Mailo", "Dirk", "Levi", "Liam", "Elias", "Frank", "Burglind", "Isabella", "Leila", "Lani",
      "Samara", "Vera", "Nelli", "Fee", "Gloria", "Renesmee", "Rachel", "Ida", "Lina", "Mathilda",
      "Laura", "Emma", "Emilia", "Fatme", "Bele", "Ella"};
  private static String[] nachnamen = new String[] {"Mueller", "Maier", "Schmidt", "Schulz",
      "Kranz", "Schroeder", "Behrens", "Jansen", "Hunter", "Ahler"};
  private static Random rand = new Random();

  private String createVorname() {
    return vornamen[rand.nextInt(40)];
  }

  private String createNachname() {
    return nachnamen[rand.nextInt(10)];
  }
}
