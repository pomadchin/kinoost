package com.cyber.kinoost.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "film_music")
public class FilmMusic {
	
	public final static String FILM_ID_FIELD_NAME = "film_id";
	public final static String MUSIC_ID_FIELD_NAME = "music_id";
	
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(foreign = true, columnName = FILM_ID_FIELD_NAME)
	private Film film;
	@DatabaseField(foreign = true, columnName = MUSIC_ID_FIELD_NAME)
	private Music music;
	
	public FilmMusic() {
	}

	public FilmMusic(Film film, Music music) {
		super();
		this.film = film;
		this.music = music;
	}

	public int getId() {
		return id;
	}
	
	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	@Override
	public String toString() {
		return "FilmMusic [id=" + id + ", film=" + film + ", music=" + music
				+ "]";
	}
}
