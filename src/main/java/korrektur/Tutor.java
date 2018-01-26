package korrektur;

public class Tutor extends Stopable {

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
//        System.out.printf("%s got %s\n", getName(), test);
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
