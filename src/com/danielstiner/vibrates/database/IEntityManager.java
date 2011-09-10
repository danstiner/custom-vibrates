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
	
	public abstract void remove(Entity entity);
	
	public abstract String getDisplayName(Entity entity);

	public abstract void setPattern(Entity entity, long[] pattern);

	public abstract long[] getPattern(Entity entity);

	public abstract String getKind(Entity entity);
}
