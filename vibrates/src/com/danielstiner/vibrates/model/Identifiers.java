package com.danielstiner.vibrates.model;

import roboguice.content.RoboContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.danielstiner.vibrates.model.internal.Database;
import com.danielstiner.vibrates.model.internal.EntityStore;
import com.danielstiner.vibrates.model.internal.IDatabase;
import com.google.inject.Inject;

public final class Identifiers extends DataModel {

	public Identifiers() {

	}

	public static final Uri CONTENT_URI = Provider.CONTENT_URI;

	public static final String CONTENT_TYPE = Provider.CONTENT_ITEM_TYPE;

	public static final String ENTITY_ID = "_id";

	public static final String KIND = "kind";
	public static final String NAME = "name";
	public static final String PATTERN = "pattern";
	public static final String NOTIFY_COUNT = "notified";
	
	/** Monotonically increasing */
	public static final int VERSION = 11;

	public static final String TABLE = "entities";

	public static class Provider extends RoboContentProvider {

		private static final String AUTHORITY = "com.danielstiner.vibrates.providers.Identifier";

		private static final String ENTITIES_BASE_PATH = "entities";

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + ENTITIES_BASE_PATH);

		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/danielstiner-vibrate-entity";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/danielstiner-vibrate-entity";
		
		
		static class DatabaseHelper implements IDatabase.IHelper {

			public void onCreate(SQLiteDatabase db) {
				// Create entity table
				String entity_sql = "CREATE TABLE " + TABLE + " ( " + ENTITY_ID
						+ " INTEGER PRIMARY KEY, " + KIND + " string, "
						+ NAME + " string, " + PATTERN + " string, "
						+ NOTIFY_COUNT + " integer " + ");";
				db.execSQL(entity_sql);
			}

			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			}

			public int version() {
				return VERSION;
			}

		}
		

		@Inject
		private IDatabase mDatabase;

		@Override
		public boolean onCreate() {
			super.onCreate();
			// TODO
			return true;
		}

		@Override
		public int delete(Uri arg0, String arg1, String[] arg2) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getType(Uri arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Uri insert(Uri uri, ContentValues arg1) {
			if (CONTENT_URI.equals(uri)) {
				// mDatabase.getWritableDatabase().insert(table, nullColumnHack,
				// values)

			}

			return null;
		}

		@Override
		public Cursor query(Uri uri, String[] projection, String selection,
				String[] selectionArgs, String sortOrder) {

			if (CONTENT_URI.equals(uri)) {
				return mDatabase.getReadableDatabase().query(EntityStore.TABLE,
						projection, selection, selectionArgs, null, null, null);
				// groupBy,
				// having,
				// orderBy)
			}

			return null;
		}

		@Override
		public int update(Uri arg0, ContentValues arg1, String arg2,
				String[] arg3) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
