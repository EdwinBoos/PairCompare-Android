package influenz.de.paircompare.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;


public class LandmarksDrawer extends Canvas
{

    public LandmarksDrawer(Bitmap bitmap)
    {
        super(bitmap);
    }


    public LandmarksDrawer drawLandmarksAsCircle(ArrayList<Point> landmarks, int canvasRadius, Paint paint)
    {
        for (Point p : landmarks) super.drawCircle(p.x, p.y, canvasRadius, paint);
        return this;
    }
}
