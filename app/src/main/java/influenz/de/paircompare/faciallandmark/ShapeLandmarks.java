package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;


public class ShapeLandmarks extends BaseLandmarks implements IEnum
{

    public ShapeLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startShapeIndex, LandmarkCodesEnum.endShapeIndex));
    }

}
