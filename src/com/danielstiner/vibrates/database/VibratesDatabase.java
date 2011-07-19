package com.danielstiner.vibrates.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import roboguice.inject.ContextScoped;

@ContextScoped
public class VibratesDatabase implements IDatabase {

	static final int VERSION = 5;

	// Need a context to create the database connection on
	@Inject private Activity activity;
	
	private IDatabaseHelper[] database_helpers;
	private SQLiteOpenHelper _dbHelper;
	
	@Inject
	public VibratesDatabase(
		@Named(DatabaseModule.DATABASE_NAME_KEY) String database_name,
		IDatabaseHelper[] database_helpers)
	{
		this.database_helpers = database_helpers;
		// Always use the latest helper's version
		int version = 0;
		for(IDatabaseHelper helper : database_helpers) {
			version = Math.max(version, helper.version());
		}
		_dbHelper = new DBHelper(activity, database_name, null, version);
	}
	
	
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for(IDatabaseHelper helper : database_helpers) {
				helper.onCreate(db);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for(IDatabaseHelper helper : database_helpers) {
				helper.onUpgrade(db, oldVersion, newVersion);
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.danielstiner.vibrates.database.IDatabase#getReadableDatabase()
	 */
	public SQLiteDatabase getReadableDatabase() {
		return _dbHelper.getReadableDatabase();
	}

	/* (non-Javadoc)
	 * @see com.danielstiner.vibrates.database.IDatabase#getWritableDatabase()
	 */
	public SQLiteDatabase getWritableDatabase() {
		return _dbHelper.getWritableDatabase();
	}
    
}
