package com.cyber.kinoost.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseAssetsHelper extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "kinoost.db";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseAssetsHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	}
}
