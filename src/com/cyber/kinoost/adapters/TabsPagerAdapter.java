package com.cyber.kinoost.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.cyber.kinoost.fragments.FilmsFragment;
import com.cyber.kinoost.fragments.OstFragment;
import com.cyber.kinoost.fragments.TopRatedFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new FilmsFragment();
		case 1:
			// Games fragment activity
			return new OstFragment();
		case 2:
			// Movies fragment activity
			return new TopRatedFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}