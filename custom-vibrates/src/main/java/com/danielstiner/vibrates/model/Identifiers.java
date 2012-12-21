package com.danielstiner.vibrates.model;

import java.util.Arrays;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.danielstiner.vibrates.model.internal.Database;
import com.danielstiner.vibrates.model.internal.IDatabase;
import com.danielstiner.vibrates.model.internal.Migration;
import com.danielstiner.vibrates.model.provider.IdentifierProvider;

public final class Identifiers implements BaseColumns {

	public static class Common {

		public static final String Name = null;

	}

	public Identifiers() {

	}

	public static final Uri CONTENT_URI = IdentifierProvider.CONTENT_URI;

	public static final String CONTENT_TYPE = IdentifierProvider.CONTENT_ITEM_TYPE;

	public static final String COL_ID = "_id";
	public static final String COL_KIND = "kind";
	public static final String COL_ENTITYID = "entity";
	public static final String COL_IDENTIFIER = "identifier";
	public static final String COL_CREATOR = "creator";

	/** Monotonically increasing */
	public static final int VERSION = Database.VERSION;

	public static final String TABLE = "lookups";

	static class DatabaseHelper implements IDatabase.IHelper {

		public void onCreate(SQLiteDatabase db) {
			// Create lookup table
			String lookup_sql = "CREATE TABLE " + TABLE + " ( " + COL_ID
					+ " INTEGER PRIMARY KEY, " + COL_IDENTIFIER
					+ " string KEY, " + COL_ENTITYID + " integer, " + COL_KIND
					+ " string, " + COL_CREATOR + " string " + ");";
			db.execSQL(lookup_sql);
		}

		@Override
		public List<Migration> getMigrations() {
			return Arrays.asList(new Migration[] { new Migration() {
				@Override
				protected int version() {
					return 19;
				}

				@Override
				protected void up(SQLiteDatabase db) {
					db.execSQL("ALTER TABLE " + TABLE + " ADD COLUMN "
							+ COL_CREATOR + " string;");
				}
			} });
		}

	}
}
