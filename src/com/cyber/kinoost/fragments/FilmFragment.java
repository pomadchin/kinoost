package com.cyber.kinoost.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.img.ImageLoader;

public class FilmFragment extends Fragment {
	
	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	RelativeLayout imgContainer;
	RelativeLayout headContainer;
	String name = "";
	String img = "";
	String year = "";
	String rating = "";
	Film film = null;

	public FilmFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

//		film = (Film) getIntent().getSerializableExtra("film");
//		img = film.getImgUrl();
//		name = film.getName();
//		year = Integer.toString(film.getYear());
//		rating = Double.toString(film.getRating());
//		imgContainer = (RelativeLayout) findViewById(R.id.img_container);
//		headContainer = (RelativeLayout) findViewById(R.id.head_container);
//		ImageLoader imageLoader = new ImageLoader(this);
//		ImageView imgView = (ImageView) this.findViewById(R.id.image_poster);
//		TextView nameView = (TextView) this.findViewById(R.id.text_name);
//		TextView yearView = (TextView) this.findViewById(R.id.text_year);
//		TextView rateView = (TextView) this.findViewById(R.id.text_mark);
		

		View myFragmentView = inflater.inflate(R.layout.film,
				container, false);
		

		return myFragmentView;
	}

}
