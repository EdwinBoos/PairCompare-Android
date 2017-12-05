package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;

public class NoseLongitudeLandmarks extends BaseLandmarks implements IEnum
{

    public NoseLongitudeLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startNoseLongitudeIndex, LandmarkCodesEnum.endNoseLongitudeIndex));
    }

}