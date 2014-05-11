package com.cyber.kinoost.db;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.cyber.kinoost.*;
import com.cyber.kinoost.db.models.*;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "kinoost.db";
	private static final int DATABASE_VERSION = 1;

	private static String DATABASE_PATH = "";
	private Context context;

	private Dao<Favorites, Integer> favoritesDao = null;
	private RuntimeExceptionDao<Favorites, Integer> favoritesRuntimeDao = null;

	private Dao<Film, Integer> filmDao = null;
	private RuntimeExceptionDao<Film, Integer> filmRuntimeDao = null;

	private Dao<FilmMusic, Integer> filmMusicDao = null;
	private RuntimeExceptionDao<FilmMusic, Integer> filmMusicRuntimeDao = null;

	private Dao<Music, Integer> musicDao = null;
	private RuntimeExceptionDao<Music, Integer> musicRuntimeDao = null;

	private Dao<MusicRating, Integer> musicRatingDao = null;
	private RuntimeExceptionDao<MusicRating, Integer> musicRatingRuntimeDao = null;

	private Dao<Performer, Integer> performerDao = null;
	private RuntimeExceptionDao<Performer, Integer> performerRuntimeDao = null;

	private Dao<User, Integer> userDao = null;
	private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;

	public DatabaseHelper(Context context) {
		//super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		if (android.os.Build.VERSION.SDK_INT >= 17)
			DATABASE_PATH = context.getApplicationInfo().dataDir
					+ "/databases/";
		else
			DATABASE_PATH = "/data/data/" + context.getPackageName()
					+ "/databases/";

		this.context = context;
		
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {

		/*try {
			TableUtils.createTable(connectionSource, Favorites.class);
			TableUtils.createTable(connectionSource, Film.class);
			TableUtils.createTable(connectionSource, FilmMusic.class);
			TableUtils.createTable(connectionSource, Music.class);
			TableUtils.createTable(connectionSource, MusicRating.class);
			TableUtils.createTable(connectionSource, Performer.class);
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		/*try {
			TableUtils.dropTable(connectionSource, Favorites.class, true);
			TableUtils.dropTable(connectionSource, Film.class, true);
			TableUtils.dropTable(connectionSource, FilmMusic.class, true);
			TableUtils.dropTable(connectionSource, Music.class, true);
			TableUtils.dropTable(connectionSource, MusicRating.class, true);
			TableUtils.dropTable(connectionSource, Performer.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}

	public Dao<Favorites, Integer> getFavoritesDao() throws SQLException {
		if (favoritesDao == null) {
			favoritesDao = getDao(Favorites.class);
		}
		return favoritesDao;
	}

	public RuntimeExceptionDao<Favorites, Integer> getFavoritesRuntimeExceptionDao() {
		if (favoritesRuntimeDao == null) {
			favoritesRuntimeDao = getRuntimeExceptionDao(Favorites.class);
		}
		return favoritesRuntimeDao;
	}

	public Dao<Film, Integer> getFilmDao() throws SQLException {
		if (filmDao == null) {
			filmDao = getDao(Film.class);
		}
		return filmDao;
	}

	public RuntimeExceptionDao<Film, Integer> getFilmRuntimeExceptionDao() {
		if (filmRuntimeDao == null) {
			filmRuntimeDao = getRuntimeExceptionDao(Film.class);
		}
		return filmRuntimeDao;
	}

	public Dao<FilmMusic, Integer> getFilmMusicDao() throws SQLException {
		if (filmMusicDao == null) {
			filmMusicDao = getDao(FilmMusic.class);
		}
		return filmMusicDao;
	}

	public RuntimeExceptionDao<FilmMusic, Integer> getFilmMusicRuntimeExceptionDao() {
		if (filmMusicRuntimeDao == null) {
			filmMusicRuntimeDao = getRuntimeExceptionDao(FilmMusic.class);
		}
		return filmMusicRuntimeDao;
	}

	public Dao<Music, Integer> getMusicDao() throws SQLException {
		if (musicDao == null) {
			musicDao = getDao(Music.class);
		}
		return musicDao;
	}

	public RuntimeExceptionDao<Music, Integer> getMusicRuntimeExceptionDao() {
		if (musicRuntimeDao == null) {
			musicRuntimeDao = getRuntimeExceptionDao(Music.class);
		}
		return musicRuntimeDao;
	}

	public Dao<MusicRating, Integer> getMusicRatingDao() throws SQLException {
		if (musicRatingDao == null) {
			musicRatingDao = getDao(MusicRating.class);
		}
		return musicRatingDao;
	}

	public RuntimeExceptionDao<MusicRating, Integer> getMusicRatingRuntimeExceptionDao() {
		if (musicRatingRuntimeDao == null) {
			musicRatingRuntimeDao = getRuntimeExceptionDao(MusicRating.class);
		}
		return musicRatingRuntimeDao;
	}

	public Dao<Performer, Integer> getPerformerDao() throws SQLException {
		if (performerDao == null) {
			performerDao = getDao(Performer.class);
		}
		return performerDao;
	}

	public RuntimeExceptionDao<Performer, Integer> getPerformerRuntimeExceptionDao() {
		if (performerRuntimeDao == null) {
			performerRuntimeDao = getRuntimeExceptionDao(Performer.class);
		}
		return performerRuntimeDao;
	}

	public Dao<User, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(User.class);
		}
		return userDao;
	}

	public RuntimeExceptionDao<User, Integer> getUserRuntimeExceptionDao() {
		if (userRuntimeDao == null) {
			userRuntimeDao = getRuntimeExceptionDao(User.class);
		}
		return userRuntimeDao;
	}
	
	public void createDataBase() throws IOException {
		// if database not exists copy it from the assets
		if (!checkDataBase()) {
			this.getReadableDatabase();
			this.close();
			try {
				copyDataBase();
				Log.e("cr", "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	private boolean checkDataBase() {
		File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {
		InputStream mInput = context.getResources().openRawResource(R.raw.kinoost);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[10240];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0)
			mOutput.write(mBuffer, 0, mLength);
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}
	
	// db dump to sd card
	public void dbDump() {
		File f = new File(DATABASE_PATH + DATABASE_NAME);
		if (f.exists()) {
			FileInputStream fis = null;
			FileOutputStream fos = null;

			try {
				fis = new FileInputStream(f);
				fos = new FileOutputStream("/mnt/sdcard/" + DATABASE_NAME);
				while (true) {
					int i = fis.read();
					if (i != -1) {
						fos.write(i);
					} else {
						break;
					}
				}
				fos.flush();
				Toast.makeText(context, "DB dump OK", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, "DB dump ERROR", Toast.LENGTH_LONG)
						.show();
			} finally {
				try {
					fos.close();
					fis.close();
				} catch (IOException ioe) {
				}
			}
		}
	}
}
