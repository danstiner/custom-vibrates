package com.danielstiner.vibrates.model;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.LoaderManager;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.model.internal.ISearchCallback;

public interface IDataModel {

	Entity entityFromCursor(Cursor c);
	
	/**
	 * Gives a photo for an entity if at all possible, otherwise returns null
	 * 
	 * @param entity
	 *            Entity instance to grab a photostream for
	 * @return Stream that can be read out to get the contacts photo, or null if
	 *         they have none
	 */
	Drawable getPicture(Entity e);

	void searchEntities(LoaderManager loaderManager,
			IEntityFilter entityFilter, ISearchCallback searchCallback);

	Entity createFromContactUri(Uri contactpath);

	Uri getContactUri(Entity e);

	Entity getEntity(String identifier, Kind kindGroup);

	Pattern getPattern(Entity e);

	void remove(Entity entity);

//	/**
//	 * 
//	 * @param contactUri
//	 * @return
//	 */
//	Entity createFromContactUri(Uri contactUri);
//
//	/**
//	 * 
//	 * @param identifier
//	 * @return
//	 */
//	Entity getEntity(String identifier);
//
//	/**
//	 * 
//	 * @return
//	 */
//	Cursor getEntities();
//
//	/**
//	 * 
//	 * @return
//	 */
//	long[] getPattern(Entity entity, String type);
//
//	/**
//	 * 
//	 * @param entity
//	 * @param longArrayExtra
//	 */
//	void setPattern(Entity entity, long[] pattern);
//
//	/**
//	 * 
//	 * @param entity
//	 */
//	void update(Entity entity);
//
//	/**
//	 * 
//	 * @param entity
//	 * @return
//	 */
//	String getDisplayName(Entity entity);
//
//	Entity.Kind getKind(Entity entity);
//
//	void remove(Entity entity);
//
//	Intent getViewIntent(Entity entity);
//
//	Entity createFromGroupId(String groupid);
//
//	Entity getEntity(String identifier, Entity.Kind kind);
//
//	Uri getContactUri(Entity entity);
//
//	Pattern getPattern(Entity e);

}
