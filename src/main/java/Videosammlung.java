import java.util.ArrayList;
import java.util.List;

public class Videosammlung {

  private Video[] videos;
  private int verbleibende;

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  Videosammlung(int n) {
    videos = new Video[n];
    verbleibende = n;
  }

  int addVideo(Video v) {
    int i = 0;
    for (; i < videos.length; i++) {
      if (videos[i] == null) {
        break;
      }
    }
    if (i == videos.length) {
      return -1;
    }
    videos[i] = v;
    verbleibende--;
    return i;
  }

  Video[] getVideos() {
    return videos;
  }

  Video verkaufen(int index) {
    if (index < 0 || index >= videos.length) {
      return null;
    }
    Video res = videos[index];
    if (res == null) {
      return null;
    }
    videos[index] = null;
    verbleibende++;
    return res;
  }

  Video verkaufen(String titel) {
    if (titel == null) {
      return null;
    }
    for (int i = 0; i < videos.length; i++) {
      Video v = videos[i];
      if (v == null) {
        continue;
      }
      if (titel.equals(v.getTitel())) {
        return verkaufen(i);
      }
    }
    return null;
  }

  public int getVerbleibende() {
    return verbleibende;
  }

  String[] videosInGenre(String genre) {
    if (genre == null) {
      return null;
    }
    List<String> result = new ArrayList<>();
    for (Video v : videos) {
      if (v == null) {
        continue;
      }
      for (String g : v.getGenres()) {
        if (g == null) {
          continue;
        }
        if (genre.equals(g)) {
          result.add(v.getTitel());
        }
      }
    }
    return result.toArray(new String[0]);
  }
}
