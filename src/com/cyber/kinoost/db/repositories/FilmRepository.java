package com.cyber.kinoost.db.repositories;

import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.Film;

public class FilmRepository extends FilmMusicRepository {
	public FilmRepository(Context context) {
		super(context);
	}
	
	public FilmRepository(DatabaseHelper dbHelper) {
		super(dbHelper);
	}
	
	public void createFilm(Film film) {
		filmDao.create(film);
	}
	
	public void createFilmList(final List<Film> films) {
		if(films == null) return;
		
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
	
	public void editFilm(Film film) {
		if(film == null) return;
		
		filmDao.createOrUpdate(film);
	}
	
	public void editFilmList(final List<Film> films) {
		if(films == null) return;
		
        filmDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Film film : films) {
	                filmDao.createOrUpdate(film);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteFilm(Film film) {
		if(film == null) return;
		
		filmDao.delete(film);
	}
	
	public void deleteFilmList(List<Film> films) {
		if(films == null) return;
		
		filmDao.delete(films);
	}
}
