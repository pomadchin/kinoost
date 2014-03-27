package com.cyber.kinoost.db.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "music_rating")
public class MusicRating {
	
	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String MUSIC_ID_FIELD_NAME = "music_id";
	public final static String DATE_TIME = "date_time";
	
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
	private User user;
	@DatabaseField(foreign = true, columnName = MUSIC_ID_FIELD_NAME)
	private Music music;
	@DatabaseField(columnName = DATE_TIME, dataType = DataType.DATE)
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
	
	public void setUserJackson(JsonNode jsonNode) throws JsonProcessingException {
		user = new ObjectMapper().treeToValue(jsonNode, User.class);
	}
	
	public void setMusicJackson(JsonNode jsonNode) throws JsonProcessingException {
		music = new ObjectMapper().treeToValue(jsonNode, Music.class);
	}

	@Override
	public String toString() {
		return "MusicRating [id=" + id + ", user=" + user + ", music=" + music
				+ ", date=" + date + ", value=" + value + "]";
	}
}
