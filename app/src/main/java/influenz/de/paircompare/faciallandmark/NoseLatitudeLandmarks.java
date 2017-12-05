package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;

public class NoseLatitudeLandmarks extends BaseLandmarks implements IEnum
{

    public NoseLatitudeLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startNoseLatitudeIndex, LandmarkCodesEnum.endNoseLatitudeIndex));
    }

}
