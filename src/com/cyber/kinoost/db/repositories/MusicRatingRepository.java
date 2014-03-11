package com.cyber.kinoost.db.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import java.sql.SQLException;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.*;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class MusicRatingRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<MusicRating, Integer> musicRatingDao = null;
	
	public MusicRatingRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		musicRatingDao = dbHelper.getMusicRatingRuntimeExceptionDao();
	}
	
	public MusicRatingRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		musicRatingDao = dbHelper.getMusicRatingRuntimeExceptionDao();
	}
	
	public void createMusicRating(MusicRating musicRating) {
		musicRatingDao.create(musicRating);
	}
	
	public void createMusicRatingList(final List<MusicRating> musicRating) {
        musicRatingDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (MusicRating mr : musicRating) {
	                musicRatingDao.create(mr);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteMusicRating(MusicRating musicRating) {
		musicRatingDao.delete(musicRating);
	}
	
	public void deleteMusicRatingList(List<MusicRating> musicRating) {
		musicRatingDao.delete(musicRating);
	}
	
	public List<MusicRating> getMusicRating(int offset, int limit) throws SQLException {
	    List<MusicRating> musicRatingList = new ArrayList<MusicRating>();
	    QueryBuilder<MusicRating, Integer> queryBuilder = musicRatingDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    musicRatingList = musicRatingDao.query(queryBuilder.prepare());

	    return musicRatingList;
	}
}
