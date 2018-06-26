package influenz.de.paircompare.converter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.core.Mat;

import influenz.de.paircompare.interfaces.IConverter;

public final class NullObjectConverter implements IConverter
{
    @Override
    public Object convert(final Mat mat, final Bitmap bitmap)
    {
        Log.e("NullObjectConverter", "Please check the action you provided to the converterFactory, " +
                "something went wrong.");

        return this;
    }
}
