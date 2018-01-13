package dijkstra_angabe;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import sun.security.util.ECKeySizeParameterSpec;

public class Seerettung {

  // WTF Why is this a global variable?
  private static PriorityQueue<Eisscholle> nachbarschollen;

  public static List<Eisscholle> findeWeg(Eisscholle[] eisschollen, List<Seeweg> seewege,
      int startIndex, int endIndex) {
    // basic input checks
    if (startIndex < 0 || startIndex >= eisschollen.length ||
        endIndex < 0 || endIndex >= eisschollen.length) {
      return null;
    }
    nachbarschollen = new PriorityQueue<>(new EisschollenComparator());

    // init
    Eisscholle e0 = eisschollen[startIndex];
    e0.setDistance(0);
    for (Eisscholle e : eisschollen) {
      if (e.equals(e0)) {
        continue;
      }
      e.setDistance(Integer.MAX_VALUE);
      e.setVorgaenger(null);
      e.setState(Eisscholle.UNBEKANNT);
    }
    nachbarschollen.add(e0);

    // algorithm
    while (!nachbarschollen.isEmpty()) {
      Eisscholle min = nachbarschollen.poll();
      min.setState(Eisscholle.BEKANNT);
      int currentDist = min.getDistance();
      for (Seeweg edge : findSeewegeFromEisscholle(seewege, min)) {
        Eisscholle neighbour = edge.getTo();
        int newDist = currentDist + edge.getDistance();
        if (newDist < neighbour.getDistance()) {
          neighbour.setVorgaenger(min);
          neighbour.setDistance(newDist);
          neighbour.setState(Eisscholle.VERMUTET);
        }
        if (neighbour.getState() != Eisscholle.BEKANNT) {
          nachbarschollen.add(neighbour);
        }
      }
    }
    // path reconstruction
    LinkedList<Eisscholle> path = new LinkedList<>();
    Eisscholle current = eisschollen[endIndex];
    while (!current.equals(eisschollen[startIndex])) {
      path.addFirst(current);
      current = current.getVorgaenger();
      if (current == null) {
        return null;
      }
    }
    path.addFirst(eisschollen[startIndex]);
    return path;
  }

  private static List<Seeweg> findSeewegeFromEisscholle(List<Seeweg> seewege,
      Eisscholle eisscholle) {
    return seewege.stream()
        .filter(o -> o.getFrom().equals(eisscholle))
        .collect(Collectors.toList());
  }
}
