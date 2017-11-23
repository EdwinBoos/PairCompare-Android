package influenz.de.paircompare.interfaces;


import android.graphics.Bitmap;

import org.opencv.core.Mat;

public interface IConverter
{
    public void convert(Mat mat, Bitmap bitmap);

}
