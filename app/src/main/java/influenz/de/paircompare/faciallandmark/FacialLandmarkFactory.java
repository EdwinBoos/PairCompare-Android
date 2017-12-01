package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;

import influenz.de.paircompare.interfaces.IFacialLandmark;


public final class FacialLandmarkFactory
{

    public static final String SHAPE_BUILD = "shapeBuild";
    public static final String CHIN_BUILD = "chinBuild";

    public IFacialLandmark build(String type, ArrayList<Point> facialLandmarks)
    {
        if(type.equalsIgnoreCase(this.SHAPE_BUILD))
        {
            return new ShapeLandmarks(facialLandmarks);
        }
        else if(type.equalsIgnoreCase(this.CHIN_BUILD))
        {
            return new ChinLandmarks(facialLandmarks);
        }
        return new NullObjectLandmarks(facialLandmarks);
    }

}
