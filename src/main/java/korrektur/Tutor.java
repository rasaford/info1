package korrektur;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Tutor extends Thread {

  private Buffer<Klausur> left;
  private Buffer<Klausur> right;
  private int task;

  public Tutor(Buffer<Klausur> left, Buffer<Klausur> right, int task) {
    this.left = left;
    this.right = right;
    this.task = task;
    setName(getName() + " Tutor " + task);
  }


  @Override
  public void run() {
    System.out.printf("started tutor %s\n", getName());
    int count = 0;
    while (true) {
      try {
        Klausur test = left.get();
        if (test == null) {
          break;
        }
        int res = Korrekturschema.punkte(task, test.getAntwort(task));
        test.setPunkte(task, res);
        right.add(test);
        count++;
      } catch (InterruptedException e) {
        e.printStackTrace();
        // this page intentionally left blank
      }
    }
    System.out.printf("stopped tutor %s at %d\n", getName(), count);
  }

}
