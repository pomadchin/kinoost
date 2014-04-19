package com.cyber.kinoost;

import com.cyber.kinoost.views.KPlayer;
import com.cyber.kinoost.views.MenuView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PActivity extends Activity {
	MenuView menu;
	RelativeLayout menuContainer;
	RelativeLayout listContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttons);
		
		menu = new MenuView(this);
		Log.v(Integer.toString(menu.getChildCount()), "asd");
		menuContainer = (RelativeLayout) findViewById(R.id.menu_container);
		listContainer = (RelativeLayout) findViewById(R.id.list_container);
		menuContainer.addView(menu);
		ToggleButton toogleButton = (ToggleButton) findViewById(R.id.main_button);
		toogleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (buttonView.isChecked()) {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							listContainer.getWidth(), LayoutParams.MATCH_PARENT);
					lp.setMargins(menuContainer.getWidth(), 0, 0, 0);
					listContainer.setLayoutParams(lp);
				} else {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					lp.setMargins(0, 0, 0, 0);
					listContainer.setLayoutParams(lp);
				}
			}
		});
		
		Intent i = new Intent();
		i.setClass(this, KPlayer.class);
		startActivity(i);
	}
}
