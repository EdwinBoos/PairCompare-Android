package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;

public class ChinLandmarks extends BaseLandmarks implements IEnum
{

    public ChinLandmarks(final ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startChinIndex, LandmarkCodesEnum.endChinIndex));
    }

}