package nogivan;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GPXWriter {
  private PrintWriter writer;

  public GPXWriter(String fileName) throws FileNotFoundException {
    writer = new PrintWriter(new File(fileName));
  }

  private void writeLine(String line) {
    writer.println(line);
  }

  public void close() {
    writer.close();
  }

  public void writeGPX(RoutingResult rr) {
    // Todo
  }
}
