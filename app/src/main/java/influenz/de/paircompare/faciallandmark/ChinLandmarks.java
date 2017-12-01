package influenz.de.paircompare.faciallandmark;


import android.graphics.Point;

import java.util.ArrayList;

import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.interfaces.IFacialLandmark;

public final class ChinLandmarks extends ArrayList<Point> implements IFacialLandmark, IEnum
{

    public ChinLandmarks(ArrayList<Point> facialLandmarks)
    {
        super(facialLandmarks);
        this.subList(LandmarkCodesEnum.startChinIndex, LandmarkCodesEnum.endChinIndex).clear();
    }

    public <T> ArrayList<T> retrieve()
    {
        return (ArrayList<T>) this;
    }
}