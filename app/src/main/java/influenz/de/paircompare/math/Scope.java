package influenz.de.paircompare.math;


import android.graphics.Point;

import java.util.ArrayList;


public class Scope
{

    private final ArrayList<Point> pointArrayList;

    public Scope(final ArrayList<Point> pointArrayList)
    {

        this.pointArrayList = pointArrayList;
    }


    public double compute()
    {

        for( Point point : this.pointArrayList )
        {

        }

        return 0.0;
    }
}
