package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.OstListViewAdapter;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.MusicRepository;

public class OstFragment extends Fragment {
    
    public OstFragment() {
        // Empty constructor required for fragment subclasses
    }
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_ost, container, false);
    	String songName;
    	try {
    		songName = (String) getArguments().getSerializable("songName");
    	} catch(Exception e) {
    		songName = "";    		
    	}  	
    	
    	List<Music> music = getMusic(songName);
    	
    	ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new OstListViewAdapter(getActivity(), music));
   	
        return rootView;
    }
    
    private List<Music> getMusic(String songName) {
    	MusicRepository musicRepo = new MusicRepository(getActivity().getBaseContext());
    	List<Music> music = null;
    	try {
			music = musicRepo.findMusicByName(songName, 0, 30);
			Log.d("SIZE", Integer.toString(music.size()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return music;
    } 
    

}
