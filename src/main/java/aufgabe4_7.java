public class aufgabe4_7 extends MiniJava {

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    String[] words = getWords("Please enter the words to merge "
        + "separated by \", \"");
    write(mixedCase(words));
  }


  private static String[] getWords(String dialog) {
    String[] words = new String[0];
    String input = readString(dialog);
    int last = 0;
    for (int i = 0; i < input.length(); i++) {
      // split
      if (i == input.length() - 1 || input.charAt(i) == ',' && input.charAt(i + 1) == ' ') {
        if (i == input.length() - 1) {
          i++;
        }
        int len = words.length;
        String[] tmpWords = new String[len + 1];
        for (int j = 0; j < words.length; j++) {
          tmpWords[j] = words[j];
        }
        String newWord = substring(input, last, i);
        last = i + 2;
        if (!validate(newWord)) {
          return getWords(dialog);
        }
        words = tmpWords;
        words[len] = newWord;
      }
    }
    return words;
  }

  private static String substring(String input, int low, int high) {
    String res = "";
    for (int i = low; i < high; i++) {
      res += input.charAt(i);
    }
    return res;
  }

  private static boolean validate(String word) {
    for (int j = 0; j < word.length(); j++) {
      char c = word.charAt(j);
      if (!('A' <= c && c <= 'Z' || 'a' <= c && c <= 'z')) {
        write("The word " + word + " is not in the range A-Z|a-z");
        return false;
      }
    }
    return true;
  }

  public static String[] mixedCase(String[] words) {
    String[] results = new String[4];
    for (int i = 0; i < words.length; i++) {
      words[i] = toLowerCase(words[i]);
    }
    results[0] = "Startcase: " + startCase(words);
    results[1] = "UPPERCASE: " + upperCase(words);
    results[2] = "snake_case: " + snakeCase(words);
    results[3] = "PascalCase: " + pascalCase(words);
    return results;
  }

  private static String toLowerCase(String input) {
    String res = "";
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      res += ('A' <= c && c <= 'Z') ? (char) (c + 32) : c;
    }
    return res;
  }

  private static String upperCase(String[] input) {
    String res = "";
    for (String word : input) {
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        res += ('A' <= c && c <= 'Z') ? c : (char) (c - 32);
      }
    }
    return res;
  }

  private static String snakeCase(String[] input) {
    String res = "";
    for (int i = 0; i < input.length; i++) {
      String word = input[i];
      if (word.length() == 0) {
        continue;
      }
      if (i != 0) {
        res += '_';
      }
      for (int j = 0; j < word.length(); j++) {
        char c = word.charAt(j);
        res += ('A' <= c && c <= 'Z') ? (char) (c + 32) : c;
      }
    }
    return res;
  }

  private static String pascalCase(String[] input) {
    String res = "";
    for (String word : input) {
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        if (i == 0) {
          res += ('A' <= c && c <= 'Z') ? c : (char) (c - 32);
        } else {
          res += ('A' <= c && c <= 'Z') ? (char) (c + 32) : c;
        }
      }
    }
    return res;
  }

  private static String startCase(String[] input) {
    String res = "";
    for (int i = 0; i < input.length; i++) {
      String word = input[i];
      for (int j = 0; j < word.length(); j++) {
        char c = word.charAt(j);
        if (i == 0 && j == 0) {
          res += ('A' <= c && c <= 'Z') ? c : (char) (c - 32);
        } else {
          res += ('A' <= c && c <= 'Z') ? (char) (c + 32) : c;
        }
      }
    }
    return res;
  }

  public static void write(String[] input) {
    String res = "";
    for (int i = 0; i < input.length; i++) {
      res += input[i] + (i != input.length - 1 ? "\n" : "");
    }
    write(res);
  }
}
