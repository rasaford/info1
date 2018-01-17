package nogivan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MapPointTest {


  @Test
  public void distance() {
    MapPoint a = new MapPoint(50.066389, -5.714722);
    MapPoint b = new MapPoint(58.643889, -3.07);
    assertEquals(968, a.distance(b) / 1000);
  }
}
