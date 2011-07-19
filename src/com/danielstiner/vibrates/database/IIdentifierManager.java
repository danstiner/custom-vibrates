package com.danielstiner.vibrates.database;

import com.danielstiner.vibrates.Entity;

import android.database.Cursor;

public interface IIdentifierManager {

	//public abstract Cursor getAll();
	
	public abstract Cursor getOrphans();

	public abstract Cursor get(Entity owner);

	public abstract Entity get(String identifier);

	public abstract void add(Entity to, String identifier);
	
	//public abstract String identifierFromCursor(Cursor c);

//	public abstract String kindFromCursor(Cursor c);

	//public abstract Entity entityFromCursor(Cursor c);

}