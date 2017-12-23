package influenz.de.paircompare.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.tzutalin.dlib.FaceDet;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import influenz.de.paircompare.R;
import influenz.de.paircompare.adapter.PageAdapter;
import influenz.de.paircompare.facefeature.Chin;
import influenz.de.paircompare.factory.FacialLandmarkFactory;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.interfaces.IFragmentCreatedListener;
import influenz.de.paircompare.observer.BitmapsObservable;
import influenz.de.paircompare.util.LandmarksCanvas;
import influenz.de.paircompare.util.RawFileLoader;

public class AnalysisFragment extends Fragment implements Observer, IEnum
{

    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private ProgressBar progressBarView;
    private ArrayList<Point> face1Landmarks;
    private ArrayList<Point> face2Landmarks;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
    {
        final View view =  inflater.inflate(R.layout.fragment_analysis, container, false);

        imageViewFace1 = (ImageView) view.findViewById(R.id.face_1_id);
        imageViewFace2 = (ImageView) view.findViewById(R.id.face_2_id);
        progressBarView = (ProgressBar) view.findViewById(R.id.spin_kit_id);
        // viewPager = (ViewPager) view.findViewById(R.id.view_pager_id);

       // pageAdapter = new PageAdapter(getFragmentManager());

        // viewPager.setPageTransformer(false, new RotateUpTransformer());
        // viewPager.setAdapter(pageAdapter);

        //  pageAdapter.addFragment(new AnalysisFragment());
        // pageAdapter.notifyDataSetChanged();
        IFragmentCreatedListener callback = (IFragmentCreatedListener) getActivity();
        callback.onFragmentReady();

        return view;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void update(final Observable observer, final Object argument)
    {
        final BitmapsObservable bitmapsObservable = (BitmapsObservable) observer;
        bitmapFace1 = bitmapsObservable.getBitmapArrayList().get(0);
        bitmapFace2 = bitmapsObservable.getBitmapArrayList().get(1);

        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected Void doInBackground(final Void... params)
            {

                progressBarView.setVisibility(View.VISIBLE);

                final Paint faceLandmarkPaint = new Paint();
                final LandmarksCanvas canvasFace1 = new LandmarksCanvas(bitmapFace1);
                final LandmarksCanvas canvasFace2 = new LandmarksCanvas(bitmapFace2);
                final File rawFile = new RawFileLoader(getContext(), R.raw.shape_predictor_68_face_landmarks).load();

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

                final FacialLandmarkFactory facialLandmarkFactory = new FacialLandmarkFactory(face1Landmarks);
                final ArrayList<Point> chinLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.CHIN_BUILD).retrieve();
                final ArrayList<Point> rightEyeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.RIGHT_EYE_BUILD).retrieve();
                final ArrayList<Point> leftEyeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.LEFT_EYE_BUILD).retrieve();
                final ArrayList<Point> rightEyeBrowLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.RIGHT_EYE_BROW_BUILD).retrieve();
                final ArrayList<Point> leftEyeBrowLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.LEFT_EYE_BROW_BUILD).retrieve();
                final ArrayList<Point> noseLatitudeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.NOSE_LATITUDE_BUILD).retrieve();
                final ArrayList<Point> noseLongitudeLandmarks = facialLandmarkFactory.build(FacialLandmarkFactory.NOSE_LONGITUDE_BUILD).retrieve();

                canvasFace1.drawLandmarksAsCircle(chinLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(rightEyeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(leftEyeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(rightEyeBrowLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(leftEyeBrowLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(noseLatitudeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace1.drawLandmarksAsCircle(noseLongitudeLandmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );
                canvasFace2.drawLandmarksAsCircle(face2Landmarks, RadiusEnum.canvasRadius, faceLandmarkPaint );

                final double chinAngle = new Chin(chinLandmarks).getAngleInDegrees();
                canvasFace1.drawText("chinAngle " + chinAngle , 100,10, faceLandmarkPaint);

                return null;
            }

            @Override
            protected void onPostExecute(final Void unused)
            {
                imageViewFace1.setImageBitmap(bitmapFace1);
                imageViewFace2.setImageBitmap(bitmapFace2);
                progressBarView.setVisibility(View.GONE);
            }

        }.execute();
    }
}
