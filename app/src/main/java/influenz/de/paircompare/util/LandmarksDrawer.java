package influenz.de.paircompare.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by e.boos on 05.12.2017.
 */

public class LandmarksDrawer extends Canvas
{

    public LandmarksDrawer(Bitmap bitmap)
    {
        super(bitmap);
    }


    public LandmarksDrawer drawLandmarksAsCircle(ArrayList<Point> chinLandmarks)
    {

        return this;
    }
}
