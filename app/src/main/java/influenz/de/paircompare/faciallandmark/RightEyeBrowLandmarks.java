package influenz.de.paircompare.faciallandmark;


import android.graphics.Point;

import java.util.ArrayList;

import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.interfaces.IFacialLandmark;

public final class RightEyeBrowLandmarks extends ArrayList<Point> implements IFacialLandmark, IEnum
{

    public RightEyeBrowLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks);
        this.subList(LandmarkCodesEnum.startRightEyeBrowIndex, LandmarkCodesEnum.endRightEyeBrowIndex).clear();
    }

    public <T> ArrayList<T> retrieve()
    {
        return (ArrayList<T>) this;
    }
}