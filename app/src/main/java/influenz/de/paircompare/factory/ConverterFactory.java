package influenz.de.paircompare.factory;
import influenz.de.paircompare.interfaces.IConverter;
import influenz.de.paircompare.converter.Bitmap2MatConverter;
import influenz.de.paircompare.converter.Mat2BitmapConverter;
import influenz.de.paircompare.converter.NullObjectConverter;

public final class ConverterFactory
{

    public static final String BITMAP_2_MAT_ACTION = "bitmap2mat";
    public static final String MAT_2_BITMAP_ACTION = "mat2Bitmap";

    public IConverter build(final String action)
    {
        if(action.equalsIgnoreCase(this.BITMAP_2_MAT_ACTION))
        {
            return new Bitmap2MatConverter();
        }
        else if(action.equalsIgnoreCase(this.MAT_2_BITMAP_ACTION))
        {
            return new Mat2BitmapConverter();
        }
        else return new NullObjectConverter();
    }

}
