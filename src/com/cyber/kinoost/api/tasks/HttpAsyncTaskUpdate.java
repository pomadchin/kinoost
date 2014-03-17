package com.cyber.kinoost.api.tasks;

import java.io.IOException;

import com.cyber.kinoost.api.models.JsonUpdate;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cyber.kinoost.api.*;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;
import com.cyber.kinoost.db.repositories.FilmRepository;
import com.cyber.kinoost.db.repositories.MusicRepository;
import com.cyber.kinoost.db.repositories.PerformerRepository;

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
			JsonUpdate jsonUpdate = mapper.readValue(jp, JsonUpdate.class);
			Log.d("HttpAsyncTask.onPostExecute", jsonUpdate.toString());
			
			FilmRepository filmRepo = new FilmRepository(getContext());
			filmRepo.createFilmList(jsonUpdate.getFilms());
			
			MusicRepository musicRepo = new MusicRepository(getContext());
			musicRepo.createMusicList(jsonUpdate.getMusic());
			
			FavoritesRepository favoritesRepo = new FavoritesRepository(getContext());
			favoritesRepo.createFavoritesList(jsonUpdate.getFavorites());
						
			PerformerRepository performerRepo = new PerformerRepository(getContext());
			performerRepo.createPerformerList(jsonUpdate.getPerformers());
			
			FilmMusicRepository filmmusicRepo =  new FilmMusicRepository(getContext());
			filmmusicRepo.createFilmMusicList(jsonUpdate.getFilmMusic());
			
			// TODO: persist data Diman
		} catch (JsonParseException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		}
   }
}