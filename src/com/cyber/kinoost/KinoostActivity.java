package com.cyber.kinoost;

import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.adapters.ListAdapter;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;
import com.cyber.kinoost.db.repositories.UserRepository;
import com.cyber.kinoost.img.ImageLoader;
import com.cyber.kinoost.views.MenuView;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
	
	ArrayList<Tuple<Film, Film>> films = new ArrayList<Tuple<Film, Film>>();
    ListAdapter la;
	
	MenuView menu;
	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	DatabaseHelper dbHelper;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	ImageLoader imageLoader;
	ListView lvMain;

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
		
		// init preferences && editor
		prefs = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = prefs.edit();
		
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
	    la = new ListAdapter(this, films);

	    // настраиваем список
	    lvMain = (ListView) findViewById(R.id.listView1);
	    lvMain.setAdapter(la);
	  }

	  // генерируем данные для адаптера
	  void fillData() {
		FilmMusicRepository filmMusicRepo = new FilmMusicRepository(this);
		try {
			films = filmMusicRepo.findTuplesFilmByName("", 0, 10);
            Log.d("SIZE", Integer.toString(films.size()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  // выводим информацию
	  public void showResult(View v) {
	    String result = "Товары в корзине:";
	    for (Tuple<Film, Film> t : la.getFilms()) {
	      result += "\n" + t.getFst().getName()
	    		  + "\n" + t.getSnd().getName();
	    }
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		// check data update on start
		Date storedDate = new Date(prefs.getLong(MainActivity.APP_PREFERENCES_UPDATE_DATETIME, 0));
		Date newDate = new Date();
		Date updDate = new Date(prefs.getLong(MainActivity.APP_PREFERENCES_UPDATE_DATE, 0));
		long diffDays = (newDate.getTime() - storedDate.getTime()) / (24 * 60 * 60 * 1000);
		
		if(diffDays >= MainActivity.APP_PREFERENCES_DAYS_UPDATE) {
			editor.putLong(MainActivity.APP_PREFERENCES_UPDATE_DATETIME, newDate.getTime());
			editor.commit();
			ApiHelper.dbUpdate(getBaseContext(), updDate);
		}
	}
	
	 @Override
	 public void onDestroy() {
         lvMain.setAdapter(null);
	     super.onDestroy();
	 }
}
