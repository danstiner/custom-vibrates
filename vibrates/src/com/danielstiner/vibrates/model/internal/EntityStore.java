package com.danielstiner.vibrates.model.internal;

import java.util.Arrays;
import java.util.List;

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

	// FIXME

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

	public Entity create(String name, long[] pattern, Entity.Kind type) {
		Uri created_uri = null;

		try {
			// Get a content resolver to work with
			ContentResolver cr = mContentResolverProvider.get(mContext);

			// Perform insert of actual entity
			ContentValues entity_values = new ContentValues(4);
			entity_values.put(KEY_NAME, name);
			entity_values.put(KEY_KIND, type.toString());
			entity_values.put(KEY_PATTERN, stringify(pattern));
			entity_values.put(KEY_NOTIFY_COUNT, 0);

			created_uri = cr.insert(Entities.CONTENT_URI, entity_values);

		} catch (Exception tr) {
			Ln.d(tr, "Create entity failed.");
		}

		// Get an entity instance to represent this new entity
		Entity e = this.getEntity(created_uri);

		// TODO error handling
		return e;
	}

	public void delete(Entity entity) {
		// First open a connection to the database
		// SQLiteDatabase db = _db.getWritableDatabase();
		// Attempt the deletion of all relevant records
		try {
			// Get a content resolver to work with
			ContentResolver cr = mContentResolverProvider.get(mContext);

			// TODO Don't forget about associated lookups

			// Perform removal
			int deleteCount = cr.delete(Entities.CONTENT_URI, KEY_ID + " == ?",
					new String[] { entity.entityid().toString() });

			// Check if we actually removed a row
			if (deleteCount == 0) {
				Ln.d("Delete entity failed.");
			}

		} catch (Exception tr) {
			Ln.e(tr, "Delete entity failed.");
		}
	}

	private Entity getEntity(Uri created_uri) {
		// TODO Auto-generated method stub
		return null;
	}

	static final String NS = com.danielstiner.vibrates.Vibrates.NS + "."
			+ "storage";
	static final String CLASSNAME = NS + "." + "EntityStore";

	public static final String TABLE = "entities";

	protected static final String KEY_ID = Entities.ENTITY_ID;
	protected static final String KEY_KIND = Entities.KIND;
	protected static final String KEY_NAME = Entities.NAME;
	protected static final String KEY_PATTERN = Entities.PATTERN;
	protected static final String KEY_NOTIFY_COUNT = Entities.NOTIFY_COUNT;

	protected static final String EXTRA_CACHE_KEY_NAME = CLASSNAME + "."
			+ KEY_NAME;
	protected static final String EXTRA_CACHE_KEY_KIND = CLASSNAME + "."
			+ KEY_KIND;
	protected static final String EXTRA_CACHE_KEY_PATTERN = CLASSNAME + "."
			+ KEY_PATTERN;
	protected static final String EXTRA_CACHE_KEY_NOTIFY_COUNT = CLASSNAME
			+ "." + KEY_NOTIFY_COUNT;

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

	//
	// @Override
	// public Entity get(Long id) {
	// Cursor c = getCursor(id);
	//
	// try {
	// if (!c.moveToFirst()) {
	// Ln.d("Could not find entity %d", id);
	// return null;
	// }
	//
	// return fromCursor(c);
	//
	// } finally {
	// c.close();
	// }
	// }

	// private Cursor getCursor(Long id) {
	// Cursor c;
	// // Open a connection to the database
	// SQLiteDatabase sql_db = _db.getReadableDatabase();
	// try {
	// // Grab all contacts
	// c = sql_db.query(TABLE, new String[] { KEY_ID, KEY_NAME, KEY_KIND,
	// KEY_PATTERN, KEY_NOTIFY_COUNT }, KEY_ID + " = ?",
	// new String[] { id.toString() }, null, null, null);
	//
	// if (c.isClosed())
	// Ln.d("Opening Cursor failed");
	//
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// return c;
	// }

	// @Override
	// public Entity fromCursor(Cursor c) {
	//
	// Entity e = entity_provider.get();
	// e.entityid(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
	//
	// Bundle got_extras = e.getExtras();
	// // TODO: Only pull extras if they exist
	// got_extras.putString(EXTRA_CACHE_KEY_NAME,
	// c.getString(c.getColumnIndexOrThrow(KEY_NAME)));
	// got_extras.putString(EXTRA_CACHE_KEY_KIND,
	// c.getString(c.getColumnIndexOrThrow(KEY_KIND)));
	// got_extras.putString(EXTRA_CACHE_KEY_PATTERN,
	// c.getString(c.getColumnIndexOrThrow(KEY_PATTERN)));
	// got_extras.putInt(EXTRA_CACHE_KEY_NOTIFY_COUNT,
	// c.getInt(c.getColumnIndexOrThrow(KEY_NOTIFY_COUNT)));
	//
	// // TODO error handling
	// return e;
	//
	// // old way
	// // return get(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
	// }

	// @Override
	// public Cursor getAll() {
	// // Open a connection to the database
	// SQLiteDatabase db = _db.getReadableDatabase();
	// try {
	// Cursor c = db.query(TABLE, null, null, null, null, null, KEY_NAME);
	//
	// if (c.isClosed())
	// Ln.d("Opening Cursor failed");
	//
	// return c;
	// } finally {
	// if (db != null)
	// db.close();
	// }
	// }
	//
	// @Override
	// public Cursor getAll(String type) {
	// // Open a connection to the database
	// SQLiteDatabase db = _db.getReadableDatabase();
	// try {
	// // Grab all contacts
	// // return db.query(TABLE,
	// // null, //new String[] { KEY_ID },
	// // null, null, null, null, null, null);
	// // TODO Fix
	// Cursor c = db.query(TABLE, null, KEY_KIND + "= ?",
	// new String[] { type }, null, null, KEY_NAME);
	//
	// if (c.isClosed())
	// Ln.d("Opening Cursor failed");
	//
	// return c;
	// } finally {
	// if (db != null)
	// db.close();
	// }
	// }

	// @Override
	// public void setPattern(Entity entity, long[] pattern) {
	// // Open a connection to the database
	// SQLiteDatabase db = _db.getReadableDatabase();
	// try {
	// String stringified_pattern = stringify(pattern);
	//
	// ContentValues values = new ContentValues(1);
	// values.put(KEY_PATTERN, stringified_pattern);
	//
	// db.update(TABLE, values, KEY_ID + " = ?", new String[] { entity
	// .entityid().toString() });
	//
	// // update cache as well
	// entity.getExtras().putString(EXTRA_CACHE_KEY_PATTERN,
	// stringified_pattern);
	//
	// } finally {
	// if (db != null)
	// db.close();
	// }
	// }

	private String stringify(long[] pattern) {
		if (pattern == null)
			return "";

		String pattern_str = Arrays.toString(pattern);

		return pattern_str.substring(1, pattern_str.length() - 1);
	}

	public Entity get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Entity> getAll(Entity.Kind type) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Entity entity) {
		// TODO Auto-generated method stub

	}

}
