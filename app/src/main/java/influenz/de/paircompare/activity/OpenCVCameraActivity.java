package influenz.de.paircompare.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import influenz.de.paircompare.R;
import influenz.de.paircompare.hybrid.DetectionBasedTracker;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.util.ConverterFactory;
import influenz.de.paircompare.util.HaarCascadeLoader;

public class OpenCVCameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, IEnum
{

    private Mat rgba;
    private Mat gray;
    private int facesFound = 0;
    private DetectionBasedTracker nativeFaceDetector;
    private Button photoButton;
    private MatOfRect faces;
    private CameraBridgeViewBase openCvCameraView;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            if (status == LoaderCallbackInterface.SUCCESS)
            {
                System.loadLibrary("detectionBasedTracker");
                HaarCascadeLoader haarCascadeFaceLoader = new HaarCascadeLoader(OpenCVCameraActivity.this, R.raw.haarcascade_frontalface_alt2);
                nativeFaceDetector = new DetectionBasedTracker(haarCascadeFaceLoader.load().getAbsolutePath(), FaceEnum.minFaceSize);
                haarCascadeFaceLoader.deleteCascadeDir();
                openCvCameraView.enableView();

            }
            else
            {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_opencvcamera);
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
        openCvCameraView.setCvCameraViewListener(this);
        photoButton = (Button) findViewById(R.id.photo_button_id);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.pauseCamera();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.resumeCamera();
    }

    public void onDestroy()
    {
        super.onDestroy();
        this.pauseCamera();
    }

    public void handleTakePhotoButtonPress(View view)
    {
        new ConverterFactory().build(ConverterFactory.MAT_2_BITMAP_ACTION);
        this.pauseCamera();
    }

    public void handleFlipCameraButtonPress(View view)
    {

        this.resumeCamera();
        openCvCameraView.setCameraIndex(
                ( openCvCameraView.getCameraIndex() == CameraBridgeViewBase.CAMERA_ID_BACK)
                        ? CameraBridgeViewBase.CAMERA_ID_FRONT
                        : CameraBridgeViewBase.CAMERA_ID_BACK );
    }

    private void resumeCamera()
    {
        if (!OpenCVLoader.initDebug())
        {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);

        } else {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    private void pauseCamera()
    {
        if (openCvCameraView != null)
            openCvCameraView.disableView();

    }

    public void onCameraViewStarted(int width, int height)
    {
        gray = new Mat();
        rgba = new Mat();
    }

    public void onCameraViewStopped()
    {
        gray.release();
        rgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {

        rgba = inputFrame.rgba();
        gray = inputFrame.gray();
        faces = new MatOfRect();
        nativeFaceDetector.detect(gray, faces);
        Rect[] facesArray = faces.toArray();
        facesFound = facesArray.length;

        int xCenter;
        int yCenter;
        int facesCounter = 0;

        for (Rect nextFace : facesArray)
        {

            facesCounter++;
            Imgproc.rectangle(rgba, nextFace.tl(), nextFace.br(), ScalarEnum.scalarFace, ThicknessEnum.rectAngleFace);
            xCenter = (nextFace.x + nextFace.width + nextFace.x) / 2;
            yCenter = (nextFace.y + nextFace.y + nextFace.height) / 2;
            Point center = new Point(xCenter, yCenter);

            Imgproc.putText(rgba, "Face " + facesCounter,
                    new Point(center.x + 20, center.y + 20),
                    Core.FONT_HERSHEY_SIMPLEX, FontSizeEnum.faceCounter, ScalarEnum.scalarText);

            // split it
            Rect eyeareaRight = new Rect(nextFace.x + nextFace.width / 16,
                    (int) (nextFace.y + (nextFace.height / 4.5)),
                    (nextFace.width - 2 * nextFace.width / 16) / 2, (int) (nextFace.height / 3.0));
            Rect eyeareaLeft = new Rect(nextFace.x + nextFace.width / 16
                    + (nextFace.width - 2 * nextFace.width / 16) / 2,
                    (int) (nextFace.y + (nextFace.height / 4.5)),
                    (nextFace.width - 2 * nextFace.width / 16) / 2, (int) (nextFace.height / 3.0));

            // draw the area - mGray is working grayscale mat, if you want to
            // see area in rgb preview, change mGray to mRgba
            Imgproc.rectangle(rgba, eyeareaLeft.tl(), eyeareaLeft.br(), ScalarEnum.scalarEyes, ThicknessEnum.rectAngleEyes);
            Imgproc.rectangle(rgba, eyeareaRight.tl(), eyeareaRight.br(), ScalarEnum.scalarEyes, ThicknessEnum.rectAngleEyes);

        }

        // We need to run android view changes on a different thread.
        OpenCVCameraActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                 photoButton.setEnabled(facesFound > FaceEnum.minFacesFound);
            }
        });

        return rgba;
    }

}