package com.cyber.kinoost.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyber.kinoost.R;

public class AppInfoFragment extends Fragment {

	public AppInfoFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.fragment_info,
				container, false);
		return myFragmentView;
	}

}
