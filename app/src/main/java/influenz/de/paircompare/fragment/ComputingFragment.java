package influenz.de.paircompare.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import influenz.de.paircompare.R;
import influenz.de.paircompare.facefeature.Chin;
import influenz.de.paircompare.factory.FacialLandmarkFactory;
import influenz.de.paircompare.observer.ChildFragmentObservable;

public class ComputingFragment extends Fragment implements Observer
{

    private TextView textView1;
    private TextView textView2;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_computing, container, false);
        textView1 = (TextView) view.findViewById(R.id.text_view_1_id);
        textView2 = (TextView) view.findViewById(R.id.text_view_2_id);

        return view;
    }

    @Override
    public void update(final Observable observable, final Object argument)
    {

        final ChildFragmentObservable childFragmentObservable = (ChildFragmentObservable) observable;
        final ArrayList<Point> face1Landmarks = childFragmentObservable.getFace1Landmarks();
        final ArrayList<Point> face2Landmarks = childFragmentObservable.getFace2Landmarks();

        final FacialLandmarkFactory facialLandmarkFactory1 = new FacialLandmarkFactory(face1Landmarks);
        final FacialLandmarkFactory facialLandmarkFactory2 = new FacialLandmarkFactory(face2Landmarks);
        final ArrayList<Point> chinLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.CHIN_BUILD).retrieve();
        final ArrayList<Point> rightEyeLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.RIGHT_EYE_BUILD).retrieve();
        final ArrayList<Point> leftEyeLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.LEFT_EYE_BUILD).retrieve();
        final ArrayList<Point> rightEyeBrowLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.RIGHT_EYE_BROW_BUILD).retrieve();
        final ArrayList<Point> leftEyeBrowLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.LEFT_EYE_BROW_BUILD).retrieve();
        final ArrayList<Point> noseLatitudeLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.NOSE_LATITUDE_BUILD).retrieve();
        final ArrayList<Point> noseLongitudeLandmarks1 = facialLandmarkFactory1.build(FacialLandmarkFactory.NOSE_LONGITUDE_BUILD).retrieve();
        final ArrayList<Point> chinLandmarks2 = facialLandmarkFactory2.build(FacialLandmarkFactory.CHIN_BUILD).retrieve();

        final double chinAngle1 = new Chin(chinLandmarks1).getAngleInDegrees();
        final double chinAngle2 = new Chin(chinLandmarks2).getAngleInDegrees();
        textView1.setText(getString(R.string.chin_angle_text_1) + Math.round(chinAngle1));
        textView2.setText(getString(R.string.chin_angle_text_2) + Math.round(chinAngle2));


    }
}
