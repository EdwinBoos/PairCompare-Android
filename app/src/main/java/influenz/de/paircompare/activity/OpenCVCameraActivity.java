package influenz.de.paircompare.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import influenz.de.paircompare.R;
import influenz.de.paircompare.fragment.LoadingFragment;
import influenz.de.paircompare.hybrid.DetectionBasedTracker;
import influenz.de.paircompare.interfaces.IConverter;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.factory.ConverterFactory;
import influenz.de.paircompare.interfaces.IFragmentCreatedListener;
import influenz.de.paircompare.math.EyeRegion;
import influenz.de.paircompare.math.FaceRegion;
import influenz.de.paircompare.observer.BitmapsObservable;
import influenz.de.paircompare.util.RawFileLoader;

public class OpenCVCameraActivity extends FragmentActivity implements CameraBridgeViewBase.CvCameraViewListener2, IEnum, IFragmentCreatedListener
{

    private Mat rgba;
    private Mat gray;
    private Bitmap bitmapFace1;
    private Bitmap bitmapFace2;
    private int facesFound = 0;
    private DetectionBasedTracker nativeFaceDetector;
    private Button photoButtonView;
    private Button flipCameraButtonView;
    private LoadingFragment loadingFragment;
    private MatOfRect faces;
    private CameraBridgeViewBase openCvCameraView;
    private CircleImageView imageViewFace1;
    private CircleImageView imageViewFace2;
    private Switch switchView;
    private PopupWindow popupWindow;

    private final BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(final int status)
        {
            if (status == LoaderCallbackInterface.SUCCESS)
            {
                System.loadLibrary("detectionBasedTracker");
                final RawFileLoader haarCascadeFaceLoader = new RawFileLoader(OpenCVCameraActivity.this, R.raw.haarcascade_frontalface_alt);
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
    public void onCreate(final Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_opencvcamera);

        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
        openCvCameraView.setCvCameraViewListener(this);
        photoButtonView = (Button) findViewById(R.id.photo_button_id);
        flipCameraButtonView = (Button) findViewById(R.id.flip_camera_id);
        switchView = (Switch) findViewById(R.id.switch_id);

        loadingFragment = new LoadingFragment();
        final View customView = inflater.inflate(R.layout.popup_window, null);
        imageViewFace1 = (CircleImageView) customView.findViewById(R.id.image_view_face1_id);
        imageViewFace2 = (CircleImageView) customView.findViewById(R.id.image_view_face2_id);
        popupWindow = new PopupWindow(customView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

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

    public void handleTakePhotoButtonPress(final View view)
    {

        if(faces.toArray().length > FaceEnum.minFacesFound)
        {
            final IConverter converterFactory = new ConverterFactory().build(ConverterFactory.MAT_2_BITMAP_ACTION);
            final Mat roiFace1 = gray.submat(faces.toArray()[0]);
            final Mat roiFace2 = gray.submat(faces.toArray()[0]);

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

    }

    public void handleSwitchPress(final View view)
    {
        photoButtonView.setEnabled(false);
    }

    public void handleRepeatButtonPress(final View view)
    {
        switchView.setEnabled(true);
        photoButtonView.setEnabled(false);
        flipCameraButtonView.setEnabled(true);
        resumeCamera();
        popupWindow.dismiss();
    }

    public void handleAcceptButtonPress(final View view)
    {

        popupWindow.dismiss();

        getSupportFragmentManager().beginTransaction()
                                   .addToBackStack(null)
                                   .replace(R.id.container_id, loadingFragment)
                                   .commit();
    }

    public void handleFlipCameraButtonPress(final View view)
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
        }
        else
        {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    private void pauseCamera()
    {
        if (openCvCameraView != null)
            openCvCameraView.disableView();

    }

    public void onCameraViewStarted(final int width, final int height)
    {
        gray = new Mat();
        rgba = new Mat();
    }

    public void onCameraViewStopped()
    {
        gray.release();
        rgba.release();
    }

    @Override
    public void onFragmentReady()
    {
        final ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(bitmapFace1);
        bitmaps.add(bitmapFace2);

        final BitmapsObservable bitmapsObservable = new BitmapsObservable(bitmaps);
        bitmapsObservable.addObserver(loadingFragment);
        bitmapsObservable.notifyObservers();
    }


    public Mat onCameraFrame(final CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {

        rgba = inputFrame.rgba();
        gray = inputFrame.gray();
        faces = new MatOfRect();
        nativeFaceDetector.detect(gray, faces);
        facesFound = faces.toArray().length;
        int facesCounter = 0;

        for (final Rect nextFace : faces.toArray())
        {
            facesCounter++;

            final EyeRegion eyeRegion = new EyeRegion(nextFace);
            final FaceRegion faceRegion = new FaceRegion(nextFace);
            Imgproc.rectangle(rgba, nextFace.tl(), nextFace.br(), ScalarEnum.scalarFace, ThicknessEnum.rectAngleFace);
            final Point center = faceRegion.computeCenterPoint();
            Imgproc.putText(  rgba, "Face " + facesCounter, new Point(center.x, center.y ),
                              Core.FONT_HERSHEY_SIMPLEX, FontSizeEnum.faceCounter, ScalarEnum.scalarText);

            final Rect eyeAreaRight = eyeRegion.computeRightEyeRegion();
            final Rect eyeAreaLeft = eyeRegion.computeLeftEyeRegion();

            Imgproc.rectangle(rgba, eyeAreaLeft.tl(), eyeAreaLeft.br(), ScalarEnum.scalarEyes, ThicknessEnum.rectAngleEyes);
            Imgproc.rectangle(rgba, eyeAreaRight.tl(), eyeAreaRight.br(), ScalarEnum.scalarEyes, ThicknessEnum.rectAngleEyes);

        }

        // We need to run android view changes on a different thread.
        OpenCVCameraActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                photoButtonView.setEnabled((facesFound > FaceEnum.minFacesFound));
            }
        });

        return rgba;
    }


}