package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.GridViewAdapter;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;

public class TopFilmsFragment extends Fragment {
    
    public TopFilmsFragment() {
        // Empty constructor required for fragment subclasses
    }
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View myFragmentView = inflater.inflate(R.layout.fragment_grid, container, false);
        List<Film> films = getTopFilms();
    	GridView gridview = (GridView) myFragmentView.findViewById(R.id.gridview);
        gridview.setAdapter(new GridViewAdapter(getActivity().getBaseContext(), films));    	
        return myFragmentView;
    }
    
    //FIXME: change method to return top films
    private List<Film> getTopFilms() {
    	FilmMusicRepository filmMusicRepo = new FilmMusicRepository(getActivity().getBaseContext());
    	List<Film> films = null;
    	try {
			films = filmMusicRepo.findFilmByName("", 0, 10);
			Log.d("SIZE", Integer.toString(films.size()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return films;
    	
    } 

}
