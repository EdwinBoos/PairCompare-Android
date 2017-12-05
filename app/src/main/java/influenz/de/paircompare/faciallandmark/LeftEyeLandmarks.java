package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;

import java.util.ArrayList;

import influenz.de.paircompare.interfaces.IEnum;


public class LeftEyeLandmarks extends BaseLandmarks implements IEnum
{

    public LeftEyeLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startLeftEyeIndex, LandmarkCodesEnum.endLeftEyeIndex));
    }


}