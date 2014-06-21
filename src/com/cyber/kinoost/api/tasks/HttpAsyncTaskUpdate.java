package com.cyber.kinoost.api.tasks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.models.JsonUpdate;
import com.cyber.kinoost.api.models.JsonUpdateList;
import com.cyber.kinoost.api.models.UserData;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.MusicRatingRepository;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        JsonFactory f = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
			JsonParser jp = f.createParser(result);
			jp.nextToken();
			JsonUpdateList jsonUpdateList = mapper.readValue(jp, JsonUpdateList.class);
			List<JsonUpdate> listUpdate = jsonUpdateList.getUpdates();
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
