package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;

public class RightEyeBrowLandmarks extends BaseLandmarks implements IEnum
{

    public RightEyeBrowLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startRightEyeBrowIndex, LandmarkCodesEnum.endRightEyeBrowIndex));
    }

}