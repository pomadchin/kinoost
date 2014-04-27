package com.cyber.kinoost.api.tasks;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;

import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.api.vk.sources.Audio;
import com.cyber.kinoost.api.vk.sources.KException;

import android.os.AsyncTask;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.db.repositories.*;
import com.cyber.kinoost.views.KPlayer;

public class HttpAsyncTaskVkSong extends AsyncTask<String, Integer, String> {
	
	private Context context;
	private Api api;
	private String request;
	private Music music;
	private KPlayer kPlayer;
	private ProgressDialog progressDialog;
	
	public HttpAsyncTaskVkSong(Api api, String request, Music music,
			Context context, KPlayer kPlayer) {
		super();
		this.context = context;
		this.api = api;
		this.request = request;
		this.music = music;
		this.kPlayer = kPlayer;
		this.progressDialog = new ProgressDialog(context);
	}
	
	public Context getContext() {
		return this.context;
	}

	public Api getApi() {
		return api;
	}

	public String getRequest() {
		return request;
	}

	public Music getMusic() {
		return music;
	}

	public KPlayer getkPlayer() {
		return kPlayer;
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Скачивание саундтрека...");
		progressDialog.setCancelable(true);
		progressDialog.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
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
			e.printStackTrace();
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

			buffer = new byte[8192];
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
		kPlayer.play(result);
	}
}