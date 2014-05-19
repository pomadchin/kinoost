package com.cyber.kinoost.db;

import java.io.IOException;
import java.sql.SQLException;

import com.cyber.kinoost.db.models.Favorites;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.FilmMusic;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.MusicRating;
import com.cyber.kinoost.db.models.Performer;
import com.cyber.kinoost.db.models.User;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	
	private static final Class<?>[] classes = new Class[]{
		Favorites.class, Film.class, 
		FilmMusic.class, Music.class, MusicRating.class,
		Performer.class, User.class 
	};
	
	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt", classes);
	}
}
