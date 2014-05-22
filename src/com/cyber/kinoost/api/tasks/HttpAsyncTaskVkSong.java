package com.cyber.kinoost.api.tasks;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

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
import com.cyber.kinoost.fragments.LoginFragment;

public class HttpAsyncTaskVkSong extends AsyncTask<String, Integer, String> {
	
	private Context context;
	private Api api;
	private String request;
	private Music music;
	private Boolean login;
	private ApiHelper apiHelper;
	private String imgUrl;
	
	public HttpAsyncTaskVkSong(Api api, String request, Music music, String imgUrl,
			Context context, ApiHelper apiHelper) {
		super();
		this.context = context;
		this.api = api;
		this.request = request;
		this.music = music;
		this.login = true;
		this.apiHelper = apiHelper;
		this.imgUrl = imgUrl;
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

			MusicRepository musicRepository = new MusicRepository(context);
			music.setFileName(songsList.get(0).url);

			musicRepository.editMusic(music);

			return songsList.get(0).url;
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
		} catch (Exception e) {
			//e.printStackTrace();
		}

		return "";

	}

	@Override
	protected void onPostExecute(String result) {
		if (login) apiHelper.startPlayerFragment(context, music, imgUrl);
	}
}