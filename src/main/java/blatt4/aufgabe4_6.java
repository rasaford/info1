package blatt4;

import miniJava.MiniJava;

public class aufgabe4_6 extends MiniJava {

  public static void main(String[] args) {
    countLetters("");
    replace('a', 'z', "");
    reverse("");
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
    return "[" + out + "]";
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

  private static boolean matches(char test, char pattern) {
    return test == pattern || test == (char) (pattern - 32);
  }

  private static char getChar(String dialog) {
    String input = readString(dialog);
    if (input.length() != 1) {
      return getChar(dialog);
    }
    return input.charAt(0);
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
