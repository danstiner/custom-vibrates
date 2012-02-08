package com.danielstiner.vibrates.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.danielstiner.vibrates.model.internal.EntityProvider;
import com.danielstiner.vibrates.model.internal.IDatabase;

public final class Entities implements BaseColumns {

	public Entities() {

	}

	public static final Uri CONTENT_URI = EntityProvider.CONTENT_URI;

	public static final String CONTENT_TYPE = EntityProvider.CONTENT_ITEM_TYPE;

	public static final String ENTITY_ID = "_id";

	public static final String KIND = "kind";
	public static final String NAME = "name";
	public static final String PATTERN = "pattern";
	public static final String NOTIFY_COUNT = "notified";

	/** Monotonically increasing */
	public static final int VERSION = 11;

	public static final String TABLE = "entities";

	static class DatabaseHelper implements IDatabase.IHelper {

		public void onCreate(SQLiteDatabase db) {
			// Create entity table
			String entity_sql = "CREATE TABLE " + TABLE + " ( " + ENTITY_ID
					+ " INTEGER PRIMARY KEY, " + KIND + " string, " + NAME
					+ " string, " + PATTERN + " string, " + NOTIFY_COUNT
					+ " integer " + ");";
			db.execSQL(entity_sql);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		public int version() {
			return VERSION;
		}

	}
}
