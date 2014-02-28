package com.cyber.kinoost;


import java.util.ArrayList;

import views.MenuView;
import adapters.Film;
import adapters.ListAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class KinoostActivity  extends Activity {
	
	ArrayList<Film> films = new ArrayList<Film>();
    ListAdapter la;
	
	MenuView menu;
	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menu = new MenuView(this);
		Log.v(Integer.toString(menu.getChildCount()), "asd");
		menuContainer = (RelativeLayout) findViewById(R.id.menu_container);
		listContainer = (RelativeLayout) findViewById(R.id.list_container);
		menuContainer.addView(menu);
		ToggleButton toogleButton = (ToggleButton) findViewById(R.id.main_button);
		toogleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(buttonView.isChecked()){
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(listContainer.getWidth(), FrameLayout.LayoutParams.WRAP_CONTENT);   
            	   lp.setMargins(menuContainer.getWidth(), 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }else{
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            	   lp.setMargins(0, 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }
            }
        });
	    fillData();
	    ListAdapter la = new ListAdapter(this, films);

	    // настраиваем список
	    ListView lvMain = (ListView) findViewById(R.id.listView1);
	    lvMain.setAdapter(la);
	  }

	  // генерируем данные для адаптера
	  void fillData() {
	    for (int i = 1; i <= 20; i++) {
	      films.add(new Film("Product " + i, i * 1000,
	          R.drawable.ic_launcher, false));
	    }
	  }

	  // выводим информацию
	  public void showResult(View v) {
	    String result = "Товары в корзине:";
	    for (Film p : la.getBox()) {
	      if (p.box)
	        result += "\n" + p.name;
	    }
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
}
