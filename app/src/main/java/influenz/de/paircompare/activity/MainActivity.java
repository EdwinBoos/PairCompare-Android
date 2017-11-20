package influenz.de.paircompare.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.WindowManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import influenz.de.paircompare.R;
import influenz.de.paircompare.hybrid.DetectionBasedTracker;
import influenz.de.paircompare.util.HaarCascadeLoader;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);

    private Mat rgba;
    private Mat gray;
    private DetectionBasedTracker nativeDetector;

    private float                  relativeFaceSize   = 0.2f;
    private int                    absoluteFaceSize   = 0;


    private CameraBridgeViewBase openCvCameraView;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                    System.loadLibrary("detectionBasedTracker");

                    HaarCascadeLoader haarCascadeLoader = new HaarCascadeLoader(
                            MainActivity.this,
                            R.raw.haarcascade_frontalface_default,
                            "haarcascade_frontalface_default.xml");

                    nativeDetector = new DetectionBasedTracker(haarCascadeLoader.getAbsolutePath(), 0);
                    haarCascadeLoader.deleteCascadeDir();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
        openCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug())
        {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);
        } else
        {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    public void disableCamera() {
        if (openCvCameraView != null)
            openCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        gray = new Mat();
        rgba = new Mat();
    }

    public void onCameraViewStopped() {

        gray.release();
        rgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        rgba = inputFrame.rgba();
        gray = inputFrame.gray();

        if (absoluteFaceSize == 0) {
            int height = gray.rows();
            if (Math.round(height * relativeFaceSize) > 0) {
                absoluteFaceSize = Math.round(height * relativeFaceSize);
            }
            nativeDetector.setMinFaceSize(absoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        nativeDetector.detect(gray, faces);

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(rgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);

        return rgba;
    }


}