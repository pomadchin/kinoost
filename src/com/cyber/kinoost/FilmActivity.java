package com.cyber.kinoost;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.api.vk.sources.KException;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;
import com.cyber.kinoost.img.ImageLoader;
import com.cyber.kinoost.views.KPlayer;
import com.cyber.kinoost.views.MenuView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FilmActivity extends Activity {
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

		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		//int height = display.getHeight();

		int imgw = (int) Math.round(0.6 * width);
		Log.v("ingWIDTH", Integer.toString(imgw));
		Log.v("width", Integer.toString(width));

		RelativeLayout.LayoutParams lpi = new RelativeLayout.LayoutParams(imgw,
				imgw);
		imgContainer.setLayoutParams(lpi);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,
				imgw);
		headContainer.setLayoutParams(lp);

		imageLoader.DisplayImage(img, imgView);
		imageLoader.DisplayImage(img, imgView);

		nameView.setText(name);
		yearView.setText(year);
		rateView.setText(rating);

		FilmMusicRepository filmMusicRepo = new FilmMusicRepository(this);

		String[] names = { "no music" };
		ArrayList<String> musicNameList = new ArrayList<String>();
		try {
			final List<Music> music = filmMusicRepo.lookupMusicForFilm(film);

			if (music != null) {
				for (Music m : music) {
					if(m.getPerformer().getName() != null)
						musicNameList.add(m.getPerformer().getName() + " - " + m.getName());
					else
						musicNameList.add(m.getName());
				}

				names = musicNameList.toArray(names);
			}

			ListView lvMain = (ListView) findViewById(R.id.list_osts);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, names);

			final KPlayer kPlayer = new KPlayer(this);
			final Account account = new Account(this);
			final ApiHelper apiHelper = new ApiHelper();
			final Api api = new Api(account);
			final Context context = this;

			lvMain.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					if (music != null) {
						try {
							apiHelper.getSoungMusic(context, api, music.get(arg2), kPlayer);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (KException e) {
							e.printStackTrace();
						}
					}
				}
			});

			lvMain.setAdapter(adapter);

		} catch (SQLException e) {
			musicNameList.add("У этого фильма нет саундтреков.");
			e.printStackTrace();
		}
	}
}
