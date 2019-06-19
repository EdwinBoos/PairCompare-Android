package influenz.de.paircompare.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;


public class LandmarksCanvas extends Canvas {

 public LandmarksCanvas(final Bitmap bitmap) {
  super(bitmap);
 }


 public LandmarksCanvas drawLandmarksAsCircle(final ArrayList < Point > landmarks, final int canvasRadius, final Paint paint) {
  for (Point p: landmarks) super.drawCircle(p.x, p.y, canvasRadius, paint);
  return this;
 }
}
