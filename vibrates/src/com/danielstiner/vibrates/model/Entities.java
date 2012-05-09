package com.danielstiner.vibrates.model;

import java.util.Arrays;
import java.util.List;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.model.internal.Database;
import com.danielstiner.vibrates.model.internal.EntityProvider;
import com.danielstiner.vibrates.model.internal.IDatabase;
import com.danielstiner.vibrates.model.internal.Migration;

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
	public static final String CREATOR = "creator";

	public static final int VERSION = Database.VERSION;

	public static final String TABLE = "entities";

	public static Uri getEntityUri(long entity_id) {
		return ContentUris.withAppendedId(CONTENT_URI, entity_id);
	}

	public static Uri getEntityUri(Entity entity) {
		return ContentUris.withAppendedId(CONTENT_URI, entity.entityid());
	}

	static class DatabaseHelper implements IDatabase.IHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create entity table
			String entity_sql = "CREATE TABLE " + TABLE + " ( " + ENTITY_ID
					+ " INTEGER PRIMARY KEY, " + KIND + " string, " + NAME
					+ " string, " + PATTERN + " string, " + NOTIFY_COUNT
					+ " integer " + ");";
			db.execSQL(entity_sql);
		}

		@Override
		public List<Migration> getMigrations() {
			return Arrays.asList(new Migration[] { new Migration() {
				@Override
				protected int version() {
					return 20;
				}

				@Override
				protected void up(SQLiteDatabase db) {
					db.execSQL("ALTER TABLE " + TABLE + " ADD COLUMN "
							+ CREATOR + " string;");
				}
			} });
		}

	}
}
