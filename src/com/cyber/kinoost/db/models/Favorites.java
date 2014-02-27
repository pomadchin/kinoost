package com.cyber.kinoost.db.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "favorites")
public class Favorites {
	
	public final static String ID_FIELD_NAME = "id";
	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String MUSIC_ID_FIELD_NAME = "music_id";
	
	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	int id;
	@DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
	private User user;
	@DatabaseField(foreign = true, columnName = MUSIC_ID_FIELD_NAME)
	private Music music;
	@DatabaseField
	Date date;
	
	public Favorites() {
	}

	public Favorites(int id, User user, Music music) {
		super();
		this.id = id;
		this.user = user;
		this.music = music;
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

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Favorites [id=" + id + ", user=" + user + ", music=" + music
				+ ", date=" + date + "]";
	}
}
