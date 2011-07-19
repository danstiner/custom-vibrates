package com.danielstiner.vibrates.database;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabaseHelper {
	
	public void onCreate(SQLiteDatabase db);

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

	public int version();

}
