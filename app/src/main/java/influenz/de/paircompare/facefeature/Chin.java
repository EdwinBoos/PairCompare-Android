package influenz.de.paircompare.facefeature;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.math.Angle;

public class Chin
{

    private final ArrayList<Point> chinLandmarks;

    public Chin(final ArrayList<Point> chinLandmarks)
    {
        this.chinLandmarks = chinLandmarks;
    }

    public double getAngle()
    {
        return new Angle(chinLandmarks.get(1), chinLandmarks.get(2), chinLandmarks.get(3)).compute();
    }

    public double getAngleInDegrees()
    {
       return Math.toDegrees(new Angle(chinLandmarks.get(1), chinLandmarks.get(2), chinLandmarks.get(3)).compute());
    }

}
