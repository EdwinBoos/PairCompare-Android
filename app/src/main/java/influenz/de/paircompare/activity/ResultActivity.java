package influenz.de.paircompare.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.tzutalin.dlib.FaceDet;
import java.io.File;
import java.util.ArrayList;
import influenz.de.paircompare.R;
import influenz.de.paircompare.facefeature.Chin;
import influenz.de.paircompare.factory.FacialLandmarkFactory;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.util.LandmarksDrawer;
import influenz.de.paircompare.util.RawFileLoader;


public class ResultActivity extends Activity implements IEnum
{

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private ArrayList<Point> face1Landmarks;
    private ArrayList<Point> face2Landmarks;
    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private ProgressBar progressBarView;

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        imageViewFace1 = (ImageView) findViewById(R.id.face_1_id);
        imageViewFace2 = (ImageView) findViewById(R.id.face_2_id);

        progressBarView = (ProgressBar) findViewById(R.id.progressbar_id);
        progressBarView.setVisibility(View.VISIBLE);

        bitmapFace1 = intent.getParcelableExtra(IntentKeyEnum.face1_key);
        bitmapFace2 = intent.getParcelableExtra(IntentKeyEnum.face2_key);

        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected Void doInBackground(Void... params)
            {
                Paint faceLandmarkPaint = new Paint();
                LandmarksDrawer canvasFace1 = new LandmarksDrawer(bitmapFace1);
                LandmarksDrawer canvasFace2 = new LandmarksDrawer(bitmapFace2);
                File rawFile = new RawFileLoader(ResultActivity.this, R.raw.shape_predictor_68_face_landmarks).load();
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