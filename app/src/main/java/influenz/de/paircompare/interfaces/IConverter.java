package influenz.de.paircompare.interfaces;


import android.graphics.Bitmap;

import org.opencv.core.Mat;

public interface IConverter {

 public Object convert(final Mat mat, final Bitmap bitmap);

}
