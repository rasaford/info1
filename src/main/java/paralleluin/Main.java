/*************************************************/
/* In dieser Klasse soll nichts geändert werden. */
/*************************************************/

package paralleluin;
public class Main {

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    Colony col = new Colony(24,20,true);
    col.startSimulation();
  }

}
