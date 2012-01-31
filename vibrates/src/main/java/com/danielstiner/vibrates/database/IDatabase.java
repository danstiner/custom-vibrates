package com.danielstiner.vibrates.database;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabase {

	public abstract SQLiteDatabase getReadableDatabase();

	public abstract SQLiteDatabase getWritableDatabase();

}