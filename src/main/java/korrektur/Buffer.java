package korrektur;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Buffer<T> {

  private Semaphore free;
  private Semaphore occupied;
  private T[] buffer;
  private int head;
  private int tail;
  private int cap;
  private AtomicInteger counter;

  @SuppressWarnings("unchecked")
  public Buffer(int size, int cap) {
    this.buffer = (T[]) new Object[size + 1];
    free = new Semaphore(size);
    occupied = new Semaphore(0);
    counter = new AtomicInteger(cap);
    this.head = this.tail = 0;
    this.cap = size + 1;
  }

  public void add(T object) throws InterruptedException {
    if (counter.get() == 0) {
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
    if (counter.done()) {
      return null;
    }
    occupied.acquire();
    T data;
    synchronized (this) {
      data = buffer[head];
      head = (head + 1) & cap;
    }
    free.release();
    return data;
  }

  @Override
  public String toString() {
    return counter + Arrays.toString(buffer);
  }

  public synchronized boolean isEmpty() {
    return tail == head;
  }

  private class AtomicInteger {

    private int counter;

    public AtomicInteger(int counter) {
      this.counter = counter;
    }

    public synchronized int get() {
      return counter;
    }

    public synchronized boolean done() throws InterruptedException {
      if (counter == 0) {
        return true;
      } else {
        counter--;
        return false;
      }
    }

    @Override
    public synchronized String toString() {
      return Integer.toString(counter);
    }
  }
}
