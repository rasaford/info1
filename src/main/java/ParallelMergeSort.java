public class ParallelMergeSort {

  public static volatile int numberOfThreads = 8;

  private static AtomicInteger workers = new AtomicInteger(0);

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void mergeSort(int[] arr) {
    mergeSort(arr, 0, arr.length - 1);
    System.out.println("workers.get() = " + workers.get());
  }

  public static void mergeSort(int[] arr, int low, int high) {
    if (low >= high) {
      return;
    }
    int mid = (low + high) / 2;
    Thread newThread = dispatch(arr, low, mid);
    mergeSort(arr, mid + 1, high);
    try {
      if (newThread != null) {
        newThread.join();
      }
    } catch (InterruptedException e) {
      System.err.println("Interrupted Thread while waiting");
    }
    merge(arr, low, mid, high);
  }

  public static synchronized Thread dispatch(final int[] arr, final int low, final int high) {
    Thread t = null;
    if (numberOfThreads > workers.get()) {
      workers.incrementAndGet();
      t = new Thread(() -> {
        NormalMergeSort.mergeSort(arr, low, high);
        workers.decrementAndGet();
      });
      t.start();
    } else {
      NormalMergeSort.mergeSort(arr, low, high);
    }
    return t;
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
}
