package com.cyber.kinoost.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Environment;

import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.api.vk.sources.Audio;
import com.cyber.kinoost.api.vk.sources.KException;

public class DownloadMethod {

	public String getSong(Api api, String request) throws IOException,
			JSONException, KException {
		ArrayList<Audio> songsList = api.searchAudio(request, "2", "0",
				(long) 1, (long) 0, null, null);
		return downloadFile(songsList.get(0).url, request);
	}

	private String downloadFile(String url, final String name) {

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

					file = new File(Environment.getDownloadCacheDirectory(),
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

		return Environment.getDownloadCacheDirectory().toString() + "/" + name
				+ ".mp3";
	}

}
