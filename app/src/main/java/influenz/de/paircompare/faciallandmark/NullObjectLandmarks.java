package influenz.de.paircompare.faciallandmark;

import android.graphics.Point;
import android.util.Log;
import java.util.ArrayList;
import influenz.de.paircompare.interfaces.IFacialLandmark;

public final class NullObjectLandmarks implements IFacialLandmark {


 public NullObjectLandmarks(final ArrayList < Point > facialLandmarks) {}

 @Override
 public < T > ArrayList < T > retrieve() {
  Log.e("NullObjectLandmarks", "Please check the action you provided to the landmarkFactory, " +
   "something went wrong.");
  return (ArrayList < T > ) new ArrayList < Void > ();
 }
}
