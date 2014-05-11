package com.cyber.kinoost.api;

import com.cyber.kinoost.api.tasks.PersistUserRunnable;
import com.cyber.kinoost.db.models.User;
import com.cyber.kinoost.db.repositories.UserRepository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Account {
	private String access_token;
	private long user_id;
	private String name;
	private Context context;
	
	public Account(Context context, String access_token, long user_id) {
		this.context = context;
		this.access_token = access_token;
		this.user_id = user_id;
	}
	
	public Account(Context context) {
		this.context = context;
		restore();
	}

	public void save() {
		if(context == null) return;
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString("access_token", access_token);
		editor.putLong("user_id", user_id);
		editor.commit();
		
		new Thread(new PersistUserRunnable(this)).start();
	}
	
	public void restore() {
		if(context == null) return;
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		access_token = prefs.getString("access_token", null);
		name = prefs.getString("user_name", null);
		user_id = prefs.getLong("user_id", 0);
		
		if(access_token == null || name == null || user_id == 0) {
			UserRepository userRepo = new UserRepository(context);
			User user = userRepo.getUser();
			access_token = user.getToken();
			user_id = (long) user.getId();
			name = user.getName();
			
			Editor editor = prefs.edit();
			editor.putString("access_token", access_token);
			editor.putLong("user_id", user_id);
			editor.commit();
		}
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAccessToken() {
		return this.access_token;
	}
	
	public long getUserId() {
		return this.user_id;
	}
	
	public Context getContext() {
		return this.context;
	}

	@Override
	public String toString() {
		return "Account [access_token=" + access_token + ", user_id=" + user_id
				+ ", name=" + name + ", context=" + context + "]";
	}
	
}