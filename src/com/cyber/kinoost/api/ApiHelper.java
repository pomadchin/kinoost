package com.cyber.kinoost.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.api.tasks.*;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.fragments.KPlayerFragment;

public class ApiHelper {
	
	private final String MUSIC_CLASS_NAME = "com.cyber.kinoost.db.models.Music";

	public static void dbUpdate(Context updateContext, Date date) {

		if (isConnected(updateContext)) {
			new HttpAsyncTaskUpdate(updateContext).execute(Constants.UPDATE_PATH,
					Long.toString(date.getTime()));
		} else
			Log.d("dbUpdate", "connection failed");
	}

	public static void userEdit(Context updateContext, Date date) {
		// TODO ~ works like edit and create
	}
	
	public static void register(String json) {
		POST(Constants.REG_PATH, json);		
	}


	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	public static String POST(String url, String json) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			// set json to StringEntity
			StringEntity se = new StringEntity(json);

			// set httpPost Entity
			httpPost.setEntity(se);

			// set some headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public static boolean isConnected(Context updateContext) {
		ConnectivityManager connMgr = (ConnectivityManager) updateContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	// getSong via vk api
	public void getSongMusic(Context context, Api api, Music music) {
		if (music.getFileName() != null) {
			File fileMusic = new File(music.getFileName());
			if (fileMusic.exists()) {
				startPlayerFragment(context, music);
				return;
			}
		}

		getSong(api, music.getName(), music, context);
	}

	public void getSong(Api api, String request, Music music, Context context) {
		new HttpAsyncTaskVkSong(api, request, music, context, this).execute(request);
	}
	
	public void startPlayerFragment(Context context, Music music) {
		KinoostActivity activity = (KinoostActivity) context;
		Fragment newFragment = new KPlayerFragment(); 
		Bundle bundle = new Bundle();
		bundle.putSerializable(MUSIC_CLASS_NAME, music);
		newFragment.setArguments(bundle);

		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack("player fragment");
        transaction.commit();
	}

}