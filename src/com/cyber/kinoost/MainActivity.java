package com.cyber.kinoost;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.Menu;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.api.*;

public class MainActivity extends Activity {

	public static final String APP_PREFERENCES = "com.cyber.kinoost";
	public static final String APP_PREFERENCES_UPDATE_DATETIME = "com.cyber.kinoost.update.datetime";
	public static final long APP_PREFERENCES_DAYS_UPDATE = 1;
	
	DatabaseHelper dbHelper;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	DatabaseHelper dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setClass(this, KinoostActivity.class);
		this.startActivity(intent);
		finish();		
		setContentView(R.layout.activity_main);
		
		// init preferences && editor
		prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = prefs.edit();
		//doDBDataStuff(); //-- uncomment to see in logs db queries examples.
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		// check data update on start
		Date storedDate = new Date(prefs.getLong(APP_PREFERENCES_UPDATE_DATETIME, 0));
		Date newDate = new Date();
		long diffDays = (newDate.getTime() - storedDate.getTime()) / (24 * 60 * 60 * 1000);
		
		if(diffDays >= APP_PREFERENCES_DAYS_UPDATE) {
			editor.putLong(APP_PREFERENCES_UPDATE_DATETIME, newDate.getTime());
			editor.commit();
			ApiHelper.dbUpdate(getBaseContext(), newDate);
		}
	}
	// test func remove from prod
	private void doDBDataStuff() {
		dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

		RuntimeExceptionDao<Favorites, Integer> favoritesDao = dbHelper.getFavoritesRuntimeExceptionDao();
		RuntimeExceptionDao<Film, Integer> filmDao = dbHelper.getFilmRuntimeExceptionDao();
		RuntimeExceptionDao<FilmMusic, Integer> filmMusicDao = dbHelper.getFilmMusicRuntimeExceptionDao();
		RuntimeExceptionDao<Music, Integer> musicDao = dbHelper.getMusicRuntimeExceptionDao();
		RuntimeExceptionDao<MusicRating, Integer> musicRatingDao = dbHelper.getMusicRatingRuntimeExceptionDao();
		RuntimeExceptionDao<Performer, Integer> performerDao = dbHelper.getPerformerRuntimeExceptionDao();
		RuntimeExceptionDao<User, Integer> userDao = dbHelper.getUserRuntimeExceptionDao();
		
		// create
		User user1 = new User(1, "name1");
		User user2 = new User(2, "name2");
		User user3 = new User(3, "name3");
		User user4 = new User(4, "name4");
		
		Performer performer1 = new Performer(1, "performer1");
		Performer performer2 = new Performer(2, "performer2");
		Performer performer3 = new Performer(3, "performer3");
		Performer performer4 = new Performer(4, "performer4");
		
		Film film1 = new Film(1, "film1", 1, 1, 1.0);
		Film film2 = new Film(2, "film2", 2, 2, 2.0);
		Film film3 = new Film(3, "film3", 3, 3, 3.0);
		Film film4 = new Film(4, "film4", 4, 4, 4.0);
		
		Music music1 = new Music(1, "music1", 1.0, performer1);
		Music music2 = new Music(2, "music2", 2.0, performer2);
		Music music3 = new Music(3, "music3", 3.0, performer3);
		Music music4 = new Music(4, "music4", 4.0, performer4);
		
		FilmMusic filmMusic1 = new FilmMusic(film1, music1);
		FilmMusic filmMusic2 = new FilmMusic(film1, music2);
		FilmMusic filmMusic3 = new FilmMusic(film2, music4);
		FilmMusic filmMusic4 = new FilmMusic(film1, music3);
		
		// create
		userDao.create(user1);
		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);
		
		performerDao.create(performer1);
		performerDao.create(performer2);
		performerDao.create(performer3);
		performerDao.create(performer4);
		
		filmDao.create(film1);
		filmDao.create(film2);
		filmDao.create(film3);
		filmDao.create(film4);
		
		musicDao.create(music1);
		musicDao.create(music2);
		musicDao.create(music3);
		musicDao.create(music4);
		
		filmMusicDao.create(filmMusic1);
		filmMusicDao.create(filmMusic2);
		filmMusicDao.create(filmMusic3);
		filmMusicDao.create(filmMusic4);
		
		// stuff
		List<User> users = userDao.queryForAll();
		Log.d("kinoost-users", users.toString());
		
		List<FilmMusic> filmMusics = filmMusicDao.queryForAll();
		Log.d("kinoost-filmMusics", filmMusics.toString());
		
		OpenHelperManager.releaseHelper();
	}
}
