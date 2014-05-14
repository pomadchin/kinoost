package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.GridViewAdapter;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;

public class FilmsFragment extends Fragment {
    
    public FilmsFragment() {
        // Empty constructor required for fragment subclasses
    }    
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	String filmName;
    	try {
    		filmName = (String) getArguments().getSerializable("filmName");
    	} catch(Exception e) {
    		filmName = "";    		
    	}  	
    	View myFragmentView = inflater.inflate(R.layout.fragment_grid, container, false);
        List<Film> films = getFilms(filmName);
    	
    	GridView gridview = (GridView) myFragmentView.findViewById(R.id.gridview);
        gridview.setAdapter(new GridViewAdapter(getActivity(), films));
        
        return myFragmentView;
    }
    
    private List<Film> getFilms(String filmName) {
    	FilmMusicRepository filmMusicRepo = new FilmMusicRepository(getActivity().getBaseContext());
    	List<Film> films = null;
    	try {
			films = filmMusicRepo.findFilmByName(filmName, 0, 30);
			Log.d("SIZE", Integer.toString(films.size()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return films;
    } 

}
