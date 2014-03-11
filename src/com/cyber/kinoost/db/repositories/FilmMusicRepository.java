package com.cyber.kinoost.db.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import java.sql.SQLException;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.*;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class FilmMusicRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<Film, Integer> filmDao = null;
	private RuntimeExceptionDao<Music, Integer> musicDao = null;
	private RuntimeExceptionDao<FilmMusic, Integer> filmMusicDao = null;
	
	private PreparedQuery<Film> filmsForMusicQuery = null;
	private PreparedQuery<Music> musicForFilmQuery = null;
	
	public FilmMusicRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		filmDao = dbHelper.getFilmRuntimeExceptionDao();
		filmMusicDao = dbHelper.getFilmMusicRuntimeExceptionDao();
		musicDao = dbHelper.getMusicRuntimeExceptionDao();
	}
	
	public FilmMusicRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		filmDao = dbHelper.getFilmRuntimeExceptionDao();
		filmMusicDao = dbHelper.getFilmMusicRuntimeExceptionDao();
		musicDao = dbHelper.getMusicRuntimeExceptionDao();
	}
	
	public void createFilm(Film film) {
		filmDao.create(film);
	}
	
	public void createFilmList(final List<Film> films) {
        filmDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Film film : films) {
	                filmDao.create(film);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteFilm(Film film) {
		filmDao.delete(film);
	}
	
	public void deleteFilmList(List<Film> films) {
		filmDao.delete(films);
	}
	
	public void createMusic(Music music) {
		musicDao.create(music);
	}
	
	public void createMusicList(final List<Music> music) {
        musicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Music song : music) {
	                musicDao.create(song);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteMusic(Music music) {
		musicDao.delete(music);
	}
	
	public void deleteMusicList(List<Music> music) {
		musicDao.delete(music);
	}
	
	public void createFilmMusic(FilmMusic filmMusic) {
		filmMusicDao.create(filmMusic);
	}
	
	public void createFilmMusicList(final List<FilmMusic> filmMusic) {
        filmMusicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (FilmMusic fm : filmMusic) {
	                filmMusicDao.create(fm);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteFilmMusic(FilmMusic filmMusic) {
		filmMusicDao.delete(filmMusic);
	}
	
	public void deleteFilmMusicList(List<FilmMusic> filmMusic) {
		filmMusicDao.delete(filmMusic);
	}
	
	public List<Film> findFilmByName(String name, int offset, int limit) throws SQLException {
	    List<Film> filmList = new ArrayList<Film>();
	    QueryBuilder<Film, Integer> queryBuilder = filmDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    if(name.length() > 0) queryBuilder.where().like("name", "%"+name+"%");
	    filmList = filmDao.query(queryBuilder.prepare());

	    return filmList;
	}
	
	public List<Music> findMusicByName(String name, int offset, int limit) throws SQLException {
	    List<Music> musicList = new ArrayList<Music>();
	    QueryBuilder<Music, Integer> queryBuilder = musicDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    if(name.length() > 0) queryBuilder.where().like("name", "%"+name+"%");
	    musicList = musicDao.query(queryBuilder.prepare());

	    return musicList;
	}

	public List<Film> lookupFilmsForMusic(Music music) throws SQLException {
		if (filmsForMusicQuery == null) {
			filmsForMusicQuery = makeFilmsForMusicQuery();
		}
		
		filmsForMusicQuery.setArgumentHolderValue(0, music);
		return filmDao.query(filmsForMusicQuery);
	}

	public List<Music> lookupMusicForFilm(Film film) throws SQLException {
		if (musicForFilmQuery == null) {
			musicForFilmQuery = makeMusicForFilmQuery();
		}
		musicForFilmQuery.setArgumentHolderValue(0, film);
		return musicDao.query(musicForFilmQuery);
	}

	private PreparedQuery<Film> makeFilmsForMusicQuery() throws SQLException {
		// build our inner query for FilmMusic objects
		QueryBuilder<FilmMusic, Integer> filmMusicQb = filmMusicDao.queryBuilder();
		// just select the film-id field
		filmMusicQb.selectColumns(FilmMusic.FILM_ID_FIELD_NAME);
		SelectArg musicSelectArg = new SelectArg();
		filmMusicQb.where().eq(FilmMusic.MUSIC_ID_FIELD_NAME, musicSelectArg);

		// build our outer query
		QueryBuilder<Film, Integer> filmQb = filmDao.queryBuilder();
		// where the id matches in the film-id from the inner query
		filmQb.where().in(Film.ID_FIELD_NAME, filmMusicQb);
		return filmQb.prepare();
	}

	private PreparedQuery<Music> makeMusicForFilmQuery() throws SQLException {
		QueryBuilder<FilmMusic, Integer> filmMusicQb = filmMusicDao.queryBuilder();
		// this time selecting for the music-id field
		filmMusicQb.selectColumns(FilmMusic.MUSIC_ID_FIELD_NAME);
		SelectArg filmSelectArg = new SelectArg();
		filmMusicQb.where().eq(FilmMusic.FILM_ID_FIELD_NAME, filmSelectArg);

		// build our outer query
		QueryBuilder<Music, Integer> musicQb = musicDao.queryBuilder();
		// where the music-id matches the inner query's music-id field
		musicQb.where().in(Film.ID_FIELD_NAME, filmMusicQb);
		return musicQb.prepare();
	}
}
