package influenz.de.paircompare.math;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public final class FaceRegion
{

    private final Rect face;

    public FaceRegion(final Rect face)
    {
        this.face = face;
    }

    public Point computeCenterPoint()
    {

       return new Point((face.x + face.width + face.x) / 2,
                                 (face.y + face.y + face.height) / 2);

    }


}
