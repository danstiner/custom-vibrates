package com.danielstiner.vibrates.model.internal;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabase {

	SQLiteDatabase getReadableDatabase();

	SQLiteDatabase getWritableDatabase();

	public interface IHelper {

		void onCreate(SQLiteDatabase db);

		void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

		int version();
	}

}