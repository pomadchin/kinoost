package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;
import com.cyber.kinoost.img.ImageLoader;
import com.cyber.kinoost.views.KPlayer;

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
        film = (Film) getArguments().getSerializable("film");
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		img = film.getImgUrl();
		name = film.getName();
		year = Integer.toString(film.getYear());
		rating = Double.toString(film.getRating());
		
		View myFragmentView = inflater.inflate(R.layout.film2, container, false);

//		headContainer = (RelativeLayout) findViewById(R.id.head_container);
		ImageView imgView = (ImageView) myFragmentView.findViewById(R.id.film_image);
//		TextView nameView = (TextView) this.findViewById(R.id.text_name);
//		TextView yearView = (TextView) this.findViewById(R.id.text_year);
//		TextView rateView = (TextView) this.findViewById(R.id.text_mark);
		
		ImageLoader imageLoader = new ImageLoader(getActivity());
		imageLoader.DisplayImage(img, imgView);
		
		
		
		FilmMusicRepository filmMusicRepo = new FilmMusicRepository(getActivity());

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

			ListView lvMain = (ListView) myFragmentView.findViewById(R.id.soundlist);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, names);

			final KPlayer kPlayer = new KPlayer(getActivity());
			final Account account = new Account(getActivity());
			final ApiHelper apiHelper = new ApiHelper();
			final Api api = new Api(account);
			final Context context = getActivity();

			lvMain.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					if (music != null)
						apiHelper.getSongMusic(context, api, music.get(arg2), kPlayer);
				}
			});

			lvMain.setAdapter(adapter);

		} catch (SQLException e) {
			musicNameList.add("У этого фильма нет саундтреков.");
			e.printStackTrace();
		}

        return myFragmentView;

	}

}
