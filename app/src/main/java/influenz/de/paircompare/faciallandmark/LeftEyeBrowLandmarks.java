package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IEnum;


public class LeftEyeBrowLandmarks extends BaseLandmarks implements IEnum {

 public LeftEyeBrowLandmarks(final ArrayList < Point > facialLandmarks) {
  super(facialLandmarks.subList(LandmarkCodesEnum.startLeftEyeBrowIndex, LandmarkCodesEnum.endLeftEyeBrowIndex));
 }


}
