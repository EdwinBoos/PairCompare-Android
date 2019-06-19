package influenz.de.paircompare.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.tzutalin.dlib.FaceDet;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import influenz.de.paircompare.R;
import influenz.de.paircompare.adapter.PageAdapter;
import influenz.de.paircompare.interfaces.IEnum;
import influenz.de.paircompare.interfaces.IFragmentCreatedListener;
import influenz.de.paircompare.observer.BitmapsObservable;
import influenz.de.paircompare.observer.ChildFragmentObservable;
import influenz.de.paircompare.util.XMLFileLoader;

public class LoadingFragment extends Fragment implements Observer, IEnum {

 private Bitmap bitmapFace1;
 private Bitmap bitmapFace2;
 private FacialLandmarksFragment facialLandmarksFragment;
 private ComputingFragment computingFragment;
 private ViewPager viewPager;
 private PageAdapter pageAdapter;
 private ProgressBar progressBarView;
 private ArrayList < Point > face1Landmarks;
 private ArrayList < Point > face2Landmarks;


 @Override
 public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
  final View view = inflater.inflate(R.layout.fragment_loading, container, false);

  progressBarView = (ProgressBar) view.findViewById(R.id.spin_kit_id);
  viewPager = (ViewPager) view.findViewById(R.id.view_pager_id);

  pageAdapter = new PageAdapter(getFragmentManager());
  viewPager.setPageTransformer(false, new CubeOutTransformer());

  facialLandmarksFragment = new FacialLandmarksFragment();
  computingFragment = new ComputingFragment();

  IFragmentCreatedListener callback = (IFragmentCreatedListener) getActivity();
  callback.onFragmentReady();

  return view;
 }


 @SuppressLint("StaticFieldLeak")
 @Override
 public void update(final Observable observable, final Object argument) {
  final BitmapsObservable bitmapsObservable = (BitmapsObservable) observable;
  bitmapFace1 = bitmapsObservable.getBitmapArrayList().get(0);
  bitmapFace2 = bitmapsObservable.getBitmapArrayList().get(1);

  new AsyncTask < Void, Void, Void > () {

   @Override
   protected void onPreExecute() {
    progressBarView.setVisibility(View.VISIBLE);
   }

   @Override
   protected Void doInBackground(final Void...params) {

    final File rawFile = new XMLFileLoader(getContext(), R.raw.shape_predictor_68_face_landmarks).load();
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

    return null;
   }

   @Override
   protected void onPostExecute(final Void unused) {
    progressBarView.setVisibility(View.GONE);

    pageAdapter.addFragment(facialLandmarksFragment);
    pageAdapter.addFragment(computingFragment);
    viewPager.setAdapter(pageAdapter);

    final ChildFragmentObservable childFragmentObservable = new ChildFragmentObservable(face1Landmarks, face2Landmarks, bitmapFace1, bitmapFace2);
    childFragmentObservable.addObserver(facialLandmarksFragment);
    childFragmentObservable.addObserver(computingFragment);
    childFragmentObservable.notifyObservers();

   }

  }.execute();
 }
}
