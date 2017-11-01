package miniJava;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MiniJava {

  protected static String readString(String text) {
    JFrame frame = new JFrame();
    String s = JOptionPane.showInputDialog(frame, text);
    frame.dispose();

    if (s == null) {
      System.exit(0);
    }
    return s;
  }

  protected static String readString() {
    return readString("Eingabe:");
  }

  protected static int readInt(String text) {
    JFrame frame = new JFrame();
    String s = JOptionPane.showInputDialog(frame, text);
    frame.dispose();

    int x;
    if (s == null) {
      System.exit(0);
    }
    try {
      x = Integer.parseInt(s.trim());
    } catch (NumberFormatException e) {
      x = readInt(text);
    }
    return x;
  }

  protected static int readInt() {
    return readInt("Geben Sie eine ganze Zahl ein:");
  }

  protected static int read(String text) {
    return readInt(text);
  }

  protected static int read() {
    return readInt();
  }

  protected static double readDouble(String text) {
    JFrame frame = new JFrame();
    String s = JOptionPane.showInputDialog(frame, text);
    frame.dispose();

    double x;
    if (s == null) {
      System.exit(0);
    }
    try {
      x = Double.parseDouble(s.trim());
    } catch (NumberFormatException e) {
      x = readDouble(text);
    }
    return x;
  }

  protected static double readDouble() {
    return readDouble("Geben Sie eine Zahl ein:");
  }

  protected static void write(String output) {
    JFrame frame = new JFrame();
    JOptionPane.showMessageDialog(frame, output, "Ausgabe", JOptionPane.PLAIN_MESSAGE);
    frame.dispose();
  }

  protected static void write(int output) {
    write("" + output);
  }

  protected static void write(double output) {
    write("" + output);
  }

  protected static void writeLineConsole(String output) {
    System.out.println(output);
  }

  protected static void writeLineConsole(int output) {
    writeLineConsole("" + output);
  }

  protected static void writeLineConsole(double output) {
    writeLineConsole("" + output);
  }

  protected static void writeLineConsole() {
    writeLineConsole("");
  }

  protected static void writeConsole(String output) {
    System.out.print(output);
  }

  protected static void writeConsole(int output) {
    writeConsole("" + output);
  }

  protected static void writeConsole(double output) {
    writeConsole("" + output);
  }

  protected static int randomInt(int minval, int maxval) {
    return minval + (new java.util.Random()).nextInt(maxval - minval + 1);
  }

  protected static int drawCard() {
    return randomInt(2, 11);
  }

  protected static int dice() {
    return randomInt(1, 6);
  }
}
