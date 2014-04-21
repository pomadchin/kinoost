package com.cyber.kinoost.db.repositories;

import java.util.ArrayList;
import java.util.Iterator;
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
	protected DatabaseHelper dbHelper;
	
	protected RuntimeExceptionDao<Film, Integer> filmDao = null;
	protected RuntimeExceptionDao<Music, Integer> musicDao = null;
	protected RuntimeExceptionDao<Performer, Integer> performerDao = null;
	protected RuntimeExceptionDao<FilmMusic, Integer> filmMusicDao = null;
	
	protected PerformerRepository performerRepo = null;
	
	protected PreparedQuery<Film> filmsForMusicQuery = null;
	protected PreparedQuery<Music> musicForFilmQuery = null;
	
	public FilmMusicRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		filmDao = dbHelper.getFilmRuntimeExceptionDao();
		filmMusicDao = dbHelper.getFilmMusicRuntimeExceptionDao();
		musicDao = dbHelper.getMusicRuntimeExceptionDao();
		performerDao = dbHelper.getPerformerRuntimeExceptionDao();
		performerRepo = new PerformerRepository(context);
	}
	
	public FilmMusicRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		filmDao = dbHelper.getFilmRuntimeExceptionDao();
		filmMusicDao = dbHelper.getFilmMusicRuntimeExceptionDao();
		musicDao = dbHelper.getMusicRuntimeExceptionDao();
		performerRepo = new PerformerRepository(dbHelper);
	}
	
	public void createFilmMusic(FilmMusic filmMusic) {
		if(filmMusic == null) return;
		
		filmMusicDao.create(filmMusic);
	}
	
	public void createFilmMusicList(final List<FilmMusic> filmMusic) {
		if(filmMusic == null) return;
		
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
	
	public void createFilmMusicListCascade(final List<FilmMusic> filmMusic) {
		if(filmMusic == null) return;
		
        filmMusicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (FilmMusic fm : filmMusic) {
	            	filmDao.create(fm.getFilm());
	            	performerDao.create(fm.getMusic().getPerformer());
	            	musicDao.create(fm.getMusic());
	                filmMusicDao.create(fm);
	            }
	            return null;
	        }
	    });
	}
	
	public void editFilmMusic(FilmMusic filmMusic) {
		if(filmMusic == null) return;
		
		filmMusicDao.createOrUpdate(filmMusic);
	}
	
	public void editFilmMusicList(final List<FilmMusic> filmMusic) {
		if(filmMusic == null) return;
		
        filmMusicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (FilmMusic fm : filmMusic) {
	                filmMusicDao.createOrUpdate(fm);
	            }
	            return null;
	        }
	    });
	}
	
	public void editFilmMusicListCascade(final List<FilmMusic> filmMusic) {
		if(filmMusic == null) return;
		
        filmMusicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (FilmMusic fm : filmMusic) {
	            	filmDao.createOrUpdate(fm.getFilm());
	            	performerDao.createOrUpdate(fm.getMusic().getPerformer());
	            	musicDao.createOrUpdate(fm.getMusic());
	                filmMusicDao.createOrUpdate(fm);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteFilmMusic(FilmMusic filmMusic) {
		if(filmMusic == null) return;
		
		filmMusicDao.delete(filmMusic);
	}
	
	public void deleteFilmMusicList(List<FilmMusic> filmMusic) {
		if(filmMusic == null) return;
		
		filmMusicDao.delete(filmMusic);
	}
	
	public void deleteFilmMusicListCascade(final List<FilmMusic> filmMusic) {
		if(filmMusic == null) return;
		
        filmMusicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (FilmMusic fm : filmMusic) {
	                filmMusicDao.delete(fm);
	                musicDao.delete(fm.getMusic());
	                performerDao.delete(fm.getMusic().getPerformer());
	                filmDao.delete(fm.getFilm());
	            }
	            return null;
	        }
	    });
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
	
	public ArrayList<Tuple<Film, Film>> findTuplesFilmByName(String name, int offset, int limit) throws SQLException {
		List<Film> filmList = findFilmByName(name, offset, limit);
		ArrayList<Tuple<Film, Film>> result = new ArrayList<Tuple<Film, Film>>();
		
		Iterator<Film> iterator = filmList.iterator();
		while (iterator.hasNext()) {
			Film fst = iterator.next();
			Film snd = null;
			
			if(iterator.hasNext())
				snd = iterator.next();
			
			result.add(new Tuple<Film, Film>(fst, snd));
		}
		
		return result;
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
	
	public List<Music> findMusicByPerformer(String name, int offset, int limit) throws SQLException {
		Performer performer = null;
		List<Performer> performerList = performerRepo.findPerformerByName(name, 0, 1);
		if(performerList.size() > 0) performer = performerList.get(0);
	    List<Music> musicList = new ArrayList<Music>();
	    if(performer != null){
	    	QueryBuilder<Music, Integer> queryBuilder = musicDao.queryBuilder();
	    	if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    	if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    	queryBuilder.where().eq(Music.PERFORMER_ID_FIELD_NAME, performer);
	    	musicList = musicDao.query(queryBuilder.prepare());
	    }	    
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

	protected PreparedQuery<Film> makeFilmsForMusicQuery() throws SQLException {
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

	protected PreparedQuery<Music> makeMusicForFilmQuery() throws SQLException {
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
