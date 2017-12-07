package influenz.de.paircompare.factory;

import android.graphics.Point;
import java.util.ArrayList;

import influenz.de.paircompare.faciallandmark.ChinLandmarks;
import influenz.de.paircompare.faciallandmark.LeftEyeBrowLandmarks;
import influenz.de.paircompare.faciallandmark.LeftEyeLandmarks;
import influenz.de.paircompare.faciallandmark.NoseLatitudeLandmarks;
import influenz.de.paircompare.faciallandmark.NoseLongitudeLandmarks;
import influenz.de.paircompare.faciallandmark.NullObjectLandmarks;
import influenz.de.paircompare.faciallandmark.OuterLipsLandmark;
import influenz.de.paircompare.faciallandmark.RightEyeBrowLandmarks;
import influenz.de.paircompare.faciallandmark.RightEyeLandmarks;
import influenz.de.paircompare.faciallandmark.ShapeLandmarks;
import influenz.de.paircompare.faciallandmark.InnerLipsLandmarks;
import influenz.de.paircompare.interfaces.IFacialLandmark;


public final class FacialLandmarkFactory
{

    public static final String SHAPE_BUILD = "shapeBuild";
    public static final String CHIN_BUILD = "chinBuild";
    public static final String RIGHT_EYE_BROW_BUILD = "rightEyeBrowBuild";
    public static final String LEFT_EYE_BROW_BUILD = "leftEyeBrowBuild";
    public static final String NOSE_LONGITUDE_BUILD = "noseLongitudeBuild";
    public static final String NOSE_LATITUDE_BUILD = "noseLatitudeBuild";
    public static final String RIGHT_EYE_BUILD = "rightEyeBuild";
    public static final String LEFT_EYE_BUILD = "leftEyeBuild";
    public static final String INNER_LIP_BUILD = "innerLipBuild";
    public static final String OUTER_LIP_BUILD = "outerLipBuild";

    private final ArrayList<Point> facialLandmarks;


    public FacialLandmarkFactory(final ArrayList<Point> facialLandmarks)
    {
        this.facialLandmarks = facialLandmarks;
    }

    public IFacialLandmark build(final String type)
    {
        if(type.equalsIgnoreCase(SHAPE_BUILD))
            return new ShapeLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(CHIN_BUILD))
            return new ChinLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(RIGHT_EYE_BROW_BUILD))
            return new RightEyeBrowLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(LEFT_EYE_BROW_BUILD))
            return new LeftEyeBrowLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(NOSE_LONGITUDE_BUILD))
            return new NoseLongitudeLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(NOSE_LATITUDE_BUILD))
            return new NoseLatitudeLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(RIGHT_EYE_BUILD))
            return new RightEyeLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(LEFT_EYE_BUILD))
            return new LeftEyeLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(INNER_LIP_BUILD))
            return new InnerLipsLandmarks(facialLandmarks);

        else if(type.equalsIgnoreCase(OUTER_LIP_BUILD))
            return new OuterLipsLandmark(facialLandmarks);

        else return new NullObjectLandmarks(facialLandmarks);
    }

}
