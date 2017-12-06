package influenz.de.paircompare.math;

import android.graphics.Point;


public class Angle
{

    private final Point p1;
    private final Point center;
    private final Point p2;


    public Angle(final Point p1, Point center, Point p2)
    {
        this.p1 = p1;
        this.center = center;
        this.p2 = p2;
    }

    public double compute()
    {
        Point transformedP1 = new Point(p1.x - center.x, p1.y - center.y);
        Point transformedP2 = new Point(p2.x - center.x, p2.y - center.y);

        double angleToP1 = Math.atan2(transformedP1.y, transformedP1.x);
        double angleToP2 = Math.atan2(transformedP2.y, transformedP2.x);

        return normaliseAngle(angleToP2 - angleToP1);
    }

    private double normaliseAngle(double angle)
    {
        if (angle < 0) angle += (2 * Math.PI);
        if (angle > Math.PI) angle = 2 * Math.PI - angle;
        return angle;
    }



}
