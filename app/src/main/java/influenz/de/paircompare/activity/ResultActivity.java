package influenz.de.paircompare.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ImageView;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import influenz.de.paircompare.R;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.util.HaarCascadeLoader;


public class ResultActivity extends Activity implements IEnum
{

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private Paint faceLandmardkPaint;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        imageViewFace1 = (ImageView) findViewById(R.id.face_1_id);
        imageViewFace2 = (ImageView) findViewById(R.id.face_2_id);

        bitmapFace1 = intent.getParcelableExtra(IntentKeyEnum.face1_key);
        bitmapFace2 = intent.getParcelableExtra(IntentKeyEnum.face2_key);

        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                if (!new File(Constants.getFaceShapeModelPath()).exists())
                {
                    new HaarCascadeLoader(ResultActivity.this, R.raw.shape_predictor_68_face_landmarks).load();
                }

                faceLandmardkPaint = new Paint();
                faceLandmardkPaint.setColor(Color.GREEN);
                faceLandmardkPaint.setStrokeWidth(2);
                faceLandmardkPaint.setStyle(Paint.Style.STROKE);
                Canvas canvas = new Canvas();
                canvas.setBitmap(bitmapFace1);
                float resizeRatio = 1;
                FaceDet faceDet = new FaceDet(
                            new HaarCascadeLoader(ResultActivity.this, R.raw.shape_predictor_68_face_landmarks)
                                                  .load()
                                                  .getAbsolutePath());

                List<VisionDetRet> results = faceDet.detect( bitmapFace1 );
                for (final VisionDetRet ret : results)
                {
                    ArrayList<Point> landmarks = ret.getFaceLandmarks();
                    for (Point point : landmarks) {
                        int pointX = (int) (point.x * resizeRatio);
                        int pointY = (int) (point.y * resizeRatio);

                        canvas.drawCircle(pointX, pointY, 2, faceLandmardkPaint);
                    }
                }

                imageViewFace1.setImageBitmap(bitmapFace1);
            }
        });
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