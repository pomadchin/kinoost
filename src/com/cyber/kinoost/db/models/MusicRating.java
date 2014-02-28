package com.cyber.kinoost.db.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "music_rating")
public class MusicRating {
	
	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String MUSIC_ID_FIELD_NAME = "music_id";
	
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
	private User user;
	@DatabaseField(foreign = true, columnName = MUSIC_ID_FIELD_NAME)
	private Music music;
	@DatabaseField
	Date date;
	@DatabaseField(index = true)
	int value;
	
	public MusicRating() {
	}

	public MusicRating(User user, Music music, int value) {
		super();
		this.user = user;
		this.music = music;
		this.value = value;
		this.date = new Date(System.currentTimeMillis());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "MusicRating [id=" + id + ", user=" + user + ", music=" + music
				+ ", date=" + date + ", value=" + value + "]";
	}
}
