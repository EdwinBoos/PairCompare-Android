package influenz.de.paircompare.fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import influenz.de.paircompare.R;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.observer.ChildFragmentObservable;
import influenz.de.paircompare.util.LandmarksCanvas;

public class FacialLandmarksFragment extends Fragment implements Observer
{

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_facial_landmarks, container, false);
        imageViewFace1 = (ImageView) view.findViewById(R.id.face_1_id);
        imageViewFace2 = (ImageView) view.findViewById(R.id.face_2_id);
        return view;
    }

    @Override
    public void update(final Observable observable, final Object argument)
    {

        final ChildFragmentObservable childFragmentObservable = (ChildFragmentObservable) observable;
        final ArrayList<Point> face1Landmarks = childFragmentObservable.getFace1Landmarks();
        final ArrayList<Point> face2Landmarks = childFragmentObservable.getFace2Landmarks();
        final Bitmap bitmapFace1 = childFragmentObservable.getBitmapFace1();
        final Bitmap bitmapFace2 = childFragmentObservable.getBitmapFace2();
        final Paint faceLandmarkPaint = new Paint();
        final LandmarksCanvas canvasFace1 = new LandmarksCanvas(bitmapFace1);
        final LandmarksCanvas canvasFace2 = new LandmarksCanvas(bitmapFace2);

        faceLandmarkPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
        faceLandmarkPaint.setStrokeWidth(IEnum.ThicknessEnum.strokeWidth);
        faceLandmarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvasFace1.drawLandmarksAsCircle(face1Landmarks, IEnum.RadiusEnum.canvasRadius, faceLandmarkPaint );
        canvasFace2.drawLandmarksAsCircle(face2Landmarks, IEnum.RadiusEnum.canvasRadius, faceLandmarkPaint );

        imageViewFace1.setImageBitmap(bitmapFace1);
        imageViewFace2.setImageBitmap(bitmapFace2);

    }
}
