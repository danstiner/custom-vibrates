package com.danielstiner.vibrates.model.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.inject.Inject;

@ContextSingleton
public class Database implements IDatabase {

	private static final String DATABASE_NAME = "vibrates";

	/** Monotonically increasing
	 * Used as version for both EntityStore and IdentifierStore
	 */
	public static final int VERSION = 21;

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
			List<Migration> migrations = new ArrayList<Migration>();
			
			// Get all possible migrations
			for (IHelper helper : mTableHelpers) {
				migrations.addAll(helper.getMigrations());
			}
			
			// Sort them to go from version 0 up
			Collections.sort(migrations, new Comparator<Migration>() {

				@Override
				public int compare(Migration lhs, Migration rhs) {
					return new Integer(lhs.version()).compareTo(rhs.version());
				}
			});
			
			// Apply them all, they will know whether they are applicable
			for(Migration m : migrations)
			{
				m.apply(db, oldVersion, newVersion);
			}
		}
		
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			List<Migration> migrations = new ArrayList<Migration>();
			
			// Get all possible migrations
			for (IHelper helper : mTableHelpers) {
				migrations.addAll(helper.getMigrations());
			}
			
			// Sort them to go down to version 0
			Collections.sort(migrations, new Comparator<Migration>() {

				@Override
				public int compare(Migration lhs, Migration rhs) {
					return -1 * new Integer(lhs.version()).compareTo(rhs.version());
				}
			});
			
			// Apply them all, they will know whether they are applicable
			for(Migration m : migrations)
			{
				m.apply(db, oldVersion, newVersion);
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
