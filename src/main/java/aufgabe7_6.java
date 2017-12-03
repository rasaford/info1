public class aufgabe7_6 {

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {

  }

  public static void slowSort(int[] array) {
    slowSortR(array, 0, array.length);
  }

  private static void slowSortR(int[] array, int min, int max) {
    if (max - min == 1) {
      if (array[max] < array[min]) {
        swap(array, min, max);
      }
      return;
    }
    slowSortR(array, min, max / 2);
    slowSortR(array, max / 2, max);
    int index = max / 2;
    if (array[max / 2] < array[max - 1]) {
      index = max - 1;
    }
    swap(array, index, max - 1);
    slowSortR(array, min, max - 2);
  }

  private static void swap(int[] array, int a, int b) {
    int temp = array[a];
    array[a] = array[b];
    array[b] = temp;
  }

  private static int[] partition(int[] array, int min, int max) {
    int[] out = new int[max - min];
    for (int i = min; i < max; i++) {
      out[i - min] = array[i];
    }
    return out;
  }

  private static void evenSlowerSort(int[] array) {

  }
}
