package com.cyber.kinoost.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Account {
	private String access_token;
	private long user_id;
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
	}

	public void restore() {
		if(context == null) return;
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		access_token = prefs.getString("access_token", null);
		user_id = prefs.getLong("user_id", 0);
	}
	
	public String getAccessToken() {
		return this.access_token;
	}
	
	public long getUserId() {
		return this.user_id;
	}
}
