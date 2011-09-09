package com.danielstiner.vibrates.database;

import java.io.InputStream;

import android.database.Cursor;
import android.net.Uri;

import com.danielstiner.vibrates.Entity;

public interface IManager {

	/**
	 * 
	 * @param contactUri
	 * @return
	 */
	public abstract Entity createFromContactUri(Uri contactUri);

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public abstract Entity getEntity(String identifier);
	
	/**
	 * 
	 * @return
	 */
	public abstract Cursor getEntities();

	/**
	 * 
	 * @return
	 */
	public abstract long[] getPattern(Entity entity, String type);

	/**
	 * 
	 * @param entity
	 * @param longArrayExtra
	 */
	public abstract void setPattern(Entity entity, long[] pattern);

	/**
	 * 
	 * @param entity
	 */
	public abstract void update(Entity entity);

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public abstract String getDisplayName(Entity entity);

	/**
	 * Gives a photo for an entity if at all possible, otherwise returns null
	 * @param entity Entity instance to grab a photostream for
	 * @return Stream that can be read out to get the contacts photo, or null if they have none
	 */
	public abstract InputStream getPhotoStream(Entity entity);

	public abstract String getKind(Entity entity);

	public abstract void remove(Entity fromCursor);
}
