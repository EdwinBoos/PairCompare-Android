package influenz.de.paircompare.util;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.core.Mat;

import influenz.de.paircompare.interfaces.IConverter;

public final class NullObjectConverter implements IConverter
{
    @Override
    public void convert(Mat mat, Bitmap bitmap)
    {
        Log.e("NullObjectConverter", "Please check the action you provided to the converterFactory, " +
                "something went wrong.");
    }
}
