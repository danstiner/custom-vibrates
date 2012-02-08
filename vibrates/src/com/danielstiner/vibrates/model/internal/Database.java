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

	private IHelper[] mTableHelpers;
	private SQLiteOpenHelper mSQLiteHelper;

	@Inject
	public Database(Context activity, IHelper[] database_helpers) {
		mTableHelpers = database_helpers;
		mSQLiteHelper = new DBHelper(activity, DATABASE_NAME, null, VERSION);
	}

	/**
	 * 
	 * @author stiner
	 * 
	 */
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for (IHelper helper : mTableHelpers) {
				helper.onCreate(db);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (IHelper helper : mTableHelpers) {
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
		return mSQLiteHelper.getWritableDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.danielstiner.vibrates.database.IDatabase#getWritableDatabase()
	 */
	public SQLiteDatabase getWritableDatabase() {
		return mSQLiteHelper.getWritableDatabase();
	}

}
