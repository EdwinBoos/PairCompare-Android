package influenz.de.paircompare.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import influenz.de.paircompare.R;


public class ResultActivity extends Activity
{

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        imageViewFace1 = (ImageView) findViewById(R.id.image_view_face1_id);
        imageViewFace2 = (ImageView) findViewById(R.id.image_view_face2_id);

        bitmapFace1 = intent.getParcelableExtra("face1_bitmap");
        bitmapFace2 = intent.getParcelableExtra("face2_bitmap");

        imageViewFace1.setImageBitmap(bitmapFace1);
        imageViewFace2.setImageBitmap(bitmapFace2);
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