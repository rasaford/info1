public class Rekursion extends MiniJava {

  public static void AusgabeMitSchleife(int n) {
    for (int i = 1; i <= n; i++) {
      System.out.print(i + ", ");
    }
    System.out.println("\nFertig");
  }

  public static void AusgabeMitRekursion(int n) {
    // TODO
  }

  public static void AusgabeTiefeRekursiv(int n) {
    // TODO
  }

  public static void main(String[] args) {
    int n = 0; // n = 10;
    while (n < 1 || n > 10) {
      n = readInt("Bitte n (1 <= n <= 10) eingeben:");
    }
    System.out.println("\nAusgabeMitSchleife:");
    AusgabeMitSchleife(n);
    System.out.println("\nAusgabeMitRekursion:");
    AusgabeMitRekursion(n);
    System.out.println("\nAusgabeTiefe:");
    AusgabeTiefe(n);
    System.out.println("\nAusgabeTiefeRekursiv:");
    AusgabeTiefeRekursiv(n);
  }

  // Gibt die Zahl mit Einrueckung aus.
  public static void EingerueckteAusgabe(int n) {
    for (int j = 1; j <= n; j++) {
      System.out.print(" ");
    }
    System.out.println(n + "");
  }

  // Simuliert die mittels Rekursion zu erreichende Ausgabe.
  public static void AusgabeTiefe(int n) {
    // Hin
    for (int i = 1; i <= n; i++) {
      EingerueckteAusgabe(i);
    }
    System.out.println();

    // Zurueck
    for (int i = n; i >= 1; i--) {
      EingerueckteAusgabe(i);
    }
    System.out.println();
  }
}
