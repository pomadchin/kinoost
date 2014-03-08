package com.cyber.kinoost.api.models;

import java.util.List;
import com.cyber.kinoost.db.models.*;

public class JsonUpdate {
	private List<Favorites> favorites;
    private List<Film> films;
    private List<Music> music;
    private List<FilmMusic> filmMusic;
    private List<Performer> performers;
	
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
	
	@Override
	public String toString() {
		return "JsonUpdate [favorites=" + favorites + ", films=" + films
				+ ", music=" + music + ", filmMusic=" + filmMusic
				+ ", performers=" + performers + "]";
	}
}