package com.cyber.kinoost;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;
import com.cyber.kinoost.img.ImageLoader;
import com.cyber.kinoost.views.MenuView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class FilmActivity extends Activity{	
	MenuView menu;
	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	RelativeLayout imgContainer;
	RelativeLayout headContainer;
	String name = "";
	String img = "";
	String year = "";
	String rating = "";
	Film film = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.film);
		film = (Film) getIntent().getSerializableExtra("film");
		img = film.getImgUrl();
		name = film.getName();
		year = Integer.toString(film.getYear());
		rating = Double.toString(film.getRating());
		menu = new MenuView(this);
		Log.v(Integer.toString(menu.getChildCount()), "asd");
		menuContainer = (RelativeLayout) findViewById(R.id.menu_container);
		listContainer = (RelativeLayout) findViewById(R.id.list_container);
		imgContainer = (RelativeLayout) findViewById(R.id.img_container);
		headContainer = (RelativeLayout) findViewById(R.id.head_container);
		menuContainer.addView(menu);
		ImageLoader imageLoader = new ImageLoader(this);
	    ImageView imgView = (ImageView) this.findViewById(R.id.image_poster);
	    TextView nameView = (TextView) this.findViewById(R.id.text_name);
	    TextView yearView = (TextView) this.findViewById(R.id.text_year);
	    TextView rateView = (TextView) this.findViewById(R.id.text_mark);
		ToggleButton toogleButton = (ToggleButton) findViewById(R.id.main_button);
		toogleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(buttonView.isChecked()){
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(listContainer.getWidth(), LayoutParams.MATCH_PARENT);   
            	   lp.setMargins(menuContainer.getWidth(), 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }else{
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            	   lp.setMargins(0, 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }
            }
        });
		

		//узнаем высоту и ширину экрана
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth(); //да, я знаю что устарело, но так удобнее
		int height = display.getHeight(); 
		
		int imgw = (int) Math.round(0.6*width);
		Log.v("ingWIDTH", Integer.toString(imgw));
		Log.v("width", Integer.toString(width));
		RelativeLayout.LayoutParams lpi = new RelativeLayout.LayoutParams(imgw, imgw);
	 	imgContainer.setLayoutParams(lpi);
	 	
	   
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, imgw);
		headContainer.setLayoutParams(lp);
	 	
	 	imageLoader.DisplayImage(img, imgView);
 	  imageLoader.DisplayImage(img, imgView);
 	  
 	 nameView.setText(name);
 	 yearView.setText(year);
 	 rateView.setText(rating);
 	  
 	  //список песенок

 	 FilmMusicRepository filmMusicRepo = new FilmMusicRepository(this);

	  String[] names = {"no music"};
	  ArrayList<String> ostlistList = new ArrayList<String>();
 	 try {
		List<Music> osts = filmMusicRepo.lookupMusicForFilm(film); 	  
	 	  
	 	  for(int i=0; i<osts.size(); i++){	 		  
	 		 ostlistList.add(osts.get(i).getName());	 		  
	 	  }
	 	  
	} catch (SQLException e) {
		ostlistList.add("NO FCKIN MUSIC!!!11");
		e.printStackTrace();
	}
		
 	  names = ostlistList.toArray(names);

 
 		    ListView lvMain = (ListView) findViewById(R.id.list_osts);

 		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
 		        R.layout.list_item, names);

 		    lvMain.setAdapter(adapter);
    	
	}
}
