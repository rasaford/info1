import java.util.Arrays;

public class aufgabe7_6 {

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    int[] array = new int[]{2, 1, -44, 55};
    slowSort(array);
    System.out.println(Arrays.toString(array));

    int[] array2 = new int[]{2, 1, -44, 55};
    evenSlowerSort(array2);
    System.out.println(Arrays.toString(array2));
  }

  public static void slowSort(int[] array) {
    slowSortR(array, 0, array.length - 1);
  }

  private static void slowSortR(int[] array, int min, int max) {
    if (max - min < 2) {
      if (array[max] < array[min]) {
        swap(array, min, max);
      }
      return;
    }
    int mid = (max - min) / 2;
    slowSortR(array, min, min + mid);
    slowSortR(array, min + mid + 1, max);
    int partialMax = min + mid;
    if (array[partialMax] < array[max]) {
      partialMax = max;
    }
    swap(array, partialMax, max);
    slowSortR(array, min, max - 1);
  }

  private static void swap(int[] array, int a, int b) {
    int temp = array[a];
    array[a] = array[b];
    array[b] = temp;
  }

  public static void evenSlowerSort(int[] array) {
    evenSlowerSortR(array, 0, array.length - 1);
  }

  private static void evenSlowerSortR(int[] array, int min, int max) {
    if (max - min < 2) {
      if (array[max] < array[min]) {
        swap(array, min, max);
      }
      return;
    }
    int mid = (max - min) / 3;
    evenSlowerSortR(array, min, min + mid);
    evenSlowerSortR(array, min + mid + 1, min + 2 * mid);
    evenSlowerSortR(array, min + 2 * mid + 1, max);
    int partialMax = min + mid;
    if (array[partialMax] < array[min + 2 * mid]) {
      partialMax = min + 2 * mid;
    }
    if (array[partialMax] < array[max]) {
      partialMax = max;
    }
    swap(array, partialMax, max);
    evenSlowerSortR(array, min, max - 1);
  }
}
