public class NormalMergeSort {
  public static void mergeSort(int[] arr) {
    mergeSort(arr, 0, arr.length - 1);
  }

  public static void mergeSort(int[] arr, int low, int high) {
    if (low < high) {
      int mid = (low + high) / 2;
      mergeSort(arr, low, mid);
      mergeSort(arr, mid + 1, high);
      merge(arr, low, mid, high);
    }
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
}
