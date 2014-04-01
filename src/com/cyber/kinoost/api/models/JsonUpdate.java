package com.cyber.kinoost.api.models;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.cyber.kinoost.db.models.Favorites;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.FilmMusic;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;

public class JsonUpdate {
    public static enum Method {ADD, DELETE, REPLACE}
    private Method method;
    private List<Favorites> favorites;
    private List<Film> films;
    private List<Music> music;
    private List<FilmMusic> filmMusic;
    private List<Performer> performers;
    private Date updateDate;


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Favorites> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorites> favorites) {
        this.favorites = favorites;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    public List<Music> getMusic() {
        return music;
    }

    public void setMusic(List<Music> music) {
        this.music = music;
    }

    public List<FilmMusic> getFilmMusic() {
        return filmMusic;
    }

    public void setFilmMusic(List<FilmMusic> filmMusic) {
        this.filmMusic = filmMusic;
    }

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public Date persist(Context context) {
    	FilmMusicRepository filmMusicRepo =  new FilmMusicRepository(context);
		FavoritesRepository favoritesRepo = new FavoritesRepository(context);
		
		if((getMethod() == Method.ADD) || (getMethod() == Method.REPLACE)) {
			filmMusicRepo.editFilmMusicListCascade(getFilmMusic());
		    favoritesRepo.editFavoritesList(getFavorites());
		} else if(getMethod() == Method.DELETE) {
			filmMusicRepo.deleteFilmMusicListCascade(getFilmMusic());
		}
		
		return getUpdateDate();
    }

	@Override
	public String toString() {
		return "JsonUpdate [method=" + method + ", favorites=" + favorites
				+ ", films=" + films + ", music=" + music + ", filmMusic="
				+ filmMusic + ", performers=" + performers + ", updateDate="
				+ updateDate + "]";
	}
}