package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import influenz.de.paircompare.interfaces.IFacialLandmark;

public abstract class BaseLandmarks extends ArrayList < Point > implements IFacialLandmark {

 public BaseLandmarks(final List < Point > landmarks) {
  super(landmarks);
 }

 public < T > ArrayList < T > retrieve() {
  return (ArrayList < T > ) this;
 }

}
