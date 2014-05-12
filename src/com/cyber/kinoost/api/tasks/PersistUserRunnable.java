package com.cyber.kinoost.api.tasks;

import java.util.ArrayList;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.cyber.kinoost.api.*;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.db.repositories.*;

public class PersistUserRunnable implements Runnable {
	private Account account;

	public PersistUserRunnable(Account account) {
		super();
		this.account = account;
	}

	@SuppressLint("UseValueOf")
	@Override
	public void run() {
		if ((int) account.getUserId() <= 0)
			return;
		
		Collection<Long> userIdsList = new ArrayList<Long>();
		com.cyber.kinoost.api.vk.sources.User vkUser = null;
		ArrayList<com.cyber.kinoost.api.vk.sources.User> userList = null;
		
		Api api = new Api(account);
		
		userIdsList.add(new Long(account.getUserId()));

		try {
			userList = api.getProfiles(userIdsList, null, null, null, null, null);
			if(userList.size() > 0)
				vkUser = userList.get(0);
			
			if (vkUser != null) 
				account.setName(vkUser.last_name + " " + vkUser.first_name);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserRepository userRepo = new UserRepository(account.getContext());
		User user = userRepo.getUser();
		user.fill(account);
		userRepo.editUser(user);
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(account.getContext());
		Editor editor = prefs.edit();
		editor.putString("user_name", account.getName());
		editor.commit();
	}
};