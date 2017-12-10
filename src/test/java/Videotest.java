import java.util.Arrays;

public class Videotest extends MiniJava {
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_RESET = "\u001B[0m";

  public static void fehler(String meldung) {
    System.out.println(ANSI_RED + "%%%%%%%%%%%% Fehler %%%%%%%%%%%%%%%");
    System.out.println(meldung);
    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + ANSI_RESET);
  }

  public static void korrekt(String meldung) {
    System.out.println(ANSI_GREEN + meldung + ANSI_RESET);
  }

  public static String printVideo(Video v) {
    return "Video{" + "titel='" + v.getTitel() + '\'' + ", id=" + v.getTitel() + ", genres="
        + Arrays.toString(v.getGenres()) + '}';
  }

  public static String printVideosammlung(Videosammlung vhs) {
    String s = "";
    Video[] videos = vhs.getVideos();
    for (int i = 0; i < videos.length; i++) {
      if (videos[i] == null) {
        s += "[_____]\n";
      } else {
        s += "[" + printVideo(videos[i]) + "]\n";
      }
    }
    return s;
  }

  // Mainmethod zum testen der Video und Videosammlung Klassen
  public static void main(String[] args) {
    String titel;


    // for (int i = 0; i < 2; i++) {
    // titel = readString("Was ist der Titel des " + (i + 1) + ". Video?\n");
    // int n;
    // do {
    // n = read("Wieviele Genres wollen Sie zu " + titel + " hinzufügen [1-5].");
    // } while(n < 1 || n > 5);
    // String[] genres = new String[n];
    // for (int j = 0; j < n; j++) {
    // genres[j] = readString("Bitte geben Sie das " + (j + 1) + ". Genre für " + titel + " ein.");
    // }
    //
    // Video v = new Video(titel);
    // for (int j = 0; j < n; j++) {
    // v.addGenre(genres[j]);
    // }
    // writeLineConsole(printVideo(v));
    // }

    // Testen der Videosammlunng Klasse
    // test ob ich die korrekte Anzahl einfügen kann
    Videosammlung vhs = new Videosammlung(20);
    for (int i = 0; i < 20; i++) {
      Video v = new Video("titel-" + i);
      for (int j = 0; j < (i % 5); j++) {
        v.addGenre("g-" + (j + 1));
      }
      int result = vhs.addVideo(v);
      if (result == -1) {
        fehler("Das " + i + ". Video konnte nicht in die Videosammlung aufgenommen werden.");
      }
    }
    korrekt("Die ersten 20 Videos wurden erfolgreich aufgenommen in die Sammlung.");
    writeLineConsole(printVideosammlung(vhs));
    Video v = new Video("Darf nicht reinpassen");
    int result = vhs.addVideo(v);
    if (result != -1) {
      fehler("Das 21. Video wurde aufgenommen, ob wohl kein Platz mehr sein sollte.");
    } else {
      korrekt("Das 21. Video wurde korrekt abgelehnt, nicely done.");
    }

    // Tests für die Verkaufen Methode
    // Das 21. Video darf nicht gefunden werden
    v = vhs.verkaufen(21);
    if (v != null) {
      fehler("Das 21. Video existiert, ob wohl nur für 20 Platz ist...");
    } else {
      korrekt("Das 21. Video wurde korrekterweise nicht gefunden.");
    }

    v = new Video("Darf nicht reinpassen");
    v.addGenre("genre-test");
    result = vhs.addVideo(v);
    if (result != -1) {
      fehler("Das 21. Video wurde aufgenommen, ob wohl kein Platz mehr sein sollte.");
    } else {
      korrekt("Das 21. Video wurde korrekt abgelehnt, nicely done.");
    }
    v = vhs.verkaufen("Darf nicht reinpassen");
    if (v != null) {
      fehler("Das 21. Video existiert, ob wohl nur für 20 Platz ist...");
    } else {
      korrekt("Das 21. Video wurde korrekterweise nicht gefunden.");
    }

    // einige videos verkaufen und prüfen ob sie wirklich verschwunden sind
    vhs.verkaufen(5);
    vhs.verkaufen(0);
    vhs.verkaufen(11);
    if (vhs.verkaufen("titel-11") == null) {
      korrekt("Das korrekte Video wurde verkauft :).");
    } else {
      fehler("Das falsche Video wurde verkauft.");
    }
    if (vhs.verkaufen("titel-5") == null) {
      korrekt("Das korrekte Video wurde verkauft :).");
    } else {
      fehler("Das falsche Video wurde verkauft.");
    }
    if (vhs.verkaufen("titel-0") == null) {
      korrekt("Das korrekte Video wurde verkauft :).");
    } else {
      fehler("Das falsche Video wurde verkauft.");
    }
    if (vhs.verkaufen(11) == null) {
      korrekt("Das korrekte Video wurde verkauft :).");
    } else {
      fehler("Das falsche Video wurde verkauft.");
    }

    vhs.verkaufen("titel-7");
    if (vhs.verkaufen(7) == null) {
      korrekt("Das korrekte Video wurde verkauft :).");
    } else {
      fehler("Das falsche Video wurde verkauft.");
    }

    // verbleibende testen
    if (vhs.getVerbleibende() == 4) {
      korrekt("Es ist die korrekte Anzahl an verbleibenden Plätzen.");
    } else {
      fehler("Es ist die inkorrekte Anzahl an verbleibenden Plätzen.");
    }

    //
    String[] titelVergleich = new String[] {"titel-4", "titel-9", "titel-14", "titel-19"};
    String[] videos = vhs.videosInGenre("g-4");
    if (Arrays.equals(videos, titelVergleich)) {
      korrekt("Es werden die richtigen Titel entsprechend des Genres ausgegeben.");
    } else {
      fehler("Es werden die falschen Titel zurück gegeben.");
    }
  }

}
