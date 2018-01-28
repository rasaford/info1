package korrektur;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Adder extends Thread {

  private Buffer<Klausur> buffer;

  public Adder(Buffer<Klausur> buffer) {
    this.buffer = buffer;
  }

  @Override
  public void run() {
    int count = 0;
    while (true) {
      try {
        Klausur test = buffer.get();
        if (test == null) {
          break;
        }
        int sum = 0;
        for (int i : test.getZweitkorrektur()) {
          sum += i;
        }
        test.setGesamtpunktzahl(sum);
        test.setNote(Korrekturschema.note(sum));
        System.out.println(test);
        count++;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.printf("stopped Adder %d at %d\n", getId(), count);
  }
}
