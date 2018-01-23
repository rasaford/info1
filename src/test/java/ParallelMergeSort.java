public class ParallelMergeSort {

  public static volatile int numberOfThreads = 8;

  private static AtomicInteger workers = new AtomicInteger(0);

  public static void mergeSort(int[] arr) {
    mergeSort(arr, 0, arr.length - 1);
    System.out.println("workers.get() = " + workers.get());
  }

  public static void mergeSort(int[] arr, int low, int high) {
    if (low >= high) {
      return;
    }
    int mid = (low + high) / 2;
    Future threadDone;
    if (numberOfThreads > workers.get()) {
      threadDone = dispatch(arr, low, mid);
    } else {
      NormalMergeSort.mergeSort(arr, low, mid);
      threadDone = null;
    }
    mergeSort(arr, mid + 1, high);
    if (threadDone != null) {
      try {
        threadDone.getDone();
      } catch (InterruptedException e) {
        System.err.println("Interrupted Thread while waiting");
      }
    }
    merge(arr, low, mid, high);
  }

  public static synchronized Future dispatch(final int[] arr, final int low, final int high) {
    workers.incrementAndGet();
    Future isDone = new Future();
    Thread t = new Thread(() -> {
      NormalMergeSort.mergeSort(arr, low, high);
      workers.decrementAndGet();
      isDone.setDone();
    });
    t.start();
    return isDone;
  }

  private static void merge(int[] arr, int low, int mid, int high) {
    int i = low;
    int j = mid + 1;
    int k = 0;

    final int SIZE = high - low + 1;
    int[] helperArr = new int[SIZE];

    while (i <= mid && j <= high) {
      if (arr[i] < arr[j]) {
        helperArr[k++] = arr[i++];
      } else {
        helperArr[k++] = arr[j++];
      }
    }

    while (i <= mid) {
      helperArr[k++] = arr[i++];
    }
    while (j <= high) {
      helperArr[k++] = arr[j++];
    }
    for (i = 0; i < SIZE; i++) {
      arr[low + i] = helperArr[i];
    }
  }

  private static class AtomicInteger {

    private int count;

    public AtomicInteger(int count) {
      this.count = count;
    }

    public synchronized int incrementAndGet() {
      return ++count;
    }

    public synchronized int decrementAndGet() {
      return --count;
    }

    public synchronized int get() {
      return count;
    }
  }

  private static class Future {

    private boolean done;

    public synchronized void setDone() {
      this.done = true;
      notify();
    }

    public synchronized boolean getDone() throws InterruptedException {
      while (!done) {
        wait();
      }
      return done;
    }
  }
}
