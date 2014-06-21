package com.cyber.kinoost.db.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.Favorites;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class FavoritesRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<Favorites, Integer> favoritesDao = null;
	private MusicRepository musicRepo = null;
	
	public FavoritesRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		favoritesDao = dbHelper.getFavoritesRuntimeExceptionDao();
		musicRepo = new MusicRepository(context);
	}
	
	public FavoritesRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		favoritesDao = dbHelper.getFavoritesRuntimeExceptionDao();
		musicRepo = new MusicRepository(dbHelper.getContext());
	}
	
	public void createFavorites(Favorites favorites) {
		if(favorites == null) return;
		
		favoritesDao.create(favorites);
	}
	
	public void createFavorites(User user, Music music) {
		if(user == null || music == null) return;
		Favorites favorites = new Favorites(user, music);
		
		favoritesDao.create(favorites);
	}
	
	public void createFavoritesList(final List<Favorites> favorites) {
		if(favorites == null) return;
		
        favoritesDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Favorites favorite : favorites) {
	                favoritesDao.create(favorite);
	            }
	            return null;
	        }
	    });
	}
	
	public void editFavorites(Favorites favorites) {
		if(favorites == null) return;
		
		favoritesDao.createOrUpdate(favorites);
	}
	
	public void editFavoritesList(final List<Favorites> favorites) {
		if(favorites == null) return;
		
        favoritesDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Favorites favorite : favorites) {
	                favoritesDao.createOrUpdate(favorite);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteFavorites(Favorites favorites) {
		if(favorites == null) return;
		
		favoritesDao.delete(favorites);
	}
	
	public void deleteFavorites(Music music) {
		if (music == null) return;
		List<Favorites> favoritesList = new ArrayList<Favorites>();

		try {
			favoritesList = getFavorites(music);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (favoritesList.size() > 0)
			deleteFavoritesList(favoritesList);
	}
	
	public void deleteFavoritesList(List<Favorites> favorites) {
		if(favorites == null) return;
		
		favoritesDao.delete(favorites);
	}
	
	public List<Favorites> getFavorites(int offset, int limit) throws SQLException {
	    List<Favorites> favoritesList = new ArrayList<Favorites>();
	    QueryBuilder<Favorites, Integer> queryBuilder = favoritesDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    favoritesList = favoritesDao.query(queryBuilder.prepare());

	    return favoritesList;
	}
	
	public boolean isFavorite(Music music) throws SQLException {
		QueryBuilder<Favorites, Integer> queryBuilder = favoritesDao.queryBuilder();
		queryBuilder.where().eq(Favorites.MUSIC_ID_FIELD_NAME, music);
		List<Favorites> favoritesList = favoritesDao.query(queryBuilder.prepare());
		
		return favoritesList.size() == 0 ? false : true;
	}
	
	public List<String> getFavoritesIds(List<Music> music) throws SQLException {
		List<String> musicIdsList = new ArrayList<String>();
		
		QueryBuilder<Favorites, Integer> queryBuilder = favoritesDao.queryBuilder();
		queryBuilder.where().in(Favorites.MUSIC_ID_FIELD_NAME, music);
		List<Favorites> favoritesList = favoritesDao.query(queryBuilder.prepare());
		
		for(Favorites fv : favoritesList)
			musicIdsList.add(Integer.toString(fv.getMusic().getId()));
		
		return musicIdsList;
	}
	
	public List<Favorites> getFavorites(List<Music> music) throws SQLException {		
		QueryBuilder<Favorites, Integer> queryBuilder = favoritesDao.queryBuilder();
		queryBuilder.where().in(Favorites.MUSIC_ID_FIELD_NAME, music);
		return favoritesDao.query(queryBuilder.prepare());
	}
	
	public List<Favorites> getFavorites(Music music) throws SQLException {		
		QueryBuilder<Favorites, Integer> queryBuilder = favoritesDao.queryBuilder();
		queryBuilder.where().eq(Favorites.MUSIC_ID_FIELD_NAME, music);
		return favoritesDao.query(queryBuilder.prepare());
	}
	
	public List<Music> getFavoriteMusic(int offset, int limit) throws SQLException {
		List<Favorites> favoritesList = getFavorites(offset, limit);
		List<Integer> musicIdsList = new ArrayList<Integer>();
		
		for(Favorites favorite : favoritesList)
			musicIdsList.add(favorite.getMusic().getId());
		
		return musicRepo.findMusicByIds(musicIdsList, 0, 0);
	}
}
