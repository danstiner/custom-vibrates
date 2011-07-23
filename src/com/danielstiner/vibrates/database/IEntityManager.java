package com.danielstiner.vibrates.database;

import java.io.InputStream;

import android.database.Cursor;
import android.net.Uri;

import com.danielstiner.vibrates.Entity;

public interface IEntityManager {

	public abstract Entity get(Long id);
	
	public abstract Cursor getAll();
	
	public abstract Entity fromCursor(Cursor c);
	
	/**
	 * 
	 * @param name
	 * @param pattern
	 * @param type
	 * @return
	 */
	public abstract Entity create(String name, long[] pattern, String type);
	
	public abstract void update(Entity entity);
	
	public abstract void remove(Entity entity);
	
	public abstract String getDisplayName(Entity entity);
	
	/**
	 * Gives a photo for an entity if at all possible, otherwise returns null
	 * @param entity Entity instance to grab a photostream for
	 * @return Stream that can be read out to get the contacts photo, or null if they have none
	 */
	public abstract InputStream getPhotoStream(Entity entity);
}
