package com.danielstiner.vibrates.model.internal;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabase {

	SQLiteDatabase getReadableDatabase();

	SQLiteDatabase getWritableDatabase();

	public interface IHelper {

		void onCreate(SQLiteDatabase db);
		
		List<Migration> getMigrations();
	}

}