package com.cyber.kinoost.api;

import java.io.IOException;

import com.cyber.kinoost.api.models.JsonUpdate;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        return ApiHelper.GET(urls[0]);
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
			// TODO: persist data
		} catch (JsonParseException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			Log.d("HttpAsyncTask.onPostExecute", e.getMessage());
			//e.printStackTrace();
		}
   }
}