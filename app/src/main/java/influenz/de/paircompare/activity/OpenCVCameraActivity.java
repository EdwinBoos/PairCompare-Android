package influenz.de.paircompare.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
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
import influenz.de.paircompare.interfaces.IConverter;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.util.ConverterFactory;
import influenz.de.paircompare.util.EyeRegion;
import influenz.de.paircompare.util.RawFileLoader;

public class OpenCVCameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, IEnum
{

    private Mat rgba;
    private Mat gray;
    private Rect[] facesArray;
    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private int facesFound = 0;
    private DetectionBasedTracker nativeFaceDetector;
    private Button photoButtonView;
    private Button flipCameraButtonView;
    private MatOfRect faces;
    private CameraBridgeViewBase openCvCameraView;
    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private Switch switchView;
    private PopupWindow popupWindow;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            if (status == LoaderCallbackInterface.SUCCESS)
            {
                System.loadLibrary("detectionBasedTracker");
                RawFileLoader haarCascadeFaceLoader = new RawFileLoader(OpenCVCameraActivity.this, R.raw.haarcascade_frontalface_alt);
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
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_opencvcamera);

        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
        openCvCameraView.setCvCameraViewListener(this);
        photoButtonView = (Button) findViewById(R.id.photo_button_id);
        flipCameraButtonView = (Button) findViewById(R.id.flip_camera_id);
        switchView = (Switch) findViewById(R.id.switch_id);

        View customView = inflater.inflate(R.layout.popup_window, null);
        imageViewFace1 = (ImageView) customView.findViewById(R.id.image_view_face1_id);
        imageViewFace2 = (ImageView) customView.findViewById(R.id.image_view_face2_id);
        popupWindow = new PopupWindow(customView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        pauseCamera();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        photoButtonView.setEnabled(false);
        resumeCamera();
    }

    public void onDestroy()
    {
        super.onDestroy();
        pauseCamera();
    }

    public void handleTakePhotoButtonPress(View view)
    {

        IConverter converterFactory = new ConverterFactory().build(ConverterFactory.MAT_2_BITMAP_ACTION);
        Mat roiFace1 = gray.submat(facesArray[0]);
        Mat roiFace2 = gray.submat(facesArray[0]); // TODO: 1 when prodcutive

        bitmapFace1 = Bitmap.createBitmap(roiFace1.cols(), roiFace1.rows(), Bitmap.Config.ARGB_8888);
        bitmapFace2 = Bitmap.createBitmap(roiFace2.cols(), roiFace2.rows(), Bitmap.Config.ARGB_8888);

        converterFactory.convert(roiFace1, bitmapFace1);
        converterFactory.convert(roiFace2, bitmapFace2);

        imageViewFace1.setImageBitmap(bitmapFace1);
        imageViewFace2.setImageBitmap(bitmapFace2);
        popupWindow.showAtLocation(openCvCameraView, Gravity.CENTER,0,0);

        flipCameraButtonView.setEnabled(false);
        photoButtonView.setEnabled(false);
        switchView.setEnabled(false);
        pauseCamera();

    }


    public void handleSwitchPress(View view)
    {
        photoButtonView.setEnabled(false);
    }


    public void handleRepeatButtonPress(View view)
    {
        switchView.setEnabled(false);
        photoButtonView.setEnabled(false);
        flipCameraButtonView.setEnabled(false);
        resumeCamera();
        popupWindow.dismiss();
    }


    public void handleAcceptButtonPress(View view)
    {
        popupWindow.dismiss();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(IntentKeyEnum.face1_key, bitmapFace1);
        intent.putExtra(IntentKeyEnum.face2_key, bitmapFace2);
        startActivity(intent);
    }


    public void handleFlipCameraButtonPress(View view)
    {
        pauseCamera();
        openCvCameraView.setCameraIndex(
                ( openCvCameraView.getCameraIndex() == CameraBridgeViewBase.CAMERA_ID_BACK)
                        ? CameraBridgeViewBase.CAMERA_ID_FRONT
                        : CameraBridgeViewBase.CAMERA_ID_BACK );
        resumeCamera();
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
        facesArray = faces.toArray();
        facesFound = facesArray.length;
        int facesCounter = 0;

        for (Rect nextFace : facesArray)
        {
            facesCounter++;

            EyeRegion eyeRegion = new EyeRegion(nextFace);
            Imgproc.rectangle(rgba, nextFace.tl(), nextFace.br(), ScalarEnum.scalarFace, ThicknessEnum.rectAngleFace);
            int xCenter = (nextFace.x + nextFace.width + nextFace.x) / 2;
            int yCenter = (nextFace.y + nextFace.y + nextFace.height) / 2;
            Point center = new Point(xCenter, yCenter);
            Imgproc.putText(  rgba, "Face " + facesCounter, new Point(center.x + 20, center.y + 40),
                              Core.FONT_HERSHEY_SIMPLEX, FontSizeEnum.faceCounter, ScalarEnum.scalarText);

            Rect eyeAreaRight = eyeRegion.computeRightEyeRegion();
            Rect eyeAreaLeft = eyeRegion.computeLeftEyeRegion();

            Imgproc.rectangle(rgba, eyeAreaLeft.tl(), eyeAreaLeft.br(), ScalarEnum.scalarEyes, ThicknessEnum.rectAngleEyes);
            Imgproc.rectangle(rgba, eyeAreaRight.tl(), eyeAreaRight.br(), ScalarEnum.scalarEyes, ThicknessEnum.rectAngleEyes);

            // We need to run android view changes on a different thread.
            OpenCVCameraActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    photoButtonView.setEnabled((facesFound > FaceEnum.minFacesFound));
                }
            });

        }

        return rgba;
    }

}