package korrektur;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Klausurkorrektur {

  private static final int SMALL_BUFFER_SIZE = 50;
  private static final int LARGE_BUFFER_SIZE = 1700;

  private static final int TUTOR = 6; // 6 Tutoren pro Aufgabe
  private static final int UEBUNGSLEITUNG = 2;
  private static final int ADDER = 12; // 12 Addierer am Ende (sind auch Tutoren)

  private List<List<Thread>> threads = new ArrayList<>();

  public static void main(String[] args) {
    Klausurkorrektur klausurkorrektur = new Klausurkorrektur();
    try {
      klausurkorrektur.grade();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void grade() throws InterruptedException {
    Buffer<Klausur> tests = new Buffer<>(LARGE_BUFFER_SIZE, 1700);
    for (int i = 0; i < LARGE_BUFFER_SIZE; i++) {
      tests.add(new Klausur());
    }
    List<Buffer<Klausur>> buffers = new LinkedList<>();
    buffers.add(tests);
    for (int i = 0; i < 7; i++) {
      buffers.add(new Buffer<>(SMALL_BUFFER_SIZE, 1700));
    }
    for (int i = 0; i < 2; i++) {
      buffers.add(new Buffer<>(LARGE_BUFFER_SIZE, 1700));
    }
//     adding isEmpty buffer to make loop easier
    buffers.add(new Buffer<>(1700, 1700));

    for (int i = 0; i < buffers.size() - 1; i++) {
      Buffer<Klausur> current = buffers.get(i);
      Buffer<Klausur> next = buffers.get(i + 1);
      if (i < 8) {
        tutoren(current, next, i);
      } else if (i == 8) {
        uebungsleitung(current, next);
      } else {
        sum(current);
      }
    }
    Thread.sleep(500);
    for (Thread t : threads.get(9)) {
      t.join();
    }
    System.out.println("Korrektur der Info 1 Klausur beendet :)");
  }


  private void tutoren(Buffer<Klausur> left, Buffer<Klausur> right, int task) {
    for (int i = 0; i < TUTOR; i++) {
      Tutor t = new Tutor(left, right, task);
      try {
        threads.get(task);
      } catch (IndexOutOfBoundsException e) {
        threads.add(task, new LinkedList<>());
      }
      threads.get(task).add(t);
      t.start();
    }
  }

  private void uebungsleitung(Buffer<Klausur> left, Buffer<Klausur> right) {
    for (int i = 0; i < UEBUNGSLEITUNG; i++) {
      Uebungsleitung ul = new Uebungsleitung(left, right);
      ul.start();
      int last = 8;
      try {
        threads.get(last);
      } catch (IndexOutOfBoundsException e) {
        threads.add(last, new LinkedList<>());
      }
      threads.get(last).add(ul);
    }
  }

  private void sum(Buffer<Klausur> left) {
    for (int i = 0; i < ADDER; i++) {
      Adder a = new Adder(left);
      a.start();
      int last = 9;
      try {
        threads.get(last);
      } catch (IndexOutOfBoundsException e) {
        threads.add(last, new LinkedList<>());
      }
      threads.get(last).add(a);
    }
  }
}
