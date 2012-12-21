package com.danielstiner.vibrates.model.internal;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.model.IEntityFilter;

public interface IEntityStore {


	/**
	 * Creates a new entity and returns it
	 * @param name
	 * @param pattern
	 * @param kind
	 * @return
	 */
	Entity create(String name, Pattern pattern, Entity.Kind kind);
	
	/**
	 * Gets a particular entity by the id used internally
	 * @param id
	 * @return Found entity or null
	 */
	Entity get(Long id);
	
	/**
	 * Gets a particular entity by a special lookup uri
	 * @param uri
	 * @return
	 */
	Entity get(Uri uri);
	
	/**
	 * Commits changes made to entity
	 * 
	 * @param entity Entity from a get, create or search call to this store
	 * @return Whether update is successful
	 */
	boolean update(Entity entity);

	/**
	 * Removes an entity from the store
	 * @param entity
	 * @return Whether an entity was removed
	 */
	boolean delete(Entity entity);

	/**
	 * Performs a search using a filter and loader manager, calls a callback with the result
	 * @param loaderManager
	 * @param filter
	 * @param callback
	 */
	void search(LoaderManager loaderManager, IEntityFilter filter,
			ISearchCallback callback);

	/**
	 * Attempts to extract an entity from the current position in the cursor, useful for search results
	 * @param c
	 * @return
	 */
	Entity fromCursor(Cursor c);

}
