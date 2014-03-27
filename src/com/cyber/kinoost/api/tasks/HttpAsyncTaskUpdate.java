package com.cyber.kinoost.api.tasks;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cyber.kinoost.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.cyber.kinoost.api.models.JsonUpdate;
import com.cyber.kinoost.api.models.JsonSend;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import android.os.AsyncTask;
import com.cyber.kinoost.api.*;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.db.repositories.*;

public class HttpAsyncTaskUpdate extends AsyncTask<String, Void, String> {
	public static final String APP_PREFERENCES = "com.cyber.kinoost";
	public static final String APP_PREFERENCES_UPDATE_DATE = "com.cyber.kinoost.update.date";
	
	private Context context;

	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	public HttpAsyncTaskUpdate(Context context) {
		super();
		this.context = context;
		prefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = prefs.edit();
	}
	
	private Context getContext() {
		return this.context;
	}
	
    @Override
    protected String doInBackground(String... params) {
    	ApiHelper.POST(params[0], jsonCreate());
        return ApiHelper.GET(params[0] + params[1]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Log.d("onPostExecute", result);
        
        FilmRepository filmRepo = new FilmRepository(getContext());
		MusicRepository musicRepo = new MusicRepository(getContext());
		FavoritesRepository favoritesRepo = new FavoritesRepository(getContext());
		PerformerRepository performerRepo = new PerformerRepository(getContext());
		FilmMusicRepository filmmusicRepo =  new FilmMusicRepository(getContext());
        
        JsonFactory f = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
			JsonParser jp = f.createParser(result);
			jp.nextToken();
			
			List<JsonUpdate> listUpdate = mapper.readValue(jp,TypeFactory.defaultInstance().constructCollectionType(List.class,JsonUpdate.class));
			Log.d("HttpAsyncTask.onPostExecute", listUpdate.toString());
			
			Date updDate = new Date();
			
			for (JsonUpdate jsonUpdate: listUpdate) {
				switch (jsonUpdate.getMethod()){
				    case ADD:
					    filmRepo.createFilmList(jsonUpdate.getFilms());
					    musicRepo.createMusicList(jsonUpdate.getMusic());
					    favoritesRepo.createFavoritesList(jsonUpdate.getFavorites());
						performerRepo.createPerformerList(jsonUpdate.getPerformers());
					    filmmusicRepo.createFilmMusicList(jsonUpdate.getFilmMusic());
					    updDate = jsonUpdate.getUpdateDate();
					break;

                    case DELETE:					
					    filmRepo.deleteFilmList(jsonUpdate.getFilms());
					    musicRepo.deleteMusicList(jsonUpdate.getMusic());
					    favoritesRepo.deleteFavoritesList(jsonUpdate.getFavorites());
					    performerRepo.deletePerformerList(jsonUpdate.getPerformers());
					    filmmusicRepo.deleteFilmMusicList(jsonUpdate.getFilmMusic());
					    updDate = jsonUpdate.getUpdateDate();
					break;
				
                    case REPLACE:
					    filmRepo.editFilmList(jsonUpdate.getFilms());
					    musicRepo.editMusicList(jsonUpdate.getMusic());
					    favoritesRepo.editFavoritesList(jsonUpdate.getFavorites());
					    performerRepo.editPerformerList(jsonUpdate.getPerformers());
					    filmmusicRepo.editFilmMusicList(jsonUpdate.getFilmMusic());
					    updDate = jsonUpdate.getUpdateDate();
					break;
                }	
			}
			
			editor.putLong(APP_PREFERENCES_UPDATE_DATE, updDate.getTime());
			editor.commit();
		} catch (JsonParseException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		}
        
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
    
    private String jsonCreate(){
		JsonSend json = new JsonSend();
		FavoritesRepository favoritesRepo = new FavoritesRepository(getContext());
		MusicRatingRepository musicRatingRepo = new MusicRatingRepository(getContext());
	    Date updDate = new Date(prefs.getLong(APP_PREFERENCES_UPDATE_DATE, 0));
	    String result = "";
		try{
		    json.setFavorites(favoritesRepo.getFavorites(0, 0));
		    json.setMusicRating(musicRatingRepo.getMusicRatingByDate(updDate, 0, 0));
		    result = json.toString();
		    Log.d("jsonCreate", result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}