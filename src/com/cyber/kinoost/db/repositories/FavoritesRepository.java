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

public class FavoritesRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<Favorites, Integer> favoritesDao = null;
	
	public FavoritesRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		favoritesDao = dbHelper.getFavoritesRuntimeExceptionDao();
	}
	
	public FavoritesRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		favoritesDao = dbHelper.getFavoritesRuntimeExceptionDao();
	}
	
	public void createFavorites(Favorites favorites) {
		favoritesDao.create(favorites);
	}
	
	public void createFavoritesList(final List<Favorites> favorites) {
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
	
	public void deleteFavorites(Favorites favorites) {
		favoritesDao.delete(favorites);
	}
	
	public void deleteFavoritesList(List<Favorites> favorites) {
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
}
