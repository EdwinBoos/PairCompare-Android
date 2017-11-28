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

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import influenz.de.paircompare.R;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.util.RawFileLoader;


public class ResultActivity extends Activity implements IEnum
{

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;

    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private Paint faceLandmarkPaint;
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
            protected Void doInBackground(Void... params) {
                if (!new File(Constants.getFaceShapeModelPath()).exists())
                {
                    new RawFileLoader(ResultActivity.this, R.raw.shape_predictor_68_face_landmarks).load();
                }

                faceLandmarkPaint = new Paint();
                faceLandmarkPaint.setColor(Color.RED);
                faceLandmarkPaint.setStrokeWidth(1);
                faceLandmarkPaint.setStyle(Paint.Style.STROKE);
                Canvas canvas = new Canvas();
                canvas.setBitmap(bitmapFace1);
                float resizeRatio = 1;
                FaceDet faceDet = new FaceDet(
                        new RawFileLoader(ResultActivity.this, R.raw.shape_predictor_68_face_landmarks)
                                .load()
                                .getAbsolutePath());

                List<VisionDetRet> results = faceDet.detect( bitmapFace1 );
                for (final VisionDetRet ret : results)
                {
                    ArrayList<Point> landmarks = ret.getFaceLandmarks();
                    for (Point point : landmarks) {
                        int pointX = (int) (point.x * resizeRatio);
                        int pointY = (int) (point.y * resizeRatio);

                        canvas.drawCircle(pointX, pointY, 2, faceLandmarkPaint);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused)
            {
                imageViewFace1.setImageBitmap(bitmapFace1);
                progressBarView.setVisibility(View.GONE);
            }

        }.execute();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void onDestroy()
    {
        super.onDestroy();

    }

}