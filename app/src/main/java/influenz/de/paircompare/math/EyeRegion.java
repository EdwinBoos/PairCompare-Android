package influenz.de.paircompare.math;
import org.opencv.core.Rect;

public final class EyeRegion
{

    private final Rect face;

    public EyeRegion(Rect face)
    {
        this.face = face;
    }

    public Rect computeLeftEyeRegion()
    {
       return new Rect(
               face.x + face.width / 16 + (face.width - 2 * face.width / 16) / 2,
               (int) (face.y + (face.height / 4.5)),
               (face.width - 2 * face.width / 16) / 2,
               (int) (face.height / 3.0)
       );
    }


    public Rect computeRightEyeRegion()
    {
        return new Rect(
                face.x + face.width / 16,
                (int) (face.y + (face.height / 4.5)),
                (face.width - 2 * face.width / 16) / 2,
                (int) (face.height / 3.0)
        );
    }





}
