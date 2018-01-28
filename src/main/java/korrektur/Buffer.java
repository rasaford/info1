package korrektur;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Buffer<T> {

  private Semaphore free;
  private Semaphore occupied;
  private T[] buffer;
  private int head;
  private int tail;
  private int cap;
  private volatile int counter;

  private final Object lock = new Object();

  @SuppressWarnings("unchecked")
  public Buffer(int size, int cap) {
    this.buffer = (T[]) new Object[size + 1];
    free = new Semaphore(size);
    occupied = new Semaphore(0);
    counter = cap + 1;
    this.head = this.tail = 0;
    this.cap = size + 1;
  }

  public void add(T object) throws InterruptedException {
    if (counter == 0) {
      System.out.printf("trying to add more than %d elements\n", 1700);
      return;
    }
    free.acquire();
    synchronized (this) {
      buffer[tail] = object;
      tail = (tail + 1) % cap;
    }
    occupied.release();
  }


  public T get() throws InterruptedException {
    synchronized (lock) {
      if (counter == 1) {
        return null;
      }
      counter--;
      occupied.acquire();
    }
    T data;
    synchronized (this) {
      data = buffer[head];
      buffer[head] = null;
      head = (head + 1) % cap;
    }
    free.release();
    return data;
  }

  @Override
  public String toString() {
    return counter + " " + Arrays.toString(buffer);
  }

  public synchronized boolean isEmpty() {
    return tail == head;
  }
}
