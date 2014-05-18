package com.cyber.kinoost.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.cyber.kinoost.fragments.FilmsByNameFragment;
import com.cyber.kinoost.fragments.MusicFragment;
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
			return new FilmsByNameFragment();
		case 1:
			// Games fragment activity
			return new MusicFragment();
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