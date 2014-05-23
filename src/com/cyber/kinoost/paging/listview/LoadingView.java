package com.cyber.kinoost.paging.listview;

import com.cyber.kinoost.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;



public class LoadingView extends LinearLayout {

	public LoadingView(Context context) {
		super(context);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.loading_view, this);
	}


}
