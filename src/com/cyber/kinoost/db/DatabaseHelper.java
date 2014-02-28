package com.cyber.kinoost.db;

import android.content.Context;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cyber.kinoost.*;
import com.cyber.kinoost.db.models.*;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "kinoost.db";
	private static final int DATABASE_VERSION = 1;
	
	private Dao<Favorites, Integer> favoritesDao = null;
	private RuntimeExceptionDao<Favorites, Integer> favoritesRuntimeDao = null;
	
	private Dao<Film, Integer> filmDao = null;
	private RuntimeExceptionDao<Film, Integer> filmRuntimeDao = null;
	
	private Dao<FilmMusic, Integer> filmMusicDao = null;
	private RuntimeExceptionDao<FilmMusic, Integer> filmMusicRuntimeDao = null;
	
	private Dao<Music, Integer> musicDao = null;
	private RuntimeExceptionDao<Music, Integer> musicRuntimeDao = null;
	
	private Dao<MusicRating, Integer> musicRatingDao = null;
	private RuntimeExceptionDao<MusicRating, Integer> musicRatingRuntimeDao = null;
	
	private Dao<Performer, Integer> performerDao = null;
	private RuntimeExceptionDao<Performer, Integer> performerRuntimeDao = null;
	
	private Dao<User, Integer> userDao = null;
	private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		
		try{
			TableUtils.createTable(connectionSource, Favorites.class);
			TableUtils.createTable(connectionSource, Film.class);
			TableUtils.createTable(connectionSource, FilmMusic.class);
			TableUtils.createTable(connectionSource, Music.class);
			TableUtils.createTable(connectionSource, MusicRating.class);
			TableUtils.createTable(connectionSource, Performer.class);
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Favorites.class, true);
			TableUtils.dropTable(connectionSource, Film.class, true);
			TableUtils.dropTable(connectionSource, FilmMusic.class, true);
			TableUtils.dropTable(connectionSource, Music.class, true);
			TableUtils.dropTable(connectionSource, MusicRating.class, true);
			TableUtils.dropTable(connectionSource, Performer.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Dao<Favorites, Integer> getFavoritesDao() throws SQLException {
		if(favoritesDao == null) {
			favoritesDao = getDao(Favorites.class);
		}
		return favoritesDao;
	}
	
	public RuntimeExceptionDao<Favorites, Integer> getFavoritesRuntimeExceptionDao() {
		if(favoritesRuntimeDao == null) {
			favoritesRuntimeDao = getRuntimeExceptionDao(Favorites.class);
		}
		return favoritesRuntimeDao;
	}
	
	public Dao<Film, Integer> getFilmDao() throws SQLException {
		if(filmDao == null) {
			filmDao = getDao(Film.class);
		}
		return filmDao;
	}
	
	public RuntimeExceptionDao<Film, Integer> getFilmRuntimeExceptionDao() {
		if(filmRuntimeDao == null) {
			filmRuntimeDao = getRuntimeExceptionDao(Film.class);
		}
		return filmRuntimeDao;
	}
	
	public Dao<FilmMusic, Integer> getFilmMusicDao() throws SQLException {
		if(filmMusicDao == null) {
			filmMusicDao = getDao(FilmMusic.class);
		}
		return filmMusicDao;
	}
	
	public RuntimeExceptionDao<FilmMusic, Integer> getFilmMusicRuntimeExceptionDao() {
		if(filmMusicRuntimeDao == null) {
			filmMusicRuntimeDao = getRuntimeExceptionDao(FilmMusic.class);
		}
		return filmMusicRuntimeDao;
	}
	
	public Dao<Music, Integer> getMusicDao() throws SQLException {
		if(musicDao == null) {
			musicDao = getDao(Music.class);
		}
		return musicDao;
	}
	
	public RuntimeExceptionDao<Music, Integer> getMusicRuntimeExceptionDao() {
		if(musicRuntimeDao == null) {
			musicRuntimeDao = getRuntimeExceptionDao(Music.class);
		}
		return musicRuntimeDao;
	}
	
	public Dao<MusicRating, Integer> getMusicRatingDao() throws SQLException {
		if(musicRatingDao == null) {
			musicRatingDao = getDao(MusicRating.class);
		}
		return musicRatingDao;
	}
	
	public RuntimeExceptionDao<MusicRating, Integer> getMusicRatingRuntimeExceptionDao() {
		if(musicRatingRuntimeDao == null) {
			musicRatingRuntimeDao = getRuntimeExceptionDao(MusicRating.class);
		}
		return musicRatingRuntimeDao;
	}
	
	public Dao<Performer, Integer> getPerformerDao() throws SQLException {
		if(performerDao == null) {
			performerDao = getDao(Performer.class);
		}
		return performerDao;
	}
	
	public RuntimeExceptionDao<Performer, Integer> getPerformerRuntimeExceptionDao() {
		if(performerRuntimeDao == null) {
			performerRuntimeDao = getRuntimeExceptionDao(Performer.class);
		}
		return performerRuntimeDao;
	}
	
	public Dao<User, Integer> getUserDao() throws SQLException {
		if(userDao == null) {
			userDao = getDao(User.class);
		}
		return userDao;
	}
	
	public RuntimeExceptionDao<User, Integer> getUserRuntimeExceptionDao() {
		if(userRuntimeDao == null) {
			userRuntimeDao = getRuntimeExceptionDao(User.class);
		}
		return userRuntimeDao;
	}
}