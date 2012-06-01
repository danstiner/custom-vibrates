package com.danielstiner.vibrates.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// @ContextScoped
public class Database implements IDatabase {

	static final int VERSION = 7;
	
	private IDatabaseHelper[] database_helpers;
	private SQLiteOpenHelper _dbHelper;
	
	@Inject
	public Database(
		Context activity,
		@Named(DatabaseModule.DATABASE_NAME_KEY) String database_name,
		IDatabaseHelper[] database_helpers
		)
	{
		this.database_helpers = database_helpers;
		_dbHelper = new DBHelper(activity, database_name, null, VERSION);
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
		// TODO, return writable on first get to let the database be setup
		return _dbHelper.getWritableDatabase();
	}

	/* (non-Javadoc)
	 * @see com.danielstiner.vibrates.database.IDatabase#getWritableDatabase()
	 */
	public SQLiteDatabase getWritableDatabase() {
		return _dbHelper.getWritableDatabase();
	}
    
}