package influenz.de.paircompare.observer;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.Observable;

public final class BitmapsObservable extends Observable {

 private final ArrayList < Bitmap > bitmapArrayList;

 public BitmapsObservable(final ArrayList < Bitmap > bitmapArrayList) {
  this.bitmapArrayList = bitmapArrayList;
 }

 @Override
 public void notifyObservers() {
  setChanged();
  super.notifyObservers();
 }

 public ArrayList < Bitmap > getBitmapArrayList() {
  return bitmapArrayList;
 }


}
