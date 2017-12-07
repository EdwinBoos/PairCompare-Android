package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;


public class RightEyeLandmarks extends BaseLandmarks implements IEnum
{

    public RightEyeLandmarks(final ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startRightEyeIndex, LandmarkCodesEnum.endRightEyeIndex));
    }


}