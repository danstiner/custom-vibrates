package com.danielstiner.vibrates.database;

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

}
