package dijkstra_angabe;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class Seerettung {

  private static PriorityQueue<Eisscholle> nachbarschollen;

  public static List<Eisscholle> findeWeg(Eisscholle[] eisschollen, List<Seeweg> seewege,
      int startIndex, int endIndex) {
    // basic input checks
    if (startIndex < 0 || startIndex >= eisschollen.length ||
        endIndex < 0 || endIndex >= eisschollen.length) {
      return null;
    }
    nachbarschollen = new PriorityQueue<>(new EisschollenComparator());
    Set<Eisscholle> bekannt = new HashSet<>();

    // init
    Eisscholle e0 = eisschollen[startIndex];
    e0.setDistance(0);
    for (Eisscholle e : eisschollen) {
      if (e.equals(e0)) {
        continue;
      }
      e.setDistance(Integer.MAX_VALUE);
      e.setVorgaenger(null);
    }
    nachbarschollen.add(e0);

    // algorithm
    while (!nachbarschollen.isEmpty()) {
      Eisscholle min = nachbarschollen.poll();
      bekannt.add(min);
      min.setState(Eisscholle.BEKANNT);
      int currentDist = min.getDistance();
      for (Seeweg edge : findSeewegeFromEisscholle(seewege, min)) {
        Eisscholle neighbour = edge.getTo();
        int newDist = currentDist + edge.getDistance();
        if (newDist < neighbour.getDistance()) {
          neighbour.setVorgaenger(min);
          neighbour.setDistance(newDist);
          nachbarschollen.add(neighbour);
        }
      }
    }
    LinkedList<Eisscholle> path = new LinkedList<>();
    Eisscholle current = eisschollen[endIndex];
    while (!current.equals(eisschollen[startIndex])) {
      path.addFirst(current);
      current = current.getVorgaenger();
    }
    path.addFirst(eisschollen[startIndex]);
    System.out.println(path);
    return path;
  }


  private static List<Seeweg> findSeewegeFromEisscholle(List<Seeweg> seewege,
      Eisscholle eisscholle) {
    return seewege.stream()
        .filter(o -> o.getFrom().equals(eisscholle))
        .collect(Collectors.toList());
  }
}
