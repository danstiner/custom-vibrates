package com.danielstiner.vibrates.model.internal;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.model.IEntityFilter;

public interface IEntityStore {

	//Entity get(Long id);

	//List<Entity> getAll(Entity.Kind type);

	Entity create(String name, long[] pattern, Entity.Kind type);

	void delete(Entity entity);

	/**
	 * Commits changes made to entity
	 * 
	 * @param entity
	 */
	void update(Entity entity);

	void search(LoaderManager loaderManager, IEntityFilter filter,
			ISearchCallback callback);

	Entity fromCursor(Cursor c);

}
