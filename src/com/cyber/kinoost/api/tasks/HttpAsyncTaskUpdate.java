package com.cyber.kinoost.api.tasks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.cyber.kinoost.api.models.JsonUpdate;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cyber.kinoost.api.*;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;
import com.cyber.kinoost.db.repositories.FilmRepository;
import com.cyber.kinoost.db.repositories.MusicRatingRepository;
import com.cyber.kinoost.db.repositories.MusicRepository;
import com.cyber.kinoost.db.repositories.PerformerRepository;
import com.cyber.kinoost.db.repositories.UserRepository;

public class HttpAsyncTaskUpdate extends AsyncTask<String, Void, String> {
	private Context context;
	
	public HttpAsyncTaskUpdate(Context context) {
		super();
		this.context = context;
	}
	
	private Context getContext() {
		return this.context;
	}
	
    @Override
    protected String doInBackground(String... params) {
    	if(params.length > 2)
    		ApiHelper.POST(params[0], params[2]);
    	
        return ApiHelper.GET(params[0]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Log.d("onPostExecute", result);
        
        JsonFactory f = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
			JsonParser jp = f.createParser(result);
			jp.nextToken();
			
			
			List<JsonUpdate> listUpdate = mapper.readValue(jp,TypeFactory.defaultInstance().constructCollectionType(List.class,JsonUpdate.class));
			Log.d("HttpAsyncTask.onPostExecute", listUpdate.toString());
			
			for (JsonUpdate jsonUpdate: listUpdate ) {
				
				FilmRepository filmRepo = new FilmRepository(getContext());
				MusicRepository musicRepo = new MusicRepository(getContext());
				FavoritesRepository favoritesRepo = new FavoritesRepository(getContext());
				PerformerRepository performerRepo = new PerformerRepository(getContext());
				FilmMusicRepository filmmusicRepo =  new FilmMusicRepository(getContext());
				
				switch ( jsonUpdate.getMethod() ){
				
				case ADD:
					
					filmRepo.createFilmList(jsonUpdate.getFilms());
									
					musicRepo.createMusicList(jsonUpdate.getMusic());
					
					favoritesRepo.createFavoritesList(jsonUpdate.getFavorites());
							
					performerRepo.createPerformerList(jsonUpdate.getPerformers());
						
					filmmusicRepo.createFilmMusicList(jsonUpdate.getFilmMusic());
					break;
				
				case DELETE:
					
					filmRepo.deleteFilmList(jsonUpdate.getFilms());
									
					musicRepo.deleteMusicList(jsonUpdate.getMusic());
									
					favoritesRepo.deleteFavoritesList(jsonUpdate.getFavorites());
								
					performerRepo.deletePerformerList(jsonUpdate.getPerformers());
									
					filmmusicRepo.deleteFilmMusicList(jsonUpdate.getFilmMusic());
					break;
				
				case REPLACE:
						
					filmRepo.editFilmList(jsonUpdate.getFilms());
					
					musicRepo.editMusicList(jsonUpdate.getMusic());
					
					favoritesRepo.editFavoritesList(jsonUpdate.getFavorites());
								
					performerRepo.editPerformerList(jsonUpdate.getPerformers());
					
					filmmusicRepo.editFilmMusicList(jsonUpdate.getFilmMusic());
					break;
					}	
				
				
				
			}
						
			
			// TODO: persist data Diman
		} catch (JsonParseException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
			
			
			
			
			
		} catch (IOException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		}
        
        FilmRepository filmRepo = new FilmRepository(getContext());
		MusicRepository musicRepo = new MusicRepository(getContext());
		FavoritesRepository favoritesRepo = new FavoritesRepository(getContext());
		PerformerRepository performerRepo = new PerformerRepository(getContext());
		FilmMusicRepository filmmusicRepo =  new FilmMusicRepository(getContext());
        
		try{					
			List<Film> film = filmRepo.findFilmByName("", 0, 10);
			Log.d("kinoost-filmRepo-findFilmByName", film.toString() );
			
			List<Music> music = musicRepo.findMusicByName("",0, 10);
			Log.d("kinoost-musicRepo-findMusicByName", music.toString());
			
			List<Performer> performer = performerRepo.findPerformerByName("", 0, 10);
			Log.d("kinoost-performerRepo-findPeromerByName", performer.toString());
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Log.d("repoFail:", e.getMessage());
				e.printStackTrace();
			}
        
   }
}