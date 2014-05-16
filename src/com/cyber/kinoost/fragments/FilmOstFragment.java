package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.FilmOstListViewAdapter;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;

public class FilmOstFragment extends Fragment {

	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	RelativeLayout imgContainer;
	RelativeLayout headContainer;
	String name = "";
	String img = "";
	String year = "";
	String rating = "";
	Film film = null;

	public FilmOstFragment() {
		// Empty constructor required for fragment subclasses
	}


	public static class ViewHolder {
		public ImageView image;
		public TextView name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		film = (Film) getArguments().getSerializable("film");
		img = film.getImgUrl();
		name = film.getName();
		year = Integer.toString(film.getYear());
		rating = Double.toString(film.getRating());

		View myFragmentView = inflater.inflate(R.layout.fragment_film_ost, container,
				false);

		FilmMusicRepository filmMusicRepo = new FilmMusicRepository(
				getActivity());
		List<Music> music = null;
		try {
			music = filmMusicRepo.lookupMusicForFilm(film);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView list = (ListView) myFragmentView.findViewById(R.id.soundlist);
		list.setAdapter(new FilmOstListViewAdapter(getActivity(), film, music));
		return myFragmentView;

	}

}
