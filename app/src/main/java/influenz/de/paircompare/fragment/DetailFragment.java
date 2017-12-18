package influenz.de.paircompare.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.tzutalin.dlib.FaceDet;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import influenz.de.paircompare.R;
import influenz.de.paircompare.facefeature.Chin;
import influenz.de.paircompare.factory.FacialLandmarkFactory;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.observer.BitmapsObservable;
import influenz.de.paircompare.util.LandmarksDrawer;
import influenz.de.paircompare.util.RawFileLoader;

public class DetailFragment extends Fragment implements Observer, IEnum
{

    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private ProgressBar progressBarView;
    private ArrayList<Point> face1Landmarks;
    private ArrayList<Point> face2Landmarks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view =  inflater.inflate(R.layout.fragment_detail, container, false);

        imageViewFace1 = (ImageView) view.findViewById(R.id.face_1_id);
        imageViewFace2 = (ImageView) view.findViewById(R.id.face_2_id);
        progressBarView = (ProgressBar) view.findViewById(R.id.progressbar_id);

        return view;
    }


    public void forceCreateView(final FragmentManager supportFragmentManager)
    {
        supportFragmentManager
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commitAllowingStateLoss();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void update(Observable observer, Object argument)
    {
        BitmapsObservable bitmapsObservable = (BitmapsObservable) observer;
        bitmapFace1 = bitmapsObservable.getBitmapArrayList().get(0);
        bitmapFace2 = bitmapsObservable.getBitmapArrayList().get(1);

        imageViewFace1.setImageBitmap(bitmapFace1);
        imageViewFace2.setImageBitmap(bitmapFace2);

        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected Void doInBackground(Void... params)
            {

                progressBarView.setVisibility(View.VISIBLE);

                Paint faceLandmarkPaint = new Paint();
                LandmarksDrawer canvasFace1 = new LandmarksDrawer(bitmapFace1);
                LandmarksDrawer canvasFace2 = new LandmarksDrawer(bitmapFace2);
                File rawFile = new RawFileLoader(getContext(), R.raw.shape_predictor_68_face_landmarks).load();

                faceLandmarkPaint.setColor(Color.GREEN);
                faceLandmarkPaint.setStrokeWidth(ThicknessEnum.strokeWidth);
                faceLandmarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                face1Landmarks =
                        new FaceDet(rawFile.getAbsolutePath())
                                .detect(bitmapFace1)
                                .get(FaceEnum.Face1Index)
                                .getFaceLandmarks();

                face2Landmarks =
                        new FaceDet(rawFile.getAbsolutePath())
                                .detect(bitmapFace2)
                                .get(FaceEnum.Face2Index)
                                .getFaceLandmarks();

                FacialLandmarkFactory facialLandmarkFactory = new FacialLandmarkFactory(face1Landmarks);

                ArrayList<Point> chinLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.CHIN_BUILD).retrieve();
                ArrayList<Point> rightEyeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.RIGHT_EYE_BUILD).retrieve();
                ArrayList<Point> leftEyeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.LEFT_EYE_BUILD).retrieve();
                ArrayList<Point> rightEyeBrowLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.RIGHT_EYE_BROW_BUILD).retrieve();
                ArrayList<Point> leftEyeBrowLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.LEFT_EYE_BROW_BUILD).retrieve();
                ArrayList<Point> noseLatitudeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.NOSE_LATITUDE_BUILD).retrieve();
                ArrayList<Point> noseLongitudeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.NOSE_LONGITUDE_BUILD).retrieve();

                canvasFace1.drawLandmarksAsCircle(chinLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(rightEyeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(leftEyeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(rightEyeBrowLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(leftEyeBrowLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(noseLatitudeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(noseLongitudeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace2.drawLandmarksAsCircle(face2Landmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );

                double chinAngle = new Chin(chinLandmarks).getAngleInDegrees();
                canvasFace1.drawText("chinAngle " + chinAngle , 50,10, faceLandmarkPaint);

                return null;
            }

            @Override
            protected void onPostExecute(Void unused)
            {
                imageViewFace1.setImageBitmap(bitmapFace1);
                imageViewFace2.setImageBitmap(bitmapFace2);
                progressBarView.setVisibility(View.GONE);
            }

        }.execute();
    }
}
