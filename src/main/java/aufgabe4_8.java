public class aufgabe4_8 extends MiniJava {

  // utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
  public static void main(String[] args) {
    int key = getIntInRange(0, 64, "Please enter a "
        + "key to use for encrypting your data");
    int vector = getIntInRange(0, 64, "Please enter "
        + "an initialization vector");
    String input = getText("Please enter a Text to encrypt");
    String enc = encrypt(input, key, vector);
    write("Encrypted Text: " + enc);
    write("Decrypted Text: " + decrypt(enc, key, vector));
  }

  private static String getText(String dialog) {
    String input = readString(dialog);
    // validate input
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (!(matches(c, 'a', 'z') ||
          matches(c, '.', ',', ' ', ';', 'ä', 'ö', 'ü'))) {
        write("The text you entered has invalid characters");
        return getText(dialog);
      }
    }
    return input;
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

  public static String encrypt(String word, int key, int init) {
    int[] buffer = encode(word);
    // encryption
    for (int i = 0; i < buffer.length; i++) {
      if (i == 0) {
        buffer[i] = (buffer[i] ^ init) ^ key;
        continue;
      }
      buffer[i] = (buffer[i] ^ buffer[i - 1]) ^ key;
    }
    return decode(buffer);
  }

  public static String decrypt(String word, int key, int init) {
    int[] encode = encode(word);
    // decryption
    int[] buffer = new int[encode.length];
    for (int i = 0; i < encode.length; i++) {
      if (i == 0) {
        buffer[i] = (encode[i] ^ key) ^ init;
        continue;
      }
      buffer[i] = encode[i] ^ key ^ encode[i - 1];
    }
    //decoding
    return decode(buffer);
  }

  private static int[] encode(String word) {
    int[] buffer = new int[word.length()];
    for (int i = 0; i < word.length(); i++) {
      buffer[i] = encode(word.charAt(i));
    }
    return buffer;
  }


  private static int encode(char c) {
    if ('a' <= c && c <= 'z') {
      return c - 97;
    }
    if ('A' <= c && c <= 'Z') {
      return c - 39;
    }
    if ('0' <= c && c <= '9') {
      return c + 4;
    }
    if (c == ' ') {
      return c + 30;
    }
    if (c == '.') {
      return c + 17;
    }
    return c;
  }

  private static String decode(int[] encoded) {
    String res = "";
    for (int i : encoded) {
      res += decode(i);
    }
    return res;
  }

  private static char decode(int i) {
    if (0 <= i && i <= 25) {
      return (char) (i + 97);
    }
    if (26 <= i && i <= 51) {
      return (char) (i + 39);
    }
    if (52 <= i && i <= 61) {
      return (char) (i - 4);
    }
    if (i == 62) {
      return (char) (i - 30);
    }
    if (i == 63) {
      return (char) (i - 17);
    }
    return (char) i;
  }
}


