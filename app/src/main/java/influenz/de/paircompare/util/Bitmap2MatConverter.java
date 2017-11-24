package influenz.de.paircompare.util;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import influenz.de.paircompare.interfaces.IConverter;

public final class Bitmap2MatConverter implements IConverter
{

    @Override
    public Object convert(Mat mat, Bitmap bitmap)
    {
        Utils.bitmapToMat(bitmap, mat);
        return mat;
    }

}
