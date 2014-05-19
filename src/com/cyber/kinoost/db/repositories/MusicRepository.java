package com.cyber.kinoost.db.repositories;

import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.Music;

public class MusicRepository extends FilmMusicRepository {
	
	public MusicRepository(Context context) {
		super(context);
	}
	
	public MusicRepository(DatabaseHelper dbHelper) {
		super(dbHelper);
	}
	
	public void createMusic(Music music) {
		if(music == null) return;
		
		musicDao.create(music);
	}
	
	public void createMusicList(final List<Music> music) {
		if(music == null) return;
		
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
	
	public void editMusic(Music music) {
		if(music == null) return;
		
		musicDao.createOrUpdate(music);
	}
	
	public void editMusicList(final List<Music> music) {
		if(music == null) return;
		
        musicDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Music song : music) {
	                musicDao.createOrUpdate(song);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteMusic(Music music) {
		if(music == null) return;
		
		musicDao.delete(music);
	}
	
	public void deleteMusicList(List<Music> music) {
		if(music == null) return;
		
		musicDao.delete(music);
	}
}
