package com.danielstiner.vibrates.storage.internal;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabase {

	SQLiteDatabase getReadableDatabase();

	SQLiteDatabase getWritableDatabase();

	public interface IDatabaseHelper {

		void onCreate(SQLiteDatabase db);

		void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

		int version();
	}

}