package com.cyber.kinoost;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.Menu;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.api.*;
import com.cyber.kinoost.db.repositories.*;
import com.cyber.kinoost.img.*;

public class MainActivity extends Activity {

	public static final String APP_PREFERENCES = "com.cyber.kinoost";
	public static final String APP_PREFERENCES_UPDATE_DATETIME = "com.cyber.kinoost.update.datetime";
	public static final String APP_PREFERENCES_UPDATE_DATE = "com.cyber.kinoost.update.date";
	public static final long APP_PREFERENCES_DAYS_UPDATE = 1;
	
	DatabaseHelper dbHelper;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// db init
		dbHelper = new DatabaseHelper(this);
		
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
		Date updDate = new Date(prefs.getLong(APP_PREFERENCES_UPDATE_DATE, 0));
		long diffDays = (newDate.getTime() - storedDate.getTime()) / (24 * 60 * 60 * 1000);
		
		if(diffDays >= APP_PREFERENCES_DAYS_UPDATE) {
			editor.putLong(APP_PREFERENCES_UPDATE_DATETIME, newDate.getTime());
			editor.commit();
			ApiHelper.dbUpdate(getBaseContext(), updDate);
		}
	}
	// test func remove from prod
	private void doDBDataStuff() {
		FavoritesRepository favoritesRepo = new FavoritesRepository(this);
		FilmRepository filmRepo = new FilmRepository(this);
		MusicRepository musicRepo = new MusicRepository(this);
		FilmMusicRepository filmMusicRepo = new FilmMusicRepository(this);
		MusicRatingRepository musicRatingRepo = new MusicRatingRepository(this);
		PerformerRepository performerRepo = new PerformerRepository(this);
		UserRepository userRepo = new UserRepository(this);
		
		// create
		List<User> userList = new ArrayList<User>(); 
		userList.add(new User(1, "name1"));
		userList.add(new User(2, "name2"));
		userList.add(new User(3, "name3"));
		userList.add(new User(4, "name4"));
		
		List<Performer> performerList = new ArrayList<Performer>();
		performerList.add(new Performer(1, "performer1"));
		performerList.add(new Performer(2, "performer2"));
		performerList.add(new Performer(3, "performer3"));
		performerList.add(new Performer(4, "performer4"));
		
		List<Film> filmList = new ArrayList<Film>();
		filmList.add(new Film(1, "film	we	qwe	qwe	we	w1", 1, "kp_822_200x200.jpg", 1.0));
		filmList.add(new Film(2, "film2", 2, "kp_822_200x200.jpg", 2.0));
		filmList.add(new Film(3, "film3", 3, "kp_822_200x200.jpg", 3.0));
		filmList.add(new Film(4, "film4", 4, "kp_822_200x200.jpg", 4.0));
		filmList.add(new Film(5, "film5", 5, "kp_822_200x200.jpg", 5.0));
		filmList.add(new Film(6, "film6", 6, "kp_822_200x200.jpg", 2.0));
		filmList.add(new Film(7, "film7", 7, "kp_822_200x200.jpg", 1.0));
		
		List<Music> musicList = new ArrayList<Music>();
		musicList.add(new Music(1, "music1", 1.0, performerList.get(0)));
		musicList.add(new Music(2, "music2", 2.0, performerList.get(1)));
		musicList.add(new Music(3, "music3", 3.0, performerList.get(2)));
		musicList.add(new Music(4, "music4", 4.0, performerList.get(3)));
		
		List<FilmMusic> filmMusicList = new ArrayList<FilmMusic>();
		filmMusicList.add(new FilmMusic(filmList.get(0), musicList.get(0)));
		filmMusicList.add(new FilmMusic(filmList.get(0), musicList.get(1)));
		filmMusicList.add(new FilmMusic(filmList.get(1), musicList.get(3)));
		filmMusicList.add(new FilmMusic(filmList.get(0), musicList.get(2)));
		
		List<MusicRating> musicRatingList = new ArrayList<MusicRating>();
		musicRatingList.add(new MusicRating(userList.get(0), musicList.get(0), 1));
		musicRatingList.add(new MusicRating(userList.get(0), musicList.get(1), 2));
		musicRatingList.add(new MusicRating(userList.get(0), musicList.get(2), 3));
		musicRatingList.add(new MusicRating(userList.get(0), musicList.get(3), 4));
		
		// create
		userRepo.editUserList(userList);
		performerRepo.editPerformerList(performerList);
		filmRepo.editFilmList(filmList);
		musicRepo.editMusicList(musicList);
		filmMusicRepo.editFilmMusicList(filmMusicList);
		musicRatingRepo.editMusicRatingList(musicRatingList);
		
		try{
		
		List<Film> film = filmRepo.findFilmByName("", 0, 10);
		Log.d("kinoost-filmRepo-findFilmByName", film.toString() );
		
		List<Music> music = musicRepo.findMusicByName("",0, 10);
		Log.d("kinoost-musicRepo-findMusicByName", music.toString());
		
		List<Performer> performer = performerRepo.findPerformerByName("", 0, 10);
		Log.d("kinoost-performerRepo-findPeromerByName", performer.toString());
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.d("repoFail:", e.getMessage());
			e.printStackTrace();
		}
		
		
		// stuff
		try {
			List<User> users = userRepo.getUserList();
			Log.d("kinoost-users", users.toString());
			
			List<Film> films = filmMusicRepo.findFilmByName("film", 0, 10);
			Log.d("kinoost-filmMusicRepo-findFilmByName:", films.toString());
			Log.d("kinoost-filmMusicRepo-lookupMusicForFilm:", filmMusicRepo.lookupMusicForFilm(films.get(0)).toString());
			
			List<MusicRating> musicRating = musicRatingRepo.getMusicRating(0, 0);
			Log.d("kinoost-musicRatingRepo-getMusicRating:", musicRating.toString());
			
			List<MusicRating> musicRatingDate = musicRatingRepo.getMusicRatingByDate(new Date(), 0, 0);
			Log.d("kinoost-musicRatingRepo-getMusicRatingDate:", musicRatingDate.toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.d("repoFail:", e.getMessage());
			e.printStackTrace();
		}
		
		//OpenHelperManager.releaseHelper();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
