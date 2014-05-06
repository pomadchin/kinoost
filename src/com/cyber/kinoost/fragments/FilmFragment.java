package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.ListViewAdapter;
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

	public static class ViewHolder {
		public ImageView image;
		public TextView name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		img = film.getImgUrl();
		name = film.getName();
		year = Integer.toString(film.getYear());
		rating = Double.toString(film.getRating());

		View myFragmentView = inflater.inflate(R.layout.soundlist, container,
				false);

		ListView listView = (ListView) myFragmentView;

		// headContainer = (RelativeLayout) findViewById(R.id.head_container);
		// ImageView imgView = (ImageView)
		// myFragmentView.findViewById(R.id.film_image);
		// TextView nameView = (TextView) this.findViewById(R.id.text_name);
		// TextView yearView = (TextView) this.findViewById(R.id.text_year);
		// TextView rateView = (TextView) this.findViewById(R.id.text_mark);

		ImageLoader imageLoader = new ImageLoader(getActivity());
		// imageLoader.DisplayImage(img, imgView);

		FilmMusicRepository filmMusicRepo = new FilmMusicRepository(
				getActivity());

		Object[] names = { "no music" };

		List<Music> music = null;
		try {
			music = filmMusicRepo.lookupMusicForFilm(film);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView list = (ListView) myFragmentView.findViewById(R.id.soundlist);

		list.setAdapter(new ListViewAdapter(getActivity(), film, music));

		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(getActivity(),
				R.layout.list_item, names);

//		final KPlayer kPlayer = new KPlayer(getActivity());
//		final Account account = new Account(getActivity());
//		final ApiHelper apiHelper = new ApiHelper();
//		final Api api = new Api(account);
//		final Context context = getActivity();

		// lvMain.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// if (music != null)
		// apiHelper.getSongMusic(context, api, music.get(arg2), kPlayer);
		// }
		// });
		//
		// lvMain.setAdapter(adapter);

		return myFragmentView;

	}

}
