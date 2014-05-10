package com.cyber.kinoost.api.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.api.vk.sources.Audio;
import com.cyber.kinoost.api.vk.sources.KException;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.MusicRepository;
import com.cyber.kinoost.fragments.*;

public class HttpAsyncTaskVkSong extends AsyncTask<String, Integer, String> {
	
	private Context context;
	private Api api;
	private String request;
	private Music music;
	private ProgressDialog progressDialog;
	private Boolean login;
	private ApiHelper apiHelper;
	
	public HttpAsyncTaskVkSong(Api api, String request, Music music,
			Context context, ApiHelper apiHelper) {
		super();
		this.context = context;
		this.api = api;
		this.request = request;
		this.music = music;
		this.progressDialog = new ProgressDialog(context);
		this.login = true;
		this.apiHelper = apiHelper;
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Скачивание саундтрека...");
		progressDialog.setCancelable(true);
		progressDialog.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
	}
	
	private void startLoginFragment() {
		KinoostActivity activity = (KinoostActivity) context;
		Fragment newFragment = new LoginFragment();
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content_frame, newFragment);
		transaction.commit();

	}

	@Override
	protected String doInBackground(String... params) {
		try {
			ArrayList<Audio> songsList = api.searchAudio(params[0], "2", "0", (long) 1, (long) 0, null, null);

			String fileName = "";

			fileName = downloadFile(songsList.get(0).url);

			MusicRepository musicRepository = new MusicRepository(context);
			music.setFileName(fileName);

			musicRepository.editMusic(music);

			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (KException e) {
			Log.i(this.getClass().getName(),e.getMessage());
			if(e.getMessage().contains("authorization failed")) {
				login = false;
				startLoginFragment();
			}
			
			//e.printStackTrace();
		}

		return "";

	}

	private String downloadFile(String uri) {

		URL url;
		HttpURLConnection urlConnection;
		InputStream inputStream;
		byte[] buffer;
		int downloadedSize;
		int totalSize;
		int bufferLength;

		File file = null;
		FileOutputStream fos = null;

		try {
			url = new URL(uri);
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();

			file = new File(context.getCacheDir(), request + ".mp3");
			file.createNewFile();
			fos = new FileOutputStream(file);

			inputStream = urlConnection.getInputStream();

			totalSize = urlConnection.getContentLength();
			downloadedSize = 0;

			buffer = new byte[10240];
			bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fos.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				publishProgress(downloadedSize, totalSize);
			}

			fos.close();
			inputStream.close();

			return context.getCacheDir().toString() + "/" + request
					+ ".mp3";
		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	protected void onProgressUpdate(Integer... values) {
		progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
	};

	@Override
	protected void onPostExecute(String result) {
		progressDialog.hide();
		if (login) apiHelper.startPlayerFragment(context, music);
	}
}