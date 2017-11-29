package influenz.de.paircompare.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;
import java.util.ArrayList;
import influenz.de.paircompare.R;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.util.RawFileLoader;


public class ResultActivity extends Activity implements IEnum
{

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;

    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private ProgressBar progressBarView;


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
                Canvas canvasFace1 = new Canvas(bitmapFace1);
                faceLandmarkPaint.setColor(Color.GREEN);
                faceLandmarkPaint.setStrokeWidth(ThicknessEnum.strokeWidth);
                faceLandmarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                ArrayList<Point> faceLandmarks =
                        new FaceDet(
                            new RawFileLoader(
                                    ResultActivity.this,
                                    R.raw.shape_predictor_68_face_landmarks)
                                .load()
                                .getAbsolutePath())
                          .detect(bitmapFace1)
                          .get(FaceEnum.Face1Index)
                          .getFaceLandmarks();
+
                for (Point point : faceLandmarks)
                {
                    canvasFace1.drawCircle(point.x, point.y, RadiusEnum.canvasRadius, faceLandmarkPaint);
                }

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