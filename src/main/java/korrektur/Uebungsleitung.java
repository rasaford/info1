package korrektur;

import java.util.Random;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Uebungsleitung extends Stopable {

  private Buffer<Klausur> left;
  private Buffer<Klausur> right;
  private Random random = new Random();

  public Uebungsleitung(Buffer<Klausur> left, Buffer<Klausur> right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public void run() {
    int count = 0;
    System.out.printf("started Übungsleitung %s\n", getName());
    while (true) {
      try {
        Klausur test = left.get();
        if (test == null) {
          break;
        }
        for (int i = 0; i < test.getPunkte().length; i++) {
          int preliminary = test.getPunkte()[i];
          int secondary = preliminary;
          switch (random.nextInt(10)) {
            case 9:
              secondary = random.nextBoolean() ?
                  Math.min(Korrekturschema.maxPoints(i), preliminary + 1) :
                  Math.max(0, preliminary - 1);
              break;
          }
          test.setZweitkorrektur(i, secondary);
        }
        right.add(test);
        count++;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.printf("stopped Übungsleitung %s at %d\n", getName(), count);
  }
}
