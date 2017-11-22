package influenz.de.paircompare.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import influenz.de.paircompare.R;
import influenz.de.paircompare.hybrid.DetectionBasedTracker;
import influenz.de.paircompare.util.HaarCascadeLoader;

public class OpenCVCameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2
{

    private static final Scalar RECT_COLOR = new Scalar(255, 255, 255);

    private Mat rgba;
    private Mat gray;
    private DetectionBasedTracker nativeFaceDetector;
    private DetectionBasedTracker nativeLeftEyeDetector;
    private DetectionBasedTracker nativeRightEyeDetector;
    private int                    facesFound = 0;
    private int xCenter;
    private int yCenter;
    private int learn_frames = 0;

    private Button photoButton;
    private CameraBridgeViewBase openCvCameraView;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                    System.loadLibrary("detectionBasedTracker");

                    HaarCascadeLoader haarCascadeFaceLoader = new HaarCascadeLoader(
                                                                    OpenCVCameraActivity.this,
                                                                    R.raw.haarcascade_frontalface_alt2,
                                                                    "haarcascade_frontalface_alt2.xml");

                    HaarCascadeLoader haarCascadeLeftEyeLoader = new HaarCascadeLoader(
                                                                    OpenCVCameraActivity.this,
                                                                    R.raw.haarcascade_lefteye_2splits,
                                                                    "haarcascade_lefteye_2splits.xml");

                    HaarCascadeLoader haarCascadeRightEyeLoader = new HaarCascadeLoader(
                                                                    OpenCVCameraActivity.this,
                                                                    R.raw.haarcascade_righteye_2splits,
                                                                    "haarcascade_righteye_2splits.xml");

                    nativeFaceDetector = new DetectionBasedTracker(haarCascadeFaceLoader.getAbsolutePath(), 0);
                    //nativeLeftEyeDetector = new DetectionBasedTracker(haarCascadeLeftEyeLoader.getAbsolutePath(), 0);
                    //nativeRightEyeDetector = new DetectionBasedTracker(haarCascadeRightEyeLoader.getAbsolutePath(), 0);
                    haarCascadeFaceLoader.deleteCascadeDir();
                    haarCascadeLeftEyeLoader.deleteCascadeDir();
                    haarCascadeRightEyeLoader.deleteCascadeDir();
                    openCvCameraView.enableView();

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        openCvCameraView = (JavaCameraView) findViewById(R.id.camera_view);
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
        MatOfRect faces = new MatOfRect();
        nativeFaceDetector.detect(gray, faces);
        Rect[] facesArray = faces.toArray();
        facesFound = facesArray.length;

        for (Rect nextFace : facesArray)
        {
            Imgproc.rectangle(rgba, nextFace.tl(), nextFace.br(), RECT_COLOR, 3);
            xCenter = (nextFace.x + nextFace.width + nextFace.x) / 2;
            yCenter = (nextFace.y + nextFace.y + nextFace.height) / 2;
            Point center = new Point(xCenter, yCenter);

            Imgproc.circle(rgba, center, 10, new Scalar(255, 0, 0, 255), 3);

//            Imgproc.putText(rgba, "[" + center.x + "," + center.y + "]",
//                    new Point(center.x + 20, center.y + 20),
//                    Core.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(255, 255, 255,
//                            255));

            Rect r = nextFace;
            // compute the eye area
            Rect eyearea = new Rect(r.x + r.width / 8,
                    (int) (r.y + (r.height / 4.5)), r.width - 2 * r.width / 8,
                    (int) (r.height / 3.0));
            // split it
            Rect eyearea_right = new Rect(r.x + r.width / 16,
                    (int) (r.y + (r.height / 4.5)),
                    (r.width - 2 * r.width / 16) / 2, (int) (r.height / 3.0));
            Rect eyearea_left = new Rect(r.x + r.width / 16
                    + (r.width - 2 * r.width / 16) / 2,
                    (int) (r.y + (r.height / 4.5)),
                    (r.width - 2 * r.width / 16) / 2, (int) (r.height / 3.0));
            // draw the area - mGray is working grayscale mat, if you want to
            // see area in rgb preview, change mGray to mRgba
            Imgproc.rectangle(rgba, eyearea_left.tl(), eyearea_left.br(),
                    new Scalar(255, 0, 0, 255), 2);
            Imgproc.rectangle(rgba, eyearea_right.tl(), eyearea_right.br(),
                    new Scalar(255, 0, 0, 255), 2);


            }


        // We need to run android view changes on a different thread.
        OpenCVCameraActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                 photoButton.setEnabled(facesFound > 0);
            }
        });

        return rgba;
    }

    public void handleTakePhotoButtonPress(View view)
    {
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




}