package dijkstra_angabe;
import java.util.Comparator;

public class EisschollenComparator implements Comparator<Eisscholle> {

  /*
   * Exercise note: !!! DO NOT TOUCH THIS, ALSO DON'T UPLOAD IT !!!
   *
   * Note: this comparator imposes orderings that are inconsistent with equals.
   */
  @Override
  public int compare(Eisscholle e1, Eisscholle e2) {
    int d1 = e1.getDistance();
    int d2 = e2.getDistance();
    return d1 > d2 ? 1 : (d1 == d2 ? 0 : -1);
  }
}
