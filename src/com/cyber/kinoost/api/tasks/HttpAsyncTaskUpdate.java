package com.cyber.kinoost.api.tasks;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cyber.kinoost.api.*;

import java.io.IOException;

import com.cyber.kinoost.api.models.JsonUpdate;
import com.cyber.kinoost.api.models.JsonSend;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;

import com.cyber.kinoost.db.repositories.FilmRepository;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.MusicRatingRepository;

public class HttpAsyncTaskUpdate extends AsyncTask<String, Void, String> {
	private Context context;
	SharedPreferences prefs;
	public HttpAsyncTaskUpdate(Context context) {
		super();
		this.context = context;
	}
	
	private Context getContext() {
		return this.context;
	}
	public void SentToServer(){
		String URL = "https://www.microsoft.com"; //windows все проверяет так (:
		ApiHelper API = new ApiHelper();
		JsonSend json = new JsonSend();
		FavoritesRepository Favor = new FavoritesRepository(getContext());
		MusicRatingRepository MusRate = new MusicRatingRepository(getContext());
	    Date storedDate = new Date(prefs.getLong("com.cyber.kinoost.update.datetime", 0));
		try{
		json.setFavorites(Favor.getFavorites(0, 0));
		json.setMusicRating(MusRate.getMusicRatingByDate(storedDate, 0, 0));
		String SendString = json.toString();
		@SuppressWarnings("static-access")
		String Log = API.POST(URL, SendString);
		System.out.println(Log);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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