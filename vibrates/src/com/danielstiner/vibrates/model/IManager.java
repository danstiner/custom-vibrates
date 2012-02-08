package com.danielstiner.vibrates.model;

import java.io.InputStream;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.widget.CursorAdapter;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.model.internal.ISearchCallback;

public interface IManager {

	/**
	 * 
	 * @param contactUri
	 * @return
	 */
	Entity createFromContactUri(Uri contactUri);

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	Entity getEntity(String identifier);

	/**
	 * 
	 * @return
	 */
	Cursor getEntities();

	/**
	 * 
	 * @return
	 */
	long[] getPattern(Entity entity, String type);

	/**
	 * 
	 * @param entity
	 * @param longArrayExtra
	 */
	void setPattern(Entity entity, long[] pattern);

	/**
	 * 
	 * @param entity
	 */
	void update(Entity entity);

	/**
	 * 
	 * @param entity
	 * @return
	 */
	String getDisplayName(Entity entity);

	/**
	 * Gives a photo for an entity if at all possible, otherwise returns null
	 * 
	 * @param entity
	 *            Entity instance to grab a photostream for
	 * @return Stream that can be read out to get the contacts photo, or null if
	 *         they have none
	 */
	InputStream getPhotoStream(Entity entity);

	Entity.Kind getKind(Entity entity);

	void remove(Entity entity);

	Intent getViewIntent(Entity entity);

	Entity getEntity(Cursor c);

	Cursor getEntities(String type);

	Entity createFromGroupId(String groupid);

	Entity getEntity(String identifier, Entity.Kind kind);

	Uri getContactUri(Entity entity);

	Pattern getPattern(Entity e);

	void searchEntities(int loaderId, LoaderManager loaderManager, IEntityFilter mEntityFilter, ISearchCallback callback);
}
