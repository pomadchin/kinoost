package com.cyber.kinoost.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.cyber.kinoost.api.tasks.*;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.api.vk.sources.Audio;
import com.cyber.kinoost.api.vk.sources.KException;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.MusicRepository;

public class ApiHelper {

	public static String update = "http://kinoserver-cybern223.rhcloud.com/kinoserver/mobile/update/";

	public static void dbUpdate(Context updateContext, Date date) {
		
		if (isConnected(updateContext)) {
			new HttpAsyncTaskUpdate(updateContext).execute(update,
					Long.toString(date.getTime()));
		} else
			Log.d("dbUpdate", "connection failed");
	}

	public static void userEdit(Context updateContext, Date date) {
		// TODO ~ works like edit and create
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
	public String getSoungMusic(Context context, Api api, Music music)
			throws IOException, JSONException, KException {
		if (music.getFileName() != null) {
			File fileMusic = new File(music.getFileName());
			if (fileMusic.exists())
				return music.getFileName();
		}

		getSong(api, music.getName(), music, context);

		MusicRepository musicRepository = new MusicRepository(context);
		List<Music> dbMusic;
		try {
			dbMusic = musicRepository.findMusicById(music.getId(), 0, 0);

			if (dbMusic.size() > 0)
				return dbMusic.get(0).getName();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
	
	public void getSong(final Api api, final String request, final Music music, final Context context) throws IOException,
			JSONException, KException {
		
		new AsyncTask<String, Integer, String>() {
			private Exception m_error = null;

			@Override
			protected String doInBackground(String... params) {
				try {
					ArrayList<Audio> songsList = api.searchAudio(params[0], "2", "0",
							(long) 1, (long) 0, null, null);
					
					return songsList.get(0).url;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return "";
				
			}
			
			@Override
		    protected void onPostExecute(String result) {
				Log.d("##########", result);
				
				String fileName = downloadFile(context, result, request);
				
				Log.d("#######----", fileName);
				
				MusicRepository musicRepository = new MusicRepository(context);
				music.setFileName(fileName);
				
				musicRepository.editMusic(music);
				
			}
		}.execute(request);
	}

	private String downloadFile(final Context context, String url, final String name) {

		new AsyncTask<String, Integer, File>() {
			private Exception m_error = null;

			@Override
			protected File doInBackground(String... params) {
				URL url;
				HttpURLConnection urlConnection;
				InputStream inputStream;
				byte[] buffer;
				int bufferLength;

				File file = null;
				FileOutputStream fos = null;

				try {
					url = new URL(params[0]);
					urlConnection = (HttpURLConnection) url.openConnection();

					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					urlConnection.connect();

					file = new File(context.getCacheDir(),
							name + ".mp3");
					file.createNewFile();
					fos = new FileOutputStream(file);

					inputStream = urlConnection.getInputStream();

					buffer = new byte[8192];
					bufferLength = 0;

					while ((bufferLength = inputStream.read(buffer)) > 0) {
						fos.write(buffer, 0, bufferLength);
					}

					fos.close();
					inputStream.close();

					return file;
				} catch (MalformedURLException e) {
					e.printStackTrace();
					m_error = e;
				} catch (IOException e) {
					e.printStackTrace();
					m_error = e;
				}

				return null;
			}

			@Override
			protected void onPostExecute(File file) {
				if (m_error != null) {
					m_error.printStackTrace();
					return;
				}

			}
		}.execute(url);

		return context.getCacheDir().toString() + "/" + name
				+ ".mp3";
	}
}