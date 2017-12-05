package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;

import java.util.ArrayList;

import influenz.de.paircompare.interfaces.IEnum;


public class InnerLipsLandmarks extends BaseLandmarks implements IEnum
{

    public InnerLipsLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks.subList(LandmarkCodesEnum.startInnerLipIndex, LandmarkCodesEnum.endInnerLipIndex));
    }


}