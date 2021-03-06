package influenz.de.paircompare.converter;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import influenz.de.paircompare.interfaces.IConverter;

public final class Mat2BitmapConverter implements IConverter {

 @Override
 public Object convert(final Mat mat, final Bitmap bitmap) {
  Utils.matToBitmap(mat, bitmap);
  return bitmap;
 }

}
