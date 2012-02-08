package com.danielstiner.vibrates.model.internal;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.danielstiner.vibrates.model.StorageModule;
import com.google.inject.Inject;

@ContextSingleton
public class Database implements IDatabase {

	private static final String DATABASE_NAME = "vibrates";

	private static final int VERSION = StorageModule.DATABASE_VERSION;

	private IHelper[] database_helpers;
	private SQLiteOpenHelper _dbHelper;

	@Inject
	public Database(Context activity, IHelper[] database_helpers) {
		this.database_helpers = database_helpers;
		_dbHelper = new DBHelper(activity, DATABASE_NAME, null, VERSION);
	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for (IHelper helper : database_helpers) {
				helper.onCreate(db);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (IHelper helper : database_helpers) {
				helper.onUpgrade(db, oldVersion, newVersion);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.danielstiner.vibrates.database.IDatabase#getReadableDatabase()
	 */
	public SQLiteDatabase getReadableDatabase() {
		// TODO, return writable on first get to let the database be setup
		return _dbHelper.getWritableDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.danielstiner.vibrates.database.IDatabase#getWritableDatabase()
	 */
	public SQLiteDatabase getWritableDatabase() {
		return _dbHelper.getWritableDatabase();
	}

}
