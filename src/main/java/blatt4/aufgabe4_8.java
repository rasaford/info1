package blatt4;

import java.util.Arrays;

public class aufgabe4_8 {

  public static void main(String[] args) {
    System.out.println(
        encrypt("The magic number is 42", 23, 55)
    );
  }


  private static String encrypt(String word, int key, int init) {
    // encoding
    int[] encode = new int[word.length()];
    for (int i = 0; i < word.length(); i++) {
      encode[i] = encode(word.charAt(i));
    }

    // encryption
    for (int i = 0; i < encode.length; i++) {
      if (i == 0) {
        encode[i] = (encode[i] ^ init) ^ key;
        continue;
      }
      encode[i] = (encode[i] ^ encode[i - 1]) ^ key;
    }

    // decryption
    int[] decode = new int[word.length()];
    for (int i = 0; i < encode.length; i++) {
      if (i == 0) {
        decode[i] = (encode[i] ^ key) ^ init;
        continue;
      }
      decode[i] = encode[i] ^ key ^ encode[i - 1];
    }

    // decoding & String conversion
    String res = "";
    for (int i : decode) {
      res += decode(i);
    }
    return res;
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


