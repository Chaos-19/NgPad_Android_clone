package com.chaosdev.ngpad.ui.main;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.chaosdev.ngpad.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

  @StringRes
  private static final int[] TAB_TITLES =
      new int[] {
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3,
        R.string.tab_text_4,
        R.string.tab_text_5,
        R.string.tab_text_6,
        R.string.tab_text_7
      };

  private final Context mContext;

  public SectionsPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    // return PlaceholderFragment.newInstance(position + 1);
        Bundle args = new Bundle();
        args.putInt("tab_Index",position);
        
    switch (position) {
      case 0:
        return new HomeFragment();
      case 1:
        return new TutorialFragment();
      case 2:
        return new QuizFragment();
      case 3:
        return new InterviewFragment();
      case 4:
        return new NewsFragment();
      case 5:
        return new LearnMoreFragment();
      case 6:
        return new HelpCenterFragment();
        // return PlaceholderFragment.newInstance(position + 1);
      default:
        throw new IllegalArgumentException("Invalid tab position");
    }
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mContext.getResources().getString(TAB_TITLES[position]);
  }

  @Override
  public int getCount() {
    // Show 2 total pages.
    return 7;
  }
}
