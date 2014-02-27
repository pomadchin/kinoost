package com.cyber.kinoost.db;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import com.cyber.kinoost.db.models.*;

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
