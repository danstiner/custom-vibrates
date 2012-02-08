package com.danielstiner.vibrates.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.danielstiner.vibrates.model.internal.IDatabase;
import com.danielstiner.vibrates.model.internal.IdentifierProvider;

public final class Identifiers implements BaseColumns {

	public Identifiers() {

	}

	public static final Uri CONTENT_URI = IdentifierProvider.CONTENT_URI;

	public static final String CONTENT_TYPE = IdentifierProvider.CONTENT_ITEM_TYPE;

	public static final String ENTITY_ID = "_id";

	public static final String KIND = "kind";
	public static final String NAME = "name";
	public static final String PATTERN = "pattern";
	public static final String NOTIFY_COUNT = "notified";

	/** Monotonically increasing */
	public static final int VERSION = 7;

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
