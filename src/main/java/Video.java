public class Video {

  private String titel;
  private int id;
  private String[] genres;
  private static int count;

  Video(String titel) {
    this.titel = titel;
    id = count++;
    genres = new String[5];
  }

  String getTitel() {
    return titel;
  }

  int getId() {
    return id;
  }

  String[] getGenres() {
    return genres;
  }

  int addGenre(String genre) {
    int i = 0;
    for (; i < genres.length; i++) {
      if (genres[i] == null) {
        break;
      }
    }
    if (i == genres.length) {
      return -1;
    }
    genres[i] = genre;
    return ++i;
  }
}