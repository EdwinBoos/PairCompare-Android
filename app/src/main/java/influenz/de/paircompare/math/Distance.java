package influenz.de.paircompare.math;

import android.graphics.Point;


public class Distance {

 private final Point point1;
 private final Point point2;

 public Distance(final Point point1, final Point point2) {
  this.point1 = point1;
  this.point2 = point2;
 }

 public double compute() {
  return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
 }

}
