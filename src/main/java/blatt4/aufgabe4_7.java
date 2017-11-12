package blatt4;

import java.util.Arrays;

public class aufgabe4_7 {

  public static void main(String[] args) {
    System.out.println(
        Arrays.toString(
            mixedCase(
                new String[]{"this", "IS", "a", "TeST"}
            )));
  }

  public static String[] mixedCase(String[] words) {
    String[] results = new String[4];
    for (int i = 0; i < words.length; i++) {
      words[i] = toLowerCase(words[i]);
    }
    Buffer b = new Buffer();
    results[0] = b.add("Startcase: ").add(startCase(words)).toString();
    b.clear();
    results[1] = b.add("UPPERCASE: ").add(upperCase(words)).toString();
    b.clear();
    results[2] = b.add("snake_case: ").add(snakeCase(words)).toString();
    b.clear();
    results[3] = b.add("PascalCase: ").add(pascalCase(words)).toString();
    return results;
  }

  private static String toLowerCase(String input) {
    Buffer b = new Buffer();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      b.add(('A' <= c && c <= 'Z') ? (char) (c + 32) : c);
    }
    return b.toString();
  }

  private static String upperCase(String[] input) {
    Buffer b = new Buffer();
    for (String word : input) {
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        b.add(('A' <= c && c <= 'Z') ? c : (char) (c - 32));
      }
    }
    return b.toString();
  }

  private static String snakeCase(String[] input) {
    Buffer b = new Buffer();
    for (int i = 0; i < input.length; i++) {
      String word = input[i];
      if (word.length() == 0) {
        continue;
      }
      if (i != 0) {
        b.add('_');
      }
      for (int j = 0; j < word.length(); j++) {
        char c = word.charAt(j);
        b.add(('A' <= c && c <= 'Z') ? (char) (c + 32) : c);
      }
    }
    return b.toString();
  }

  private static String pascalCase(String[] input) {
    Buffer b = new Buffer();
    for (String word : input) {
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        if (i == 0) {
          b.add(('A' <= c && c <= 'Z') ? c : (char) (c - 32));
        } else {
          b.add(('A' <= c && c <= 'Z') ? (char) (c + 32) : c);
        }
      }
    }
    return b.toString();
  }

  private static String startCase(String[] input) {
    Buffer b = new Buffer();
    for (int i = 0; i < input.length; i++) {
      String word = input[i];
      for (int j = 0; j < word.length(); j++) {
        char c = word.charAt(j);
        if (i == 0 && j == 0) {
          b.add(('A' <= c && c <= 'Z') ? c : (char) (c - 32));
        } else {
          b.add(('A' <= c && c <= 'Z') ? (char) (c + 32) : c);
        }
      }
    }
    return b.toString();
  }


  static class Buffer {

    private char[] buffer;
    private int len;

    public Buffer() {
      buffer = new char[8];
    }

    public Buffer add(char c) {
      if (len == buffer.length) {
        char[] old = buffer;
        buffer = new char[old.length * 2];
        for (int i = 0; i < old.length; i++) {
          buffer[i] = old[i];
        }
      }
      buffer[len++] = c;
      return this;
    }

    public Buffer add(String str) {
      for (int i = 0; i < str.length(); i++) {
        add(str.charAt(i));
      }
      return this;
    }

    public Buffer clear() {
      len = 0;
      return this;
    }

    public String toString() {
      String r = "";
      for (int i = 0; i < len; i++) {
        r += buffer[i];
      }
      return r;
    }
  }
}
