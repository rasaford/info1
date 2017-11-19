package blatt4;

import miniJava.MiniJava;

public class aufgabe4_6 extends MiniJava {

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    String text = getText("Please enter a text to modify with stingray:");
    int choice = getIntInRange(1, 4, "What do you want to do with it? \n"
        + "1: Count the letters\n"
        + "2: replace letters\n"
        + "3: word mirroring");
    switch (choice) {
      case 1:
        write(countLetters(text));
        break;
      case 2:
        char from = getChar("Enter the letter you want to match");
        char to = getChar("Enter the letter you want to match");
        write(replace(from, to, text));
        break;
      case 3:
        write(reverse(text));
        break;
    }
  }

  private static String getText(String dialog) {
    String input = readString(dialog);
    // validate input
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (!(matches(c, 'a', 'z') ||
          matches(c, '.', ',', ' ',';', 'ä', 'ö', 'ü', '?', '!'))) {
        write("The text you entered has invalid characters");
        return getText(dialog);
      }
    }
    return input;
  }

  private static char getChar(String dialog) {
    String input = readString(dialog);

    if (input.length() != 1) {
      write("You can only enter one character");
      return getChar(dialog);
    }
    char c = input.charAt(0);
    if (!(matches(c, 'a', 'z') ||
        matches(c, 'ä', 'ö', 'ü'))) {
      write("The input has to be a letter");
      return getChar(dialog);
    }
    return input.charAt(0);
  }

  /**
   * getIntInRange gets an int from the user in the given range.
   *
   * @param min min of the input value (inclusive)
   * @param max max of the input value (exclusive)
   * @param dialog Message to display to the user
   * @return user input
   */
  private static int getIntInRange(int min, int max, String dialog) {
    int input = readInt(dialog);
    if (input < min || input >= max) {
      write(String.format("The given number has to be in the range %d <= x < %d", min, max));
      return getIntInRange(min, max, dialog);
    }
    return input;
  }

  public static String countLetters(String text) {
    // 26 + 3
    int[] counts = new int[30];
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      counts[hash(c)] += 1;
    }
    return writeChars(counts);
  }

  private static String writeChars(int[] counts) {
    String out = "";
    for (int i = 1; i < counts.length; i++) {
      if (counts[i] == 0) {
        continue;
      }
      out += hashInv(i) + ": " + counts[i] + " ";
    }
    return out;
  }


  private static int hash(char c) {
    // A-Z -> a-z, Ä -> ä, Ü -> ü, Ö -> ö
    if ('A' <= c && c <= 'Z' || c == 'Ä' || c == 'Ö' || c == 'Ü') {
      c = (char) (c + 32);
    }
    // if char is out of range map it to 0
    if (c < 'a' || c > 'z' && c != 'ä' && c != 'ö' && c != 'ü') {
      return 0;
    }
    if (c == 'ä') {
      c -= 105;
    } else if (c == 'ö') {
      c -= 122;
    } else if (c == 'ü') {
      c -= 127;
    }
    // layout: * a-z ä ö ü
    c -= 96;
    return c;
  }

  private static char hashInv(int i) {
    if (i == 27) { // ä
      i += 105;
    } else if (i == 28) { // ö
      i += 122;
    } else if (i == 29) { // ü
      i += 127;
    }
    return (char) (i + 64);
  }

  public static String replace(char from, char to, String text) {
    from = toLowerCase(from);
    to = toLowerCase(to);
    String res = "";

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (matches(c, from)) {
        c = (c > 'Z') ? to : (char) (to - 32);
      }
      res += c;
    }

    return res;
  }

  private static char toLowerCase(char c) {
    return (c < 'Z') ? (char) (c + 32) : c;
  }

  private static boolean matches(char test, char... pattern) {
    boolean match = false;
    for (char c : pattern) {
      match = match || (test == c || test == (char) (c - 32));
    }
    return match;
  }

  private static boolean matches(char test, char start, char end) {
    return start <= test && test <= end ||
        (char) (start - 32) <= test && test <= (char) (end - 32);
  }


  public static String reverse(String text) {
    String word = "";
    String result = "";
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == ' ') {
        result += reverseWord(word) + " ";
        word = "";
      } else if (i == text.length() - 1) {
        word += c;
        result += reverseWord(word);
      } else {
        word += c;
      }
    }
    return result;
  }


  private static String reverseWord(String word) {
    String res = "";
    for (int i = word.length() - 1; i >= 0; i--) {
      char c = word.charAt(i);
      res += c;
    }
    return res;
  }
}
