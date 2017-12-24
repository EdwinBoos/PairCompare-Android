package influenz.de.paircompare.observer;

import android.graphics.Bitmap;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.Observable;

public final class ChildFragmentObservable extends Observable
{

    private final Bitmap bitmapFace1;
    private final Bitmap bitmapFace2;
    private final ArrayList<Point> face1Landmarks;
    private final ArrayList<Point> face2Landmarks;

    public ChildFragmentObservable(final ArrayList<Point> face1Landmarks,
                                   final ArrayList<Point> face2Landmarks,
                                   final Bitmap bitmapFace1,
                                   final Bitmap bitmapFace2) {

        this.bitmapFace1 = bitmapFace1;
        this.bitmapFace2 = bitmapFace2;
        this.face1Landmarks = face1Landmarks;
        this.face2Landmarks = face2Landmarks;
    }

    @Override
    public void notifyObservers()
    {
        setChanged();
        super.notifyObservers();
    }

    public ArrayList<Point> getFace1Landmarks()
    {
        return face1Landmarks;
    }

    public ArrayList<Point> getFace2Landmarks()
    {
        return face2Landmarks;
    }

    public Bitmap getBitmapFace1()
    {
        return bitmapFace1;
    }

    public Bitmap getBitmapFace2()
    {
        return bitmapFace2;
    }


}
