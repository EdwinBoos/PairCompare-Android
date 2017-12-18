package influenz.de.paircompare.observer;


import java.util.Observable;

public class BitmapsObservable extends Observable
{

    @Override
    public void notifyObservers()
    {
        setChanged();
        super.notifyObservers();
    }
}
