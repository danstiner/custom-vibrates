package com.danielstiner.vibrates.model.internal;

import roboguice.inject.ContextScopedProvider;
import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.model.Entities;
import com.danielstiner.vibrates.model.IEntityFilter;
import com.google.inject.Inject;
import com.google.inject.Provider;

@ContextSingleton
public class EntityStore implements IEntityStore, IPatternStore {
	@Inject
	private Provider<Entity> mEntityProvider;

	@Inject
	private Application mContext;

	@Inject
	private ContextScopedProvider<ContentResolver> mContentResolverProvider;

	@Override
	public Entity create(String name, Pattern pattern, Entity.Kind kind) {
		Uri created_uri = null;

		try {
			// Get a content resolver to work with
			ContentResolver cr = mContentResolverProvider.get(mContext);

			// Perform insert of actual entity
			ContentValues entity_values = new ContentValues(4);
			entity_values.put(Entities.NAME, name);
			entity_values.put(Entities.KIND, kind.toString());
			entity_values.put(Entities.PATTERN, pattern.toString());
			entity_values.put(Entities.NOTIFY_COUNT, 0);

			created_uri = cr.insert(Entities.CONTENT_URI, entity_values);

		} catch (Exception tr) {
			Ln.d(tr, "Create entity failed.");
		}

		// Get an entity instance to represent this new entity
		Entity e = this.get(created_uri);

		// TODO error handling
		return e;
	}

	@Override
	public Entity get(Long id) {
		// Get a content resolver to work with
		ContentResolver cr = mContentResolverProvider.get(mContext);

		Cursor c = cr.query(Entities.getEntityUri(id), null, null, null, null);

		if (!c.moveToFirst())
			return null;

		return fromCursor(c);
	}

	@Override
	public Entity get(Uri uri) {
		// Get a content resolver to work with
		ContentResolver cr = mContentResolverProvider.get(mContext);

		Cursor c = cr.query(uri, null, null, null, null);

		if (!c.moveToFirst())
			return null;

		return fromCursor(c);
	}

	@Override
	public boolean update(Entity entity) {
		// Get a content resolver to work with
		ContentResolver cr = mContentResolverProvider.get(mContext);

		// Perform update of actual entity
		int update_rows = cr.update(Entities.getEntityUri(entity),
				entity.asContentValues(), null, null);

		if (update_rows == 1)
			return true;
		else if (update_rows > 1)
			throw new IllegalStateException(
					"Updated more than one entity somehow");

		return false;
	}

	@Override
	public boolean delete(Entity entity) {
		try {
			// Get a content resolver to work with
			ContentResolver cr = mContentResolverProvider.get(mContext);

			// TODO Don't forget about associated lookups

			// Perform removal
			int deleteCount = cr.delete(Entities.getEntityUri(entity), null,
					null);

			// Check if we actually removed a row
			if (deleteCount == 0) {
				return false;
			} else if (deleteCount > 1)
				throw new IllegalStateException(
						"Deleted more than one entity somehow");

		} catch (Exception tr) {
			Ln.e(tr, "Delete entity failed.");
			return false;
		}

		return true;
	}

	@Override
	public void search(LoaderManager loaderManager, IEntityFilter filter,
			ISearchCallback callback) {

		Loader<Cursor> l = loaderManager.getLoader(filter.getLoaderId());
		LoaderCallbacks<Cursor> lc = new EntitySearchLoaderCallbacks(mContext)
				.setFilter(filter).setCallback(callback);

		if (l == null)
			loaderManager.initLoader(filter.getLoaderId(), null, lc);
		else
			loaderManager.restartLoader(filter.getLoaderId(), null, lc);
	}

	@Override
	public Entity fromCursor(Cursor c) {
		Entity e = mEntityProvider.get();

		e.entityid(c.getLong(c.getColumnIndexOrThrow(Entities.ENTITY_ID)));
		e.setKind(Entity.Kind.fromString(c.getString(c
				.getColumnIndexOrThrow(Entities.KIND))));
		e.setName(c.getString(c.getColumnIndexOrThrow(Entities.NAME)));
		e.setNotifyCount(c.getInt(c
				.getColumnIndexOrThrow(Entities.NOTIFY_COUNT)));
		e.setPattern(Pattern.fromString(c.getString(c
				.getColumnIndexOrThrow(Entities.PATTERN))));

		return e;
	}

	// /**
	// *
	// * @param c
	// * @return
	// */
	// public static Uri getLookupUri(Entity c) {
	// // FIXME: Don't assume the identier is the contact lookup, or the id even
	// return ContactsContract.Contacts.getLookupUri(c.entityid(),
	// c.identifier());
	// }

}
