package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;

import java.util.ArrayList;

import influenz.de.paircompare.interfaces.IEnum;


public class OuterLipsLandmark extends BaseLandmarks implements IEnum {

 public OuterLipsLandmark(final ArrayList < Point > facialLandmarks) {
  super(facialLandmarks.subList(LandmarkCodesEnum.startOuterLipIndex, LandmarkCodesEnum.endOuterLipIndex));
 }


}
