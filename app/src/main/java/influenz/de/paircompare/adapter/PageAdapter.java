package influenz.de.paircompare.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public final class PageAdapter extends FragmentStatePagerAdapter {

 private final ArrayList < Fragment > fragmentList = new ArrayList < > ();

 public PageAdapter(final FragmentManager fragmentManager) {
  super(fragmentManager);
 }

 @Override
 public Fragment getItem(final int position) {
  return fragmentList.get(position);
 }

 public void addFragment(final Fragment fragment) {
  fragmentList.add(fragment);
 }

 @Override
 public int getCount() {
  return fragmentList.size();
 }
}
