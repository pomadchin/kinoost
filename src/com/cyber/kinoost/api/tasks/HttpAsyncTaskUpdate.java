package com.cyber.kinoost.api.tasks;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cyber.kinoost.api.*;

import java.io.IOException;
import java.util.List;

import com.cyber.kinoost.api.models.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;

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
	
    @Override
    protected String doInBackground(String... params) {
    	ApiHelper.POST(params[0], jsonCreate());
        return ApiHelper.GET(params[0] + params[1]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Log.d("onPostExecute", result);
        
		FilmRepository filmRepo = new FilmRepository(context);
		MusicRepository musicRepo = new MusicRepository(context);
		PerformerRepository performerRepo = new PerformerRepository(context);
      
        JsonFactory f = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
			JsonParser jp = f.createParser(result);
			jp.nextToken();
			JsonUpdateList jsonUpdateList = mapper.readValue(jp, JsonUpdateList.class);
			List<JsonUpdate> listUpdate = jsonUpdateList.getUpdates();
			Log.d("HttpAsyncTask.onPostExecute", listUpdate.toString());
			Date updDate = new Date();
			
			for (JsonUpdate jsonUpdate: listUpdate)
				updDate = jsonUpdate.persist(context);
			
			editor.putLong(APP_PREFERENCES_UPDATE_DATE, updDate.getTime());
			editor.commit();
		} catch (JsonParseException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		}
        
		try {
			List<Film> film = filmRepo.findFilmByName("", 0, 10);
			Log.d("kinoost-filmRepo-findFilmByName", film.toString());

			List<Music> music = musicRepo.findMusicByName("", 0, 10);
			Log.d("kinoost-musicRepo-findMusicByName", music.toString());
			
			List<Music> musicrus = musicRepo.findMusicByName("Песня охотника", 0, 10);
			Log.d("kinoost-musicRepo-findMusicByNameRus", musicrus.toString());
			
			List<Music> musics1 = musicRepo.findMusicByFullName("", "Tangerine Dream", 0, 10);
			Log.d("kinoost-musicRepo-findMusicByFullName1", musics1.toString());
			
			List<Music> musics2 = musicRepo.findMusicByFullName("Annie & Father", "", 0, 10);
			Log.d("kinoost-musicRepo-findMusicByFullName2", musics2.toString());
			
			List<Music> musics3 = musicRepo.findMusicByFullName("Annie & Father", "Tangerine Dream", 0, 10);
			Log.d("kinoost-musicRepo-findMusicByFullName3", musics3.toString());

			List<Performer> performer = performerRepo.findPerformerByName("",0, 10);
			Log.d("kinoost-performerRepo-findPeromerByName",performer.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.d("repoFail:", e.getMessage());
			e.printStackTrace();
		}
   }
    
    private String jsonCreate(){
		UserData json = new UserData();
		FavoritesRepository favoritesRepo = new FavoritesRepository(context);
		MusicRatingRepository musicRatingRepo = new MusicRatingRepository(context);
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